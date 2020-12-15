package mx.gob.queretaro.ws.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import mx.gob.queretaro.services.request.RolRequest;
import mx.gob.queretaro.services.service.IRolService;
import mx.gob.queretaro.services.util.CatalogoStrings;
import mx.gob.queretaro.services.util.Util;

@RestController
@RequestMapping("administracion/roles")
public class RolesRest {

	private final ResultadoResponse resultado;
	private final HttpSession session;
	private final IRolService rolService;

	@Autowired
	public RolesRest(IRolService rolService, ResultadoResponse resultado, HttpSession session) {
		this.rolService = rolService;
		this.resultado = resultado;
		this.session = session;
	}

	@GetMapping("/obtenerPorRol")
	public ResultadoResponse obtenerPorRol(@RequestParam(value = "txtRol", required = false, defaultValue = "") String txtRol) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(rolService.obtenerRolesPorRol(txtRol.trim()));
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@GetMapping("/obtenerTodos")
	public ResponseEntity<Map<String, Object>> obtenerTodos(
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int offset,
			@RequestParam(value = "order") String order,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "search", required = false, defaultValue = "") String search
	) {
		Map<String, Object> result = new HashMap<>();
		Page<TcRol> paginacion = rolService.obtenerRoles(limit, offset, order, sort, search);

		Long total = paginacion.getTotalElements();
		List<TcRol> roles = paginacion.getContent();

		result.put("total", total);
		result.put("rows", roles);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/obtenerRecursos")
	public ResultadoResponse obtenerRecursos(@RequestParam(value = "txtIdRol") long txtIdRol) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(rolService.obtenerRecursosPorIdRol(txtIdRol));
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@PostMapping("/rol")
	public ResultadoResponse guardar(@Valid @RequestBody RolRequest rolRequest, BindingResult errores) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());

			if (!errores.hasErrors()) {
				resultado.setEstatus(CatalogoStrings.getSuccess());
				resultado.setDatos(rolService.guardarRol(rolRequest, usuarioSesion.getUsuario().trim()));
				resultado.setMensaje("El rol se guardo con exito.");
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

	@PutMapping("/rol/{id}")
	public ResultadoResponse borrar(@PathVariable("id") long id) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(rolService.borrarRol(id, usuarioSesion.getUsuario().trim()));
			resultado.setMensaje("El rol se borro con exito.");
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@GetMapping("/obtenerRoles")
	public ResultadoResponse obtenerRoles() {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(rolService.obtenerRoles());
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

}
