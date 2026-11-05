package com.unla.epicentrogourmet.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

	// cuantos pedidos entran en cada pagina del listado
	private static final int POR_PAGINA = 5;

	// GET /cajero/pedidos -> los pedidos de su unidad
	@GetMapping("/pedidos")
	public ModelAndView listadoPedidos(Authentication auth,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate hasta,
			@RequestParam(defaultValue = "0") int pagina) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.CAJERO_PEDIDOS);
		mAV.addObject("desde", desde);
		mAV.addObject("hasta", hasta);
		try {
			Page<Pedido> pagPedidos = pedidoService.pedidosDeCajero(auth.getName(), desde, hasta, PageRequest.of(Math.max(pagina, 0), POR_PAGINA));
			mAV.addObject("pedidos", pagPedidos.getContent());
			mAV.addObject("pag", pagPedidos);
			mAV.addObject("unidad", pedidoService.unidadDeCajero(auth.getName()));
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
			mAV.addObject("unidad", pedidoService.unidadDeCajero(email));
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
			mAV.addObject("unidad", pedidoService.unidadDeCajero(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		return mAV;
	}

	// POST /cajero/cierre -> cierra la caja de hoy
	@PostMapping("/cierre")
	public ModelAndView cerrarCaja(Authentication auth, RedirectAttributes redirect) {
		try {
			pedidoService.cerrarCaja(auth.getName());
			redirect.addFlashAttribute("exito", "Caja cerrada. Hoy ya no se pueden cargar más pedidos.");
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("error", e.getMessage());
		}
		return new ModelAndView("redirect:/cajero/cierre");
	}
}