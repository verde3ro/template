package mx.gob.queretaro.controller;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.util.CatalogoStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class CustomErrorController implements ErrorController  {

	private final ResultadoResponse resultado;
	private static final String XMLHTTPREQUEST = "XMLHttpRequest";
	private static final String XREQUESTDWITH = "X-Requested-With";

	@Autowired
	public CustomErrorController(ResultadoResponse resultado) {
		this.resultado = resultado;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@GetMapping(value = "/error")
	public Object handleError(Model model, HttpServletResponse response, HttpServletRequest request) {
		int status = response.getStatus();
		HttpHeaders headers = new HttpHeaders();
		Exception ex = (Exception) request.getAttribute("javax.servlet.error.exception");

		headers.setContentType(MediaType.APPLICATION_JSON);

		if (status > 0) {
			log.error("Ocurrió un error " + status, ex);

			if(status == HttpStatus.UNAUTHORIZED.value()) {
				if (XMLHTTPREQUEST.equals(request.getHeader(XREQUESTDWITH))) {
					resultado.setEstatus(CatalogoStrings.getWarning());
					resultado.setDatos(null);
					resultado.setMensaje("401 - No autorizado.");

					return new ResponseEntity<>(resultado, headers, HttpStatus.UNAUTHORIZED);
				} else {
					model.addAttribute("page", "Página de error 401");

					return "/error/401";
				}
			} else if(status == HttpStatus.FORBIDDEN.value()) {
				if (XMLHTTPREQUEST.equals(request.getHeader(XREQUESTDWITH))) {
					resultado.setEstatus(CatalogoStrings.getWarning());
					resultado.setDatos(null);
					resultado.setMensaje("403 - No autorizado.");

					return new ResponseEntity<>(resultado, headers, HttpStatus.FORBIDDEN);
				} else {

					model.addAttribute("page", "Página de error 403");

					return "/error/403";
				}
			} else if(status == HttpStatus.NOT_FOUND.value()) {
				if (XMLHTTPREQUEST.equals(request.getHeader(XREQUESTDWITH))) {
					resultado.setEstatus(CatalogoStrings.getWarning());
					resultado.setDatos(null);
					resultado.setMensaje("404 - Recurso no encontrado.");

					return new ResponseEntity<>(resultado, headers, HttpStatus.NOT_FOUND);
				} else {
					model.addAttribute("page", "Página de error 404");

					return "/error/404";
				}
			} else if(status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				if (XMLHTTPREQUEST.equals(request.getHeader(XREQUESTDWITH))) {
					resultado.setEstatus(CatalogoStrings.getWarning());
					resultado.setDatos(null);
					resultado.setMensaje("500 - Algo salió mal.");

					return new ResponseEntity<>(resultado, headers, HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					model.addAttribute("page", "Página de error 500");

					return "/error/500";
				}
			}
		}

		if (XMLHTTPREQUEST.equals(request.getHeader(XREQUESTDWITH))) {
			resultado.setEstatus(CatalogoStrings.getWarning());
			resultado.setDatos(null);
			resultado.setMensaje("400 - Petición incorrecta.");

			return new ResponseEntity<>(resultado, headers, HttpStatus.BAD_REQUEST);
		} else {
			model.addAttribute("page", "Página de error 400");

			return "/error/400";
		}
	}

}
