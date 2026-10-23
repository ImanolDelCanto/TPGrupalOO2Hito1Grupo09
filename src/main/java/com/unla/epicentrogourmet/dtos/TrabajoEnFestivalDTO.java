package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

// Una fila de "mis festivales": donde trabajo el empleado y cuanto cobro ahi.
public class TrabajoEnFestivalDTO {

	private String festival;
	private String temporada;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private int diasFestival;
	private String unidad;
	private String tipoUnidad;
	private boolean activo;
	private BigDecimal totalACobrar;

	public TrabajoEnFestivalDTO() {
	}

	public String getFestival() { return festival; }
	public void setFestival(String festival) { this.festival = festival; }

	public String getTemporada() { return temporada; }
	public void setTemporada(String temporada) { this.temporada = temporada; }

	public LocalDate getFechaInicio() { return fechaInicio; }
	public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

	public LocalDate getFechaFin() { return fechaFin; }
	public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

	public int getDiasFestival() { return diasFestival; }
	public void setDiasFestival(int diasFestival) { this.diasFestival = diasFestival; }

	public String getUnidad() { return unidad; }
	public void setUnidad(String unidad) { this.unidad = unidad; }

	public String getTipoUnidad() { return tipoUnidad; }
	public void setTipoUnidad(String tipoUnidad) { this.tipoUnidad = tipoUnidad; }

	public boolean isActivo() { return activo; }
	public void setActivo(boolean activo) { this.activo = activo; }

	public BigDecimal getTotalACobrar() { return totalACobrar; }
	public void setTotalACobrar(BigDecimal totalACobrar) { this.totalACobrar = totalACobrar; }
}