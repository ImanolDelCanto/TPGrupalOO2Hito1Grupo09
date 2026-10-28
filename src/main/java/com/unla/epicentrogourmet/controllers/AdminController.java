package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;

// @PreAuthorize sobre la clase: TODOS los metodos de este controller exigen el rol
@Controller
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
@RequestMapping("/admin")
public class AdminController {

	private final IFestivalService festivalService;

	public AdminController(IFestivalService festivalService) {
		this.festivalService = festivalService;
	}

	@GetMapping("")
	public ModelAndView index() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.ADMIN_INDEX);
		mAV.addObject("festivales", festivalService.getAll());
		return mAV;
	}
}
