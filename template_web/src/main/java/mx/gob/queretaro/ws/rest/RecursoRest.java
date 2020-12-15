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
import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.request.RecursoRequest;
import mx.gob.queretaro.services.service.IRecursoService;
import mx.gob.queretaro.services.util.CatalogoStrings;
import mx.gob.queretaro.services.util.Util;

@RestController
@RequestMapping("administracion/recursos")
public class RecursoRest {

	private final IRecursoService recursoService;
	private final ResultadoResponse resultado;
	private final HttpSession session;

	@Autowired
	public RecursoRest(IRecursoService recursoService, ResultadoResponse resultado, HttpSession session) {
		this.recursoService = recursoService;
		this.resultado = resultado;
		this.session = session;
	}

	@GetMapping("/obtenerPorRecurso")
	public ResultadoResponse obtenerPorRecurso(@RequestParam(value = "txtRecurso", required = false, defaultValue = "") String txtRecurso) {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(recursoService.obtenerRecursosPorRecurso(txtRecurso));
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

	@GetMapping("/obtenerTodos")
	public ResponseEntity<Map<String, Object> > obtenerTodos(
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int offset,
			@RequestParam(value = "order") String order,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "search", required = false, defaultValue = "") String search
			) {
		Map<String, Object> result = new HashMap<>();
		long total;
		List<TcRecurso> recursos;

		Page<TcRecurso> paginacion = recursoService.obtenerPaginacion(limit, offset, order, sort, search);
		total = paginacion.getTotalElements();
		recursos = paginacion.getContent();

		result.put("total", total);
		result.put("rows", recursos);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/recurso")
	public ResultadoResponse guardar(@Valid @RequestBody RecursoRequest recursoRequest, BindingResult errores) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());

			if (!errores.hasErrors()) {
				resultado.setEstatus(CatalogoStrings.getSuccess());
				resultado.setDatos(recursoService.guardarRecurso(recursoRequest, usuarioSesion.getUsuario().trim()));
				resultado.setMensaje("El recurso se guardo con exito.");
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

	@PutMapping("/recurso/{id}")
	public ResultadoResponse borrar(@PathVariable("id") long id) {
		try {
			TcUsuario usuarioSesion = (TcUsuario) session.getAttribute(CatalogoStrings.getUsuario());

			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(recursoService.borrarRecurso(id, usuarioSesion.getUsuario().trim()));
			resultado.setMensaje("El recurso se borro con exito.");
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}

		return resultado;
	}

}
