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
@RequestMapping("/administracion/roles")
public class RolesController {

	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("section", "Administración");
		model.addAttribute("page", "Roles");
		model.addAttribute("complement", "Crear, Modificar, Eliminar, Consultar");

		return "/administracion/roles/index";
	}

	@GetMapping("/rol")
	public String usuario(Model model,
			@RequestParam(value = "id", required = false) BigDecimal txtIdRol,
			@RequestParam(value = "rol", required = false, defaultValue = "") String txtRol,
			@RequestParam(value = "descripcion", required = false, defaultValue = "") String txtDescripcion
			) {
		model.addAttribute("txtIdRol", txtIdRol);
		model.addAttribute("txtRol", txtRol.trim());
		model.addAttribute("txtDescripcion", txtDescripcion.trim());

		return "/administracion/roles/rol";
	}
}
