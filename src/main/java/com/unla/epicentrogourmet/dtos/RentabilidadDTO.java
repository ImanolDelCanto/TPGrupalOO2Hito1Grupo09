package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

// rentabilidad neta de una unidad, con cada parte de la cuenta (no se guarda en la base).
// desde y hasta quedan en null cuando es el festival completo.
public class RentabilidadDTO {

	private String nombreUnidad;
	private String nombreFestival;
	private BigDecimal recaudacion;
	private BigDecimal costoPlatos;
	private BigDecimal sueldos;
	private BigDecimal canon;
	private BigDecimal rentabilidadNeta;
	private LocalDate desde;
	private LocalDate hasta;
	private int dias;
	private int diasFestival;
	private LocalDate inicioFestival;
	private LocalDate finFestival;

	public RentabilidadDTO(String nombreUnidad, String nombreFestival, BigDecimal recaudacion,
			BigDecimal costoPlatos, BigDecimal sueldos, BigDecimal canon, BigDecimal rentabilidadNeta,
			LocalDate desde, LocalDate hasta, int dias, int diasFestival,
			LocalDate inicioFestival, LocalDate finFestival) {
		this.nombreUnidad = nombreUnidad;
		this.nombreFestival = nombreFestival;
		this.recaudacion = recaudacion;
		this.costoPlatos = costoPlatos;
		this.sueldos = sueldos;
		this.canon = canon;
		this.rentabilidadNeta = rentabilidadNeta;
		this.desde = desde;
		this.hasta = hasta;
		this.dias = dias;
		this.diasFestival = diasFestival;
		this.inicioFestival = inicioFestival;
		this.finFestival = finFestival;
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

	public LocalDate getDesde() {
		return desde;
	}

	public LocalDate getHasta() {
		return hasta;
	}

	public int getDias() {
		return dias;
	}

	public int getDiasFestival() {
		return diasFestival;
	}

	public LocalDate getInicioFestival() {
		return inicioFestival;
	}

	public LocalDate getFinFestival() {
		return finFestival;
	}
}