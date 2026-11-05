package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.epicentrogourmet.dtos.ModificarUnidadDTO;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IFestivalService;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

import jakarta.validation.Valid;

// @PreAuthorize sobre la clase: TODOS los metodos de este controller exigen el rol
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
		return mAV;
	}

		// GET /admin/unidades/{id}/editar -> el formulario con los datos actuales de la unidad
	@GetMapping("/unidades/{idUnidad}/editar")
	public ModelAndView formularioEditarUnidad(@PathVariable long idUnidad, RedirectAttributes redirect) {
		try {
			ModelAndView mAV = new ModelAndView(ViewRouteHelper.ADMIN_UNIDAD_EDITAR);
			mAV.addObject("datos", unidadDeVentaService.datosDe(idUnidad));
			mAV.addObject("unidad", unidadDeVentaService.buscarPorId(idUnidad));
			return mAV;
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("error", e.getMessage());
			return new ModelAndView("redirect:" + ViewRouteHelper.ADMIN_ROOT);
		}
	}

	// POST /admin/unidades/{id}/editar -> guarda los cambios y vuelve al festival
	@PostMapping("/unidades/{idUnidad}/editar")
	public ModelAndView editarUnidad(@PathVariable long idUnidad,
			@Valid @ModelAttribute("datos") ModificarUnidadDTO datos,
			BindingResult result,
			RedirectAttributes redirect) {
		// el id sale de la direccion, no de un campo del formulario
		datos.setIdUnidad(idUnidad);

		if (!result.hasErrors()) {
			try {
				UnidadDeVenta guardada = unidadDeVentaService.modificarUnidad(datos);
				redirect.addFlashAttribute("exito",
						"Se actualizaron los datos de \"" + guardada.getNombreComercial() + "\"");
				return new ModelAndView(
						"redirect:/reportes/unidades?idFestival=" + guardada.getFestival().getIdFestival());
			} catch (IllegalArgumentException e) {
				// error de negocio: va arriba del formulario
				result.reject("datos", e.getMessage());
			}
		}

		// hubo errores: el mismo formulario, con lo que escribio y los mensajes
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.ADMIN_UNIDAD_EDITAR);
		mAV.addObject("unidad", unidadDeVentaService.buscarPorId(idUnidad));
		return mAV;
	}
}
