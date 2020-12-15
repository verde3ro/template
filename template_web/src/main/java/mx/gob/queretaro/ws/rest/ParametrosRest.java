package mx.gob.queretaro.ws.rest;

import java.util.ArrayList;
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
import mx.gob.queretaro.repositories.model.TcParametro;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.request.ParametroRequest;
import mx.gob.queretaro.services.service.IParametroService;
import mx.gob.queretaro.services.util.CatalogoStrings;
import mx.gob.queretaro.services.util.Util;

@RestController
@RequestMapping("administracion/parametros")
public class ParametrosRest {

	private final IParametroService parametroService;
	private final ResultadoResponse resultado;
	private final HttpSession session;

	@Autowired
	public ParametrosRest(IParametroService parametroService, ResultadoResponse resultado, HttpSession session) {
		this.parametroService = parametroService;
		this.resultado = resultado;
		this.session = session;
	}

	@GetMapping("/obtenerPorNombre")
	public ResultadoResponse obtenerPorNombre(@RequestParam(value = "nombre", required = false, defaultValue = "") String nombre) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(parametroService.obtenerPorNombre(nombre));
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
		Page<TcParametro> paginacion = parametroService.obtenerPaginacion(limit, offset, order, sort, search);
		Long total = (paginacion != null) ? paginacion.getTotalElements() : 0L;
		List<TcParametro> parametros = (paginacion != null) ? paginacion.getContent() : new ArrayList<>();

		result.put("total", total);
		result.put("rows", parametros);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/parametro")
	public ResultadoResponse guardar(@Valid @RequestBody ParametroRequest parametroRequest, BindingResult errores) {
		try {
			TcUsuario usuario = (TcUsuario) session.getAttribute("usuario");

			if (!errores.hasErrors()) {
				resultado.setEstatus(CatalogoStrings.getSuccess());
				resultado.setDatos(parametroService.guardarParametro(parametroRequest, usuario.getUsuario().trim()));
				resultado.setMensaje("El parámetro se guardo con exito.");
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

	@PutMapping("/parametro/{id}")
	public ResultadoResponse borrar(@PathVariable("id") long id) {
		try {
			TcUsuario usuario = (TcUsuario) session.getAttribute("usuario");

			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(parametroService.borrarParametro(id, usuario.getUsuario().trim()));
			resultado.setMensaje("El parámetro se borro con exito.");
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}
}
