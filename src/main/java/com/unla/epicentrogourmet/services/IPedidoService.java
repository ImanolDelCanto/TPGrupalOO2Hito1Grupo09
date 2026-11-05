package com.unla.epicentrogourmet.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.unla.epicentrogourmet.dtos.CierreCajaDTO;
import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;

//Cajero y Responsable pueden hacer pedidos
public interface IPedidoService {

	// el menu de la unidad del responsable, para armar el formulario
	List<Plato> platosDeResponsable(String emailResponsable);

	// los pedidos ya cargados de su unidad, para el listado
	Page<Pedido> pedidosDeResponsable(String emailResponsable, LocalDate desde, LocalDate hasta, Pageable pageable);

	// el alta: los platos elegidos y la cantidad de cada uno
	Pedido altaPedidoPorResponsable(String emailResponsable, List<Long> idsPlatos, List<Integer> cantidades);

	// el menu de la unidad del cajero, para armar el formulario
	List<Plato> platosDeCajero(String emailCajero);

    //los pedidos ya cargados de su unidad, para el listado
	Page<Pedido> pedidosDeCajero(String emailCajero, LocalDate desde, LocalDate hasta, Pageable pageable);

	// el alta: los platos elegidos y la cantidad de cada uno
	Pedido altaPedidoPorCajero(String emailCajero, List<Long> idsPlatos, List<Integer> cantidades);

	// si el usuario logueado es un cajero (para mostrar sus links en la barra)
	boolean esCajero(String email);

	// resumen de los pedidos de hoy de la unidad del cajero
	CierreCajaDTO cierreDeCaja(String emailCajero);

	// el cajero cierra la caja de hoy de su unidad
	void cerrarCaja(String emailCajero);

	// la unidad del cajero logueado (la vista le pregunta si su caja esta cerrada)
	UnidadDeVenta unidadDeCajero(String emailCajero);
}