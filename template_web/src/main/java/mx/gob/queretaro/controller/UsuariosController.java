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
@RequestMapping("/administracion/usuarios")
public class UsuariosController {

	@GetMapping("/index")
	public String index(Model model) {
		model.addAttribute("section", "Administración");
		model.addAttribute("page", "Usuarios");
		model.addAttribute("complement", "Crear, Modificar, Eliminar, Consultar");

		return "/administracion/usuarios/index";
	}

	@GetMapping("/usuario")
	public String usuario(Model model,
			@RequestParam(value = "id", required = false) BigDecimal txtIdUsuario,
			@RequestParam(value = "usuario", required = false, defaultValue = "") String txtUsuario,
			@RequestParam(value = "nombre", required = false, defaultValue = "") String txtNombre,
			@RequestParam(value = "correo", required = false, defaultValue = "") String txtCorreo,
			@RequestParam(value = "avUsuarioSecretariaDirec.jefeInmediato", required = false, defaultValue = "") String txtJefe,
			@RequestParam(value = "usuarioEbs", required = false, defaultValue = "") String txtUsuarioEBS,
			@RequestParam(value = "envioCorreo", required = false, defaultValue = "") String chEnvioCorreo
			)
	{
		model.addAttribute("txtIdUsuario", txtIdUsuario);
		model.addAttribute("txtUsuario", txtUsuario.trim());
		model.addAttribute("txtNombre", txtNombre.trim());
		model.addAttribute("txtCorreo", txtCorreo.trim());
		model.addAttribute("txtJefe", txtJefe.trim());
		model.addAttribute("chEnvioCorreo", (chEnvioCorreo.trim().isEmpty()) ? "Si" : (chEnvioCorreo.trim().equals("1")) ? "Si" : "No");

		model.addAttribute("txtUsuarioEBS", txtUsuarioEBS.trim());

		return "/administracion/usuarios/usuario";
	}

	@GetMapping("/secretariaDireccion")
	public String usuario() {
		return "/administracion/usuarios/secretariaDireccion";
	}
}
