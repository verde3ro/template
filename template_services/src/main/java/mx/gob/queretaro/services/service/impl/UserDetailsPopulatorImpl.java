package mx.gob.queretaro.services.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.repositories.repository.IUsuarioRepository;

@Service("ldapDetailsServiceImpl")
@Transactional(propagation = Propagation.REQUIRED)
public class UserDetailsPopulatorImpl implements LdapAuthoritiesPopulator {

	private final IUsuarioRepository usuarioRepository;
	private final HttpSession session;

	@Autowired
	public UserDetailsPopulatorImpl(IUsuarioRepository usuarioRepository, HttpSession session) {
		this.usuarioRepository = usuarioRepository;
		this.session = session;
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations datosUsuario,
			String nombreUsuario) {
		try {
			if (!nombreUsuario.trim().isEmpty()) {
				TcUsuario usuario = usuarioRepository.obtenerUsuarioPorUsuario(nombreUsuario, "AC");

				if (usuario != null) {
					List<TcRol> roles = usuarioRepository.obtenerRolesPorIdUsuario(usuario.getId(), "AC");

					if (!roles.isEmpty()) {
						Set<GrantedAuthority> grantedAuthorities;
						session.setAttribute("usuario", usuario);

						grantedAuthorities = roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getRol().trim())).collect(Collectors.toSet());

						return grantedAuthorities;
					} else {
						throw new UsernameNotFoundException("El usuario no tiene roles asignados en base de datos.");
					}
				} else {
					throw new UsernameNotFoundException("El usuario no se encontró en base de datos.");
				}
			} else {
				throw new UsernameNotFoundException("El usuario no debe ser vacío.");
			}
		} catch (UsernameNotFoundException ex) {
			throw new UsernameNotFoundException("Ocurrió un error: " + ex.getMessage());
		}
	}
}
