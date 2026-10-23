package com.unla.epicentrogourmet.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;

@Controller
public class UserController {

	@GetMapping("/login")
	public String login(Model model,
						@RequestParam(required = false) String error,
						@RequestParam(required = false) String logout,
						@RequestParam(required = false) String registrado) {
		model.addAttribute("error", error);
		model.addAttribute("logout", logout);
		model.addAttribute("registrado", registrado);
		return ViewRouteHelper.USER_LOGIN;
	}

	// Spring manda aca despues de un login exitoso.
	// repartimos segun el rol.
	@GetMapping("/loginsuccess")
	public String loginCheck(Authentication auth) {
		if (tieneRol(auth, "ROLE_ADMINISTRADOR"))
			return "redirect:" + ViewRouteHelper.ADMIN_ROOT;
		if (tieneRol(auth, "ROLE_RESPONSABLE"))
			return "redirect:" + ViewRouteHelper.RESPONSABLE_ROOT;
		if (tieneRol(auth, "ROLE_EMPLEADO"))
			return "redirect:" + ViewRouteHelper.EMPLEADO_ROOT;
		return "redirect:" + ViewRouteHelper.ROUTE;
	}

	private boolean tieneRol(Authentication auth, String rol) {
		return auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals(rol));
	}
}
