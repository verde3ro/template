package mx.gob.queretaro.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

	@ExceptionHandler(Exception.class)
	public Object handle(Exception ex, Model model, HttpServletRequest request) {
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			Map<String, String> resultado = new HashMap<>();
			HttpHeaders headers = new HttpHeaders();
			HttpStatus status;
			String error;

			if (ex instanceof HttpRequestMethodNotSupportedException) {
				error = "Por favor actualiza la página e intenta de nuevo.";
				status = HttpStatus.METHOD_NOT_ALLOWED;
			} else {
				String mensaje = ex.fillInStackTrace().getMessage();

				if (mensaje.contains("ConstraintViolationException")) {
					error = "Por favor verifica que no estés intentado registrar un valor que ya está registrado.";
				} else {
					error = "Por favor comunícate con el administrador.";
				}
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}

			headers.setContentType(MediaType.APPLICATION_JSON);
			resultado.put("estatus", "warning");
			resultado.put("datos", null);
			resultado.put("mensaje", "Ocurrió un error: " + error);

			return new ResponseEntity<>(resultado, headers, status);
		} else {
			if (ex instanceof HttpRequestMethodNotSupportedException) {
				model.addAttribute("page", "Página de error 405");

				return "error/405";
			} else {
				model.addAttribute("page", "Página de error 500");

				return "error/500";
			}
		}
	}
}
