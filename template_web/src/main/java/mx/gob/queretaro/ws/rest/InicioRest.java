package mx.gob.queretaro.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.response.ResultadoResponse;
import mx.gob.queretaro.services.service.IInicioService;
import mx.gob.queretaro.services.util.CatalogoStrings;

@RestController
@RequestMapping("inicio")
public class InicioRest {

	private final IInicioService inicioService;
	private final ResultadoResponse resultado;

	@Autowired
	public InicioRest(IInicioService inicioService, ResultadoResponse resultado) {
		this.inicioService = inicioService;
		this.resultado = resultado;
	}

	@GetMapping(value = "/obtenerAvisoPrivacidad", produces = "application/json;charset=UTF-8")
	public ResultadoResponse obtenerPorNombre() {
		try {
			resultado.setEstatus(CatalogoStrings.getSuccess());
			resultado.setDatos(inicioService.obtieneAvisoPrivacidad());
			resultado.setMensaje(null);
		} catch (InternalException ex) {
			resultado.setEstatus(CatalogoStrings.getError());
			resultado.setDatos(null);
			resultado.setMensaje(ex.getMessage());
		}
		return resultado;
	}

}
