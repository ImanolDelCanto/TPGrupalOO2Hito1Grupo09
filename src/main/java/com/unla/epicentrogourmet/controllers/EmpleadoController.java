package com.unla.epicentrogourmet.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.ILiquidacionService;
import com.unla.epicentrogourmet.services.IReciboPdfService;

@Controller
@PreAuthorize("hasRole('ROLE_EMPLEADO')")
@RequestMapping("/empleado")
public class EmpleadoController {

	private final ILiquidacionService liquidacionService;
	private final IReciboPdfService reciboPdfService;

	public EmpleadoController(ILiquidacionService liquidacionService,
			IReciboPdfService reciboPdfService) {
		this.liquidacionService = liquidacionService;
		this.reciboPdfService = reciboPdfService;
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

	// GET /empleado/recibo -> descarga el recibo en PDF.
	@GetMapping("/recibo")
	public ResponseEntity<byte[]> descargarRecibo(Authentication auth) {
		// la misma liquidacion que muestra la pantalla
		LiquidacionHaberesDTO liquidacion = liquidacionService.liquidarPorEmail(auth.getName());
		byte[] pdf = reciboPdfService.generar(liquidacion);

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				// "attachment" hace que el navegador lo descargue en vez de abrirlo
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recibo-de-sueldo.pdf\"")
				.body(pdf);
	}
}