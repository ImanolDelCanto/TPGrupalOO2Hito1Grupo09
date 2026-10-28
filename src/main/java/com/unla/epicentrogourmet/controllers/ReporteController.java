package com.unla.epicentrogourmet.controllers;

import org.springframework.security.core.Authentication;
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

	// GET /reportes/mayor-canon?idFestival=2&cantidad=3
	@GetMapping("/mayor-canon")
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public ModelAndView mayorCanon(@RequestParam Long idFestival,
			@RequestParam(defaultValue = "3") int cantidad) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REPORTE_MAYOR_CANON);
		mAV.addObject("cantidad", cantidad);
		try {
			mAV.addObject("festival", reporteService.buscarFestival(idFestival));
			mAV.addObject("filas", reporteService.unidadesConMayorCanon(idFestival, cantidad));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}

	// GET /reportes/ranking?idFestival=2
	@GetMapping("/ranking")
	@PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
	public ModelAndView ranking(@RequestParam Long idFestival) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REPORTE_RANKING);
		try {
			mAV.addObject("festival", reporteService.buscarFestival(idFestival));
			mAV.addObject("filas", reporteService.rankingDeUnidades(idFestival));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}

	// GET /reportes/canon -> el canon de la unidad del responsable logueado
	@GetMapping("/canon")
	@PreAuthorize("hasRole('ROLE_RESPONSABLE')")
	public ModelAndView canonDeUnidad(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REPORTE_CANON);
		try {
			mAV.addObject("fila", reporteService.canonDeUnidad(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}
}