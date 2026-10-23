package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

// @PreAuthorize sobre la clase: TODOS los metodos de este controller exigen
// el rol. Mismo mecanismo que DegreeController en el repo
@Controller
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
@RequestMapping("/admin")
public class AdminController {

	private final IFestivalService festivalService;
	private final IUnidadDeVentaService unidadDeVentaService;

	public AdminController(IFestivalService festivalService, IUnidadDeVentaService unidadDeVentaService) {
		this.festivalService = festivalService;
		this.unidadDeVentaService = unidadDeVentaService;
	}

	@GetMapping("")
	public ModelAndView index() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.ADMIN_INDEX);
		mAV.addObject("festivales", festivalService.getAll());
		mAV.addObject("unidades", unidadDeVentaService.getAll());
		return mAV;
	}
}
