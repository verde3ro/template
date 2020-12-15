package mx.gob.queretaro.ws.rest;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.util.CatalogoStrings;

/**
 *
 * @author rverde
 */
@RestController
@Slf4j
public class SeguridadRest {

	private final ResultadoResponse resultado;
	private final HttpServletRequest request;

	@Autowired
	public SeguridadRest(ResultadoResponse resultado, HttpServletRequest request) {
		this.resultado = resultado;
		this.request = request;
	}

	@GetMapping("/isLogin")
	public ResultadoResponse isLogin() {

		try {
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				resultado.setEstatus(CatalogoStrings.getSuccess());

				if (auth != null && !auth.getName().equals("anonymousUser") && auth.isAuthenticated()) {
					resultado.setDatos(auth.getName());
				} else {
					resultado.setDatos(null);
				}

				resultado.setMensaje(null);
			} else {
				new HttpHeaders().setLocation(new URI(request.getServletContext().getContextPath() + "/index"));
			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), SeguridadRest.class);
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje("Ocurrió un error al verificar la sesión del usuario.");
		}

		return resultado;
	}

}
