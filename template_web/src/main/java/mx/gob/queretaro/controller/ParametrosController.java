package mx.gob.queretaro.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author rverde
 */
@Controller
@RequestMapping("/administracion/parametros")
public class ParametrosController {

	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("section", "Administración");
		model.addAttribute("page", "Parametros");
		model.addAttribute("complement", "Crear, Modificar, Eliminar, Consultar");

		return "/administracion/parametros/index";
	}

	@GetMapping("/parametro")
	public String usuario(Model model,
			@RequestParam(value = "id", required = false) BigDecimal txtIdParametro,
			@RequestParam(value = "nombre", required = false, defaultValue = "") String txtNombre,
			@RequestParam(value = "valor", required = false, defaultValue = "") String txtValor
			) {
		model.addAttribute("txtIdParametro", txtIdParametro);
		model.addAttribute("txtNombre", txtNombre.trim());
		model.addAttribute("txtValor", txtValor.trim());

		return "/administracion/parametros/parametro";
	}
}
