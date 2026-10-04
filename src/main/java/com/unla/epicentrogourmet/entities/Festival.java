package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "festival")
public class Festival {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idFestival;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String temporada;
	@Column(nullable = false)
	private LocalDate fechaInicio;
	@Column(nullable = false)
	private LocalDate fechaFin;
	@Column(nullable = false)
	private double costoPorSuperficie;
	@Column(nullable = false)
	private double costoPorMontaje;
	@Column(nullable = false)
	private double plusElectricidad;
	@Column(name = "sueldoBase", nullable = false)
	private double costoSueldoBase;
	// todavia no se puede mapear: UnidadDeVenta no es @Entity.
	// En el paso 5 esto pasa a ser @OneToMany(mappedBy = "festival")
	@Transient
	private Set<UnidadDeVenta> unidades = new HashSet<UnidadDeVenta>();
	
	public Festival() {

	}

	public Festival(String nombre, String temporada, LocalDate fechaInicio, LocalDate fechaFin,
	        double costoPorSuperficie, double costoPorMontaje, double plusElectricidad, double costoSueldoBase) {
	    super();
	    this.nombre = nombre;
	    this.temporada = temporada;
	    this.fechaInicio = fechaInicio;
	    this.fechaFin = fechaFin;
	    this.costoPorSuperficie = costoPorSuperficie;
	    this.costoPorMontaje = costoPorMontaje;
	    this.plusElectricidad = plusElectricidad;
	    this.costoSueldoBase = costoSueldoBase;
	}

	protected void setIdFestival(long idFestival) {
	    this.idFestival = idFestival;
	}
	
	public long getIdFestival() {
		return idFestival;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getCostoPorSuperficie() {
		return costoPorSuperficie;
	}

	public void setCostoPorSuperficie(double costoPorSuperficie) {
		this.costoPorSuperficie = costoPorSuperficie;
	}

	public double getCostoPorMontaje() {
		return costoPorMontaje;
	}

	public void setCostoPorMontaje(double costoPorMontaje) {
		this.costoPorMontaje = costoPorMontaje;
	}

	public double getPlusElectricidad() {
		return plusElectricidad;
	}

	public void setPlusElectricidad(double plusElectricidad) {
		this.plusElectricidad = plusElectricidad;
	}

	public double getCostoSueldoBase() {
		return costoSueldoBase;
	}

	public void setCostoSueldoBase(double costoSueldoBase) {
		this.costoSueldoBase = costoSueldoBase;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Set<UnidadDeVenta> getUnidades() {
		return unidades;
	}

	public void setUnidades(Set<UnidadDeVenta> unidades) {
		this.unidades = unidades;
	}

	@Override
	public String toString() {
		return "Festival [idFestival=" + idFestival + ", nombre=" + nombre + ", temporada=" + temporada + ", fechaFin="
				+ fechaFin + ", costoPorSuperficie=" + costoPorSuperficie + ", costoPorMontaje=" + costoPorMontaje
				+ ", plusElectricidad=" + plusElectricidad + ", costoSueldoBase=" + costoSueldoBase + "]";
	}
	
	
}