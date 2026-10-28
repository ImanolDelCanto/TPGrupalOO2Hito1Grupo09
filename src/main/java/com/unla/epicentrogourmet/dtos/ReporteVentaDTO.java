package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

// recaudacion total de un festival
public class ReporteVentaDTO {

	private String nombreFestival;
	private int cantidadPedidos;
	private BigDecimal recaudacionTotal;
	private String nombreUnidad;
	private BigDecimal recaudacionUnidad;

	public ReporteVentaDTO(String nombreFestival, int cantidadPedidos, BigDecimal recaudacionTotal,
			String nombreUnidad, BigDecimal recaudacionUnidad) {
		this.nombreFestival = nombreFestival;
		this.cantidadPedidos = cantidadPedidos;
		this.recaudacionTotal = recaudacionTotal;
		this.nombreUnidad = nombreUnidad;
		this.recaudacionUnidad = recaudacionUnidad;
	}

	public String getNombreFestival() {
		return nombreFestival;
	}

	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	public BigDecimal getRecaudacionTotal() {
		return recaudacionTotal;
	}

	public String getNombreUnidad() {
		return nombreUnidad;
	}

	public BigDecimal getRecaudacionUnidad() {
		return recaudacionUnidad;
	}
}
