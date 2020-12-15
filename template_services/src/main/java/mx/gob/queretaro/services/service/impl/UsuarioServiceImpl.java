package mx.gob.queretaro.services.service.impl;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.repositories.model.TdUsuarioRol;
import mx.gob.queretaro.repositories.model.TdUsuarioRolPK;
import mx.gob.queretaro.repositories.repository.IParametroRepository;
import mx.gob.queretaro.repositories.repository.IUsuarioRepository;
import mx.gob.queretaro.repositories.repository.IUsuarioRolRepository;
import mx.gob.queretaro.services.request.RolRequest;
import mx.gob.queretaro.services.request.UsuarioRequest;
import mx.gob.queretaro.services.respose.GeqWsResponse;
import mx.gob.queretaro.services.service.IUsuarioService;
import mx.gob.queretaro.services.util.Aes;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UsuarioServiceImpl implements IUsuarioService {

	private final IUsuarioRepository usuarioRepository;
	private final IUsuarioRolRepository usuarioRolRepository;
	private final IParametroRepository parametroRepository;

	private String ldapUsuario;
	private String ldapPassword;
	private String rutaGeqQW;

	@Autowired
	public UsuarioServiceImpl(IUsuarioRepository usuarioRepository, IUsuarioRolRepository usuarioRolRepository, IParametroRepository parametroRepository) {
		this.usuarioRepository = usuarioRepository;
		this.usuarioRolRepository = usuarioRolRepository;
		this.parametroRepository = parametroRepository;
	}

	@PostConstruct
	public void init() {
		try {
			this.ldapUsuario = parametroRepository.obtenerPorNombre("LDAP_USUARIO", "AC").getValor().trim();
			this.ldapPassword = Aes.desencriptar(parametroRepository.obtenerPorNombre("LDAP_CONTRASENA", "AC").getValor().trim(), null);
			this.rutaGeqQW = parametroRepository.obtenerPorNombre("RUTA_GEQWS", "AC").getValor().trim();
		} catch (InternalException ex) {
			log.error("Ocurrió un error al inicializar los parámetros.", ex);
		}
	}

	@Override
	public TcUsuario obtenerUsuario(String usuario) throws InternalException {
		TcUsuario user;

		if (!usuario.trim().isEmpty()) {
			user = usuarioRepository.obtenerUsuarioPorUsuario(usuario, "AC");
		} else {
			throw new InternalException("El usuario no debe estar vacío.",UsuarioServiceImpl.class);
		}

		return user;
	}

	@Override
	public List<TcRol> obtenerRolesPorIdUsuario(long idUsuario)throws InternalException{
		if (idUsuario > 0) {
			try {
				return usuarioRepository.obtenerRolesPorIdUsuario(idUsuario, "AC");
			} catch (Exception ex) {
				log.error("Ocurrió un error al obtener los roles del usuario.", ex);
				throw new InternalException("Ocurrió un error al obtener los roles del usuario.", ex,UsuarioServiceImpl.class);
			}
		} else {
			throw new InternalException("El usuario no debe ser nulo.",UsuarioServiceImpl.class);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TcUsuario guardarUsuario(UsuarioRequest usuarioRequest, String usuarioSesion)throws InternalException {
		if (usuarioRequest != null && !usuarioRequest.getRoles().isEmpty() && usuarioSesion != null) {

			try {
				TcUsuario usuario;

				if (usuarioRequest.getId() == 0) {
					usuario = usuarioRepository.obtenerUsuarioPorUsuario(usuarioRequest.getUsuario().trim(), "IN");

					if (usuario == null) {
						usuario = new TcUsuario();

						usuario.setUsuario(usuarioRequest.getUsuario().trim());
						usuario.setNombre(usuarioRequest.getNombre().trim());
						usuario.setCorreo(usuarioRequest.getCorreo().trim());
						usuario.setEstatus("AC");
						usuario.setUsuarioI(usuarioSesion.trim());
						usuario.setFechaI(new Date());

						usuario = usuarioRepository.save(usuario);
					} else {
						actualizaUsuario(usuarioRequest, usuarioSesion, usuario);
					}
				} else {
					usuario =  usuarioRepository.obtenerUsuarioPorId(usuarioRequest.getId(), "AC");

					actualizaUsuario(usuarioRequest, usuarioSesion, usuario);
				}

				guardaUsuarioRol(usuarioRequest, usuarioSesion, usuario);

				return usuario;
			} catch (Exception ex) {
				log.error("Ocurrió un error al guardar el usuario.", ex);
				throw new InternalException("Ocurrió un error al guardar el usuario.", ex, UsuarioServiceImpl.class);
			}
		} else {
			throw new InternalException("El usuario no debe ser nulo", UsuarioServiceImpl.class);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long borrarUsuario(long idUsuario, String usuarioSesion)throws InternalException{
		if (idUsuario > 0 && usuarioSesion != null) {
			try {
				usuarioRepository.borrarUsuario(idUsuario);
				usuarioRepository.borrarUsuarioRol(idUsuario);

				return idUsuario;
			} catch (Exception ex) {
				log.error("Ocurrió un error al borrar el usuario.", ex);
				throw new InternalException("Ocurrió un error al borrar el usuario.", ex, UsuarioServiceImpl.class);
			}
		} else {
			throw new InternalException("El usuario no debe ser nulo o vacío .", UsuarioServiceImpl.class);
		}
	}

	@Override
	public Page<TcUsuario> obtenerUsuarios(int limit, int offset, String order, String sort, String search) throws InternalException{
		try {
			return usuarioRepository.obtenerUsuarios(search, PageRequest.of((offset / limit), limit, Sort.by(new Sort.Order((order.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC, sort))));
		} catch (Exception ex) {
			log.error("Ocurrió un error al obtener el listado de los usuarios.", ex);
			throw new InternalException("Ocurrió un error al obtener el listado de los usuarios.");
		}
	}

	@Override
	public Map<String, Object> obtenerUsuarioLdap(String usuario) throws InternalException {
		try {
			Map<String, Object> resultado = null;
			ObjectMapper mapper = new ObjectMapper();
			RestTemplate restTemplate = new RestTemplate();
			String auth = this.ldapUsuario.trim() + ":" + this.ldapPassword.trim();
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
			String authHeader = new String(encodedAuth);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Basic " + authHeader);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(this.rutaGeqQW.trim() + "ldap/" + usuario, HttpMethod.GET, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				GeqWsResponse wsResponse = mapper.readValue(response.getBody(), GeqWsResponse.class);

				if (wsResponse.getEstatus().trim().equals("success")) {
					resultado = wsResponse.getDatos();
				}
			} else {
				throw new InternalException("Ocurrió un error al conectarse al Web Service de usuarios, estatus: "+ response.getStatusCode() + ".");
			}
			return resultado;
		} catch (RuntimeException ex) {
			throw new InternalException("No se encontraron resultados");
		} catch (Exception ex) {
			log.error("Ocurrió un error al conectarse al Web Service de usuarios.", ex);
			throw new InternalException("Ocurrió un error al conectarse al Web Service de usuarios.");
		}
	}

	private void guardaUsuarioRol(UsuarioRequest usuarioRequest, String usuarioSesion, TcUsuario usuario) {
		TdUsuarioRol usuarioRol;

		for (RolRequest rolRequest : usuarioRequest.getRoles()) {
			usuarioRol = new TdUsuarioRol();
			usuarioRol.setId(new TdUsuarioRolPK(usuario.getId(), rolRequest.getId()));
			usuarioRol.setUsuarioI(usuarioSesion.trim());
			usuarioRol.setFechaI(new Date());
			usuarioRol.setEstatus("AC");

			usuarioRolRepository.save(usuarioRol);
		}
	}

	private void actualizaUsuario(UsuarioRequest usuarioRequest, String usuarioSesion, TcUsuario usuario) {
		usuario.setUsuario(usuarioRequest.getUsuario().trim());
		usuario.setNombre(usuarioRequest.getNombre().trim());
		usuario.setCorreo(usuarioRequest.getCorreo().trim());
		usuario.setEstatus("AC");
		usuario.setUsuarioU(usuarioSesion.trim());
		usuario.setFechaU(new Date());

		usuarioRepository.borrarUsuarioRol(usuario.getId());
		usuarioRepository.save(usuario);
	}

}
