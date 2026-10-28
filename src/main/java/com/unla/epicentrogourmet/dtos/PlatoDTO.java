package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

// un plato con cuantas veces se pidio
public class PlatoDTO {

	private String nombre;
	private BigDecimal precioVenta;
	private int cantidadPedidos;
	private int unidadesVendidas;

	public PlatoDTO(String nombre, BigDecimal precioVenta, int cantidadPedidos, int unidadesVendidas) {
		this.nombre = nombre;
		this.precioVenta = precioVenta;
		this.cantidadPedidos = cantidadPedidos;
		this.unidadesVendidas = unidadesVendidas;
	}

	public String getNombre() {
		return nombre;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	public int getUnidadesVendidas() {
		return unidadesVendidas;
	}
}