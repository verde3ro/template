package mx.gob.queretaro.services.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.repositories.repository.IUsuarioRepository;

@Slf4j
@Service("userDetailsServiceImpl")
@Transactional(propagation = Propagation.REQUIRED)
public class UserDetailsServiceImpl implements UserDetailsService {

	private final IUsuarioRepository usuarioRepository;
	private final HttpSession session;

	@Autowired
	public UserDetailsServiceImpl(IUsuarioRepository usuarioRepository, HttpSession session) {
		this.usuarioRepository = usuarioRepository;
		this.session = session;
	}

	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) {
		try {
			if (!nombreUsuario.trim().isEmpty()) {
				TcUsuario usuario = usuarioRepository.obtenerUsuarioPorUsuario(nombreUsuario, "AC");

				if (usuario != null) {
					List<TcRol> roles = usuarioRepository.obtenerRolesPorIdUsuario(usuario.getId(),usuario.getEstatus());


					if (!roles.isEmpty()) {
						session.setAttribute("usuario", usuario);
						Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

						for (TcRol rol : roles) {
							grantedAuthorities.add(new SimpleGrantedAuthority(rol.getRol().trim()));
						}

						return new org.springframework.security.core.userdetails.User(usuario.getUsuario(), usuario.getPassword(), grantedAuthorities);
					} else {
						throw new UsernameNotFoundException("El usuario no tiene roles asignados en base de datos.");
					}
				} else {
					throw new UsernameNotFoundException("El usuario no se encontró en base de datos.");
				}
			} else {
				throw new UsernameNotFoundException("El usuario no debe ser vacío.");
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error en UserDetailsService.", ex);
			throw new UsernameNotFoundException("Ocurrió un error: " + ex.getMessage());
		}
	}
}
