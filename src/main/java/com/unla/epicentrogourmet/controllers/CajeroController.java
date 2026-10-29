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

// Cajero carga pedidos y cierra la caja.
// El rol es EMPLEADO; que sea cajero lo valida el service.
@Controller
@PreAuthorize("hasRole('ROLE_EMPLEADO') and @pedidoService.esCajero(authentication.name)")
@RequestMapping("/cajero")
public class CajeroController {

	private final IPedidoService pedidoService;

	public CajeroController(IPedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	// GET /cajero/pedidos -> los pedidos de su unidad
	@GetMapping("/pedidos")
	public ModelAndView listadoPedidos(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.CAJERO_PEDIDOS);
		try {
			mAV.addObject("pedidos", pedidoService.pedidosDeCajero(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}

	// GET /cajero/pedido -> formulario con el menu de su unidad
	@GetMapping("/pedido")
	public ModelAndView formularioPedido(Authentication auth) {
		return vistaFormulario(auth.getName(), null);
	}

	// POST /cajero/pedido -> guarda el pedido
	@PostMapping("/pedido")
	public ModelAndView altaPedido(Authentication auth,
			@RequestParam(required = false) List<Long> idsPlatos,
			@RequestParam(required = false) List<Integer> cantidades,
			RedirectAttributes redirect) {
		try {
			Pedido pedido = pedidoService.altaPedidoPorCajero(auth.getName(), idsPlatos, cantidades);
			redirect.addFlashAttribute("exito",
					"Pedido #" + pedido.getIdPedido() + " cargado. Total: $ " + pedido.getTotal());
			return new ModelAndView("redirect:/cajero/pedidos");
		} catch (IllegalArgumentException e) {
			return vistaFormulario(auth.getName(), e.getMessage());
		}
	}

	// arma la vista del formulario; si hubo un error, lo muestra arriba
	private ModelAndView vistaFormulario(String email, String error) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.CAJERO_PEDIDO);
		try {
			mAV.addObject("platos", pedidoService.platosDeCajero(email));
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}
		mAV.addObject("error", error);
		return mAV;
	}

    // GET /cajero/cierre -> resumen de los pedidos de hoy de su unidad
	@GetMapping("/cierre")
	public ModelAndView cierreDeCaja(Authentication auth) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.CAJERO_CIERRE);
		try {
			mAV.addObject("cierre", pedidoService.cierreDeCaja(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}
}