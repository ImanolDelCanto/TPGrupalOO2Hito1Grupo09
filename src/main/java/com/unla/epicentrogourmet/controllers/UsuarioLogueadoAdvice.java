package com.unla.epicentrogourmet.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.unla.epicentrogourmet.services.IPedidoService;

// agrega a todas las vistas si el usuario logueado es cajero,
// para que la barra muestre sus links solo a los cajeros
@ControllerAdvice
public class UsuarioLogueadoAdvice {

	private final IPedidoService pedidoService;

	public UsuarioLogueadoAdvice(IPedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@ModelAttribute("esCajero")
	public boolean esCajero(Authentication auth) {
		return auth != null && pedidoService.esCajero(auth.getName());
	}
}