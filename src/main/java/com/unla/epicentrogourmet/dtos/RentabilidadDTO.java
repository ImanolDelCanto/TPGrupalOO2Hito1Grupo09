package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

// rentabilidad neta de una unidad
public class RentabilidadDTO {

	private String nombreUnidad;
	private String nombreFestival;
	private BigDecimal recaudacion;
	private BigDecimal costoPlatos;
	private BigDecimal sueldos;
	private BigDecimal canon;
	private BigDecimal rentabilidadNeta;

	public RentabilidadDTO(String nombreUnidad, String nombreFestival, BigDecimal recaudacion,
			BigDecimal costoPlatos, BigDecimal sueldos, BigDecimal canon, BigDecimal rentabilidadNeta) {
		this.nombreUnidad = nombreUnidad;
		this.nombreFestival = nombreFestival;
		this.recaudacion = recaudacion;
		this.costoPlatos = costoPlatos;
		this.sueldos = sueldos;
		this.canon = canon;
		this.rentabilidadNeta = rentabilidadNeta;
	}

	public String getNombreUnidad() {
		return nombreUnidad;
	}

	public String getNombreFestival() {
		return nombreFestival;
	}

	public BigDecimal getRecaudacion() {
		return recaudacion;
	}

	public BigDecimal getCostoPlatos() {
		return costoPlatos;
	}

	public BigDecimal getSueldos() {
		return sueldos;
	}

	public BigDecimal getCanon() {
		return canon;
	}

	public BigDecimal getRentabilidadNeta() {
		return rentabilidadNeta;
	}
}
