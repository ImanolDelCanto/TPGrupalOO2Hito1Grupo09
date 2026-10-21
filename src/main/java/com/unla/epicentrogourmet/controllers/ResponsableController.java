package com.unla.epicentrogourmet.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IPedidoService;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

@Controller
@PreAuthorize("hasRole('ROLE_RESPONSABLE')")
@RequestMapping("/responsable")
public class ResponsableController {

	private final IUnidadDeVentaService unidadDeVentaService;
	private final IPedidoService pedidoService;

	public ResponsableController(IUnidadDeVentaService unidadDeVentaService,
			IPedidoService pedidoService) {
		this.unidadDeVentaService = unidadDeVentaService;
		this.pedidoService = pedidoService;
	}

	@GetMapping("")
	public ModelAndView index(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RESPONSABLE_INDEX);
		mAV.addObject("username", auth.getName());
		unidadDeVentaService.findByResponsable(auth.getName())
				.ifPresent(unidad -> mAV.addObject("unidad", unidad));
		return mAV;
	}

	// GET /responsable/pedido -> formulario con el menu de su unidad
	@GetMapping("/pedido")
	public ModelAndView formularioPedido(Authentication auth) {
		return vistaFormulario(auth.getName(), null);
	}

	// POST /responsable/pedido -> guarda el pedido
	@PostMapping("/pedido")
	public ModelAndView altaPedido(Authentication auth,
			@RequestParam(required = false) List<Long> idsPlatos,
			@RequestParam(required = false) List<Integer> cantidades,
			RedirectAttributes redirect) {
		try {
			Pedido pedido = pedidoService.altaPedidoPorResponsable(auth.getName(), idsPlatos, cantidades);
			redirect.addFlashAttribute("exito",
					"Pedido #" + pedido.getIdPedido() + " cargado. Total: $ " + pedido.getTotal());
			return new ModelAndView("redirect:/responsable/pedidos");
		} catch (IllegalArgumentException e) {
			return vistaFormulario(auth.getName(), e.getMessage());
		}
	}

	// GET /responsable/pedidos -> los pedidos de su unidad
	@GetMapping("/pedidos")
	public ModelAndView listadoPedidos(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RESPONSABLE_PEDIDOS);
		try {
			mAV.addObject("pedidos", pedidoService.pedidosDeResponsable(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}

	// arma la vista del formulario; si hubo un error, lo muestra arriba
	private ModelAndView vistaFormulario(String email, String error) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.RESPONSABLE_PEDIDO);
		try {
			mAV.addObject("platos", pedidoService.platosDeResponsable(email));
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}
		mAV.addObject("error", error);
		return mAV;
	}
}
