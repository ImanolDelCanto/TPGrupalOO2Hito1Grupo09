package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.epicentrogourmet.dtos.AltaPlatoDTO;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IPlatoService;

import jakarta.validation.Valid;

// Un controller por recurso
@Controller
@PreAuthorize("hasRole('ROLE_RESPONSABLE')")
@RequestMapping("/responsable/platos")
public class PlatoController {

	private final IPlatoService platoService;

	public PlatoController(IPlatoService platoService) {
		this.platoService = platoService;
	}

	// GET /responsable/platos -> el menu de su unidad + el formulario vacio
	@GetMapping("")
	public ModelAndView menu(Authentication auth) {
		return vistaMenu(auth.getName(), new AltaPlatoDTO());
	}

	// POST /responsable/platos -> agrega un plato al menu
	@PostMapping("")
	public ModelAndView alta(Authentication auth,
			@Valid @ModelAttribute("plato") AltaPlatoDTO plato,
			BindingResult result,
			RedirectAttributes redirect) {

		if (!result.hasErrors()) {
			try {
				Plato guardado = platoService.altaPlato(auth.getName(), plato);
				redirect.addFlashAttribute("exito", "Se agregó \"" + guardado.getNombre() + "\" al menú");
				return new ModelAndView("redirect:/responsable/platos");
			} catch (IllegalArgumentException e) {
				result.reject("plato", e.getMessage());
			}
		}
		// hubo errores: la misma pagina, con lo que habia escrito y los mensajes
		return vistaMenu(auth.getName(), null);
	}

	// arma la pagina; si dtoNuevo es null, se reusa el que ya trajo el formulario
	private ModelAndView vistaMenu(String email, AltaPlatoDTO dtoNuevo) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RESPONSABLE_PLATOS);
		if (dtoNuevo != null)
			mAV.addObject("plato", dtoNuevo);
		try {
			mAV.addObject("menu", platoService.menuDeResponsable(email));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}
}