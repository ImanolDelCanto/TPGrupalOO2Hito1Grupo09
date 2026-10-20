package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.ILiquidacionService;

@Controller
@PreAuthorize("hasRole('ROLE_EMPLEADO')")
@RequestMapping("/empleado")
public class EmpleadoController {

	private final ILiquidacionService liquidacionService;

	public EmpleadoController(ILiquidacionService liquidacionService) {
		this.liquidacionService = liquidacionService;
	}

	// auth.getName() devuelve el email del que inicio sesion:
	// con eso se busca su empleado y se le liquida el sueldo
	@GetMapping("")
	public ModelAndView index(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.EMPLEADO_INDEX);
		mAV.addObject("username", auth.getName());
		try {
			mAV.addObject("liquidacion", liquidacionService.liquidarPorEmail(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}
}
