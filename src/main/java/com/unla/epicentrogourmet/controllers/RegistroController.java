package com.unla.epicentrogourmet.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unla.epicentrogourmet.dtos.RegistroUnidadDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

import jakarta.validation.Valid;

// Publico: SecurityConfiguration ya deja entrar sin login a /registro/**
@Controller
@RequestMapping("/registro")
public class RegistroController {

	private final IUnidadDeVentaService unidadDeVentaService;
	private final IFestivalService festivalService;

	public RegistroController(IUnidadDeVentaService unidadDeVentaService,
			IFestivalService festivalService) {
		this.unidadDeVentaService = unidadDeVentaService;
		this.festivalService = festivalService;
	}

	// GET /registro/unidad -> muestra el formulario vacio
	@GetMapping("/unidad")
	public ModelAndView formulario() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REGISTRO_UNIDAD);
		mAV.addObject("registro", new RegistroUnidadDTO());
		mAV.addObject("festivales", festivalesDisponibles());
		return mAV;
	}

	// POST /registro/unidad -> recibe lo que se completo en el formulario
	@PostMapping("/unidad")
	public ModelAndView registrar(@Valid @ModelAttribute("registro") RegistroUnidadDTO registro,
			BindingResult result) {

		if (!result.hasErrors()) {
			try {
				unidadDeVentaService.registrarConResponsable(registro);
				return new ModelAndView("redirect:/login?registrado");
			} catch (IllegalArgumentException e) {
				// error de negocio (email repetido, codigo invalido...): va arriba del formulario
				result.reject("registro", e.getMessage());
			}
		}

		// hubo errores: se vuelve a mostrar el formulario con lo que ya habia escrito
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.REGISTRO_UNIDAD);
		mAV.addObject("festivales", festivalesDisponibles());
		return mAV;
	}

	// en el desplegable solo se ofrecen festivales que no terminaron
	private List<Festival> festivalesDisponibles() {
		return festivalService.getAll().stream()
				.filter(f -> !f.estaFinalizado())
				.toList();
	}
}
