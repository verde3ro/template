package mx.gob.queretaro.ws.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.request.UsuarioRequest;
import mx.gob.queretaro.services.service.IUsuarioService;
import mx.gob.queretaro.services.util.CatalogoStrings;
import mx.gob.queretaro.services.util.Util;

/**
 *
 * @author rverde
 */
@RestController
@RequestMapping("administracion/usuarios")
public class UsuariosRest {

	private final ResultadoResponse resultado;
	private final HttpSession session;
	private final IUsuarioService usuarioService;

	@Autowired
	public UsuariosRest(ResultadoResponse resultado, HttpSession session, IUsuarioService usuarioService) {
		this.resultado = resultado;
		this.session = session;
		this.usuarioService = usuarioService;
	}

	@GetMapping("/obtenerTodos")
	public ResponseEntity <Map<String, Object>>obtenerTodos(@RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int offset, @RequestParam(value = "order") String order,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "search", required = false, defaultValue = "") String search)
	{
		Map<String, Object> result = new HashMap<>();
		Page<TcUsuario> paginacion;
		try {
			paginacion = usuarioService.obtenerUsuarios(limit, offset, order, sort, search);
			Long total = paginacion.getTotalElements();
			List<TcUsuario> usuarios = paginacion.getContent();

			for (TcUsuario usuario : usuarios) {
				String rolesTexto = null;
				List<TcRol> roles = usuarioService.obtenerRolesPorIdUsuario(usuario.getId());

				for (TcRol rol : roles) {
					rolesTexto = (null == rolesTexto) ? rol.getRol() : rolesTexto.concat(" - " + rol.getRol().trim());
				}

				usuario.setPassword(rolesTexto);
			}

			result.put("total", total);
			result.put("rows", usuarios);

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (InternalException e) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/obtenerRoles")
	public ResultadoResponse obtenerRoles(
			@RequestParam(value = "txtIdUsuario") long txtIdUsuario) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(usuarioService.obtenerRolesPorIdUsuario(txtIdUsuario));
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@PostMapping("/usuario")
	public ResultadoResponse guardar(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult errores) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());

			if (!errores.hasErrors()) {
				resultado.setEstatus(CatalogoStrings.getSuccess());
				resultado.setDatos(usuarioService.guardarUsuario(usuarioRequest, usuarioSesion.getUsuario()));
				resultado.setMensaje("El usuario se guardo con exito.");
			} else {
				resultado.setEstatus(CatalogoStrings.getError());
				resultado.setDatos(null);
				resultado.setMensaje(Util.getErrorsFieldsForm(errores));
			}
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@PutMapping("/usuario/{id}")
	public ResultadoResponse borrar(@PathVariable("id") long txtIdUsuario) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());

			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(usuarioService.borrarUsuario(txtIdUsuario, usuarioSesion.getUsuario().trim()));
			resultado.setMensaje("El usuario se borro con exito.");
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@GetMapping(path = "/obtenerUsuarioLdap", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultadoResponse obtenerUsuarioLdap(
			@RequestParam(value = "txtUsuario", defaultValue = "") String txtUsuario) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(usuarioService.obtenerUsuarioLdap(txtUsuario));
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}
		return resultado;
	}

}
