package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

public class RankingUnidadDTO {

	private String nombreComercial;
	private String codigoUnico;
	private String tipoUnidad;
	private int cantidadPedidos;
	private BigDecimal recaudacion;

	public RankingUnidadDTO(String nombreComercial, String codigoUnico, String tipoUnidad, int cantidadPedidos, BigDecimal recaudacion) {
		this.nombreComercial = nombreComercial;
		this.codigoUnico = codigoUnico;
		this.tipoUnidad = tipoUnidad;
		this.cantidadPedidos = cantidadPedidos;
		this.recaudacion = recaudacion;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public String getTipoUnidad() {
		return tipoUnidad;
	}

	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	public BigDecimal getRecaudacion() {
		return recaudacion;
	}
}