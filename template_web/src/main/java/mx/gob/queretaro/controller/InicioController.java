package mx.gob.queretaro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

	@GetMapping("/inicio")
	public String inicio(Model model) {
		model.addAttribute("page", "Inicio");
		model.addAttribute("complement", "Página Principal");

		return "inicio";
	}

}
