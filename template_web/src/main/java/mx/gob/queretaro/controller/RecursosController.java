package mx.gob.queretaro.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author rverde
 */
@Controller
@RequestMapping("/administracion/recursos")
public class RecursosController {

	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("section", "Administración");
		model.addAttribute("page", "Recursos");
		model.addAttribute("complement", "Crear, Modificar, Eliminar, Consultar");

		return "/administracion/recursos/index";
	}

	@GetMapping("/recurso")
	public String usuario(Model model,
	                      @RequestParam(value = "id", required = false) BigDecimal txtIdRecurso,
	                      @RequestParam(value = "recurso", required = false, defaultValue = "") String txtRecurso,
	                      @RequestParam(value = "descripcion", required = false, defaultValue = "") String txtDescripcion,
	                      @RequestParam(value = "sesion", required = false, defaultValue = "") String cmbSesion
	) {
		model.addAttribute("txtIdRecurso", txtIdRecurso);
		model.addAttribute("txtRecurso", txtRecurso.trim());
		model.addAttribute("txtDescripcion", txtDescripcion.trim());
		model.addAttribute("cmbSesion", cmbSesion.trim());

		return "/administracion/recursos/recurso";
	}
}
