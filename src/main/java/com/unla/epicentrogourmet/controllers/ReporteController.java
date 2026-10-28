package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IReporteService;

// @PreAuthorize en cada metodo, diferencia reportes del admin y del responsable
@Controller
@RequestMapping("/reportes")
public class ReporteController {

	private final IReporteService reporteService;

	public ReporteController(IReporteService reporteService) {
		this.reporteService = reporteService;
	}

	// GET /reportes/unidades?idFestival=2
	@GetMapping("/unidades")
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public ModelAndView unidadesDeFestival(@RequestParam Long idFestival) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REPORTE_UNIDADES);
		try {
			mAV.addObject("festival", reporteService.buscarFestival(idFestival));
			mAV.addObject("unidades", reporteService.unidadesDeFestival(idFestival));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}
}