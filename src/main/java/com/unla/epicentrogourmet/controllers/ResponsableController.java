package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

@Controller
@PreAuthorize("hasRole('ROLE_RESPONSABLE')")
@RequestMapping("/responsable")
public class ResponsableController {

	private final IUnidadDeVentaService unidadDeVentaService;

	public ResponsableController(IUnidadDeVentaService unidadDeVentaService) {
		this.unidadDeVentaService = unidadDeVentaService;
	}

	@GetMapping("")
	public ModelAndView index(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RESPONSABLE_INDEX);
		mAV.addObject("username", auth.getName());
		unidadDeVentaService.findByResponsable(auth.getName())
				.ifPresent(unidad -> mAV.addObject("unidad", unidad));
		return mAV;
	}
}
