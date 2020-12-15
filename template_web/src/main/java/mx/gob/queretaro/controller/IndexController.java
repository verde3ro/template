package mx.gob.queretaro.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping(value = {"/index", "/"})
	public String index(Model model, Principal principal) {

		if (principal == null) {
			return "index";
		} else {
			return "redirect:/inicio";
		}
	}
}
