package com.unla.epicentrogourmet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// <class name="datos.Plato" table="plato"> del Hito 1
@Entity
@Table(name = "plato")
public class Plato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPlato;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private double precioVenta;
	@Column(nullable = false)
	private double costoProduccion;
	// * (Plato) a 1 (UnidadDeVenta). Lado dueño: la FK idUnidad vive en esta tabla
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUnidad", nullable = false)
	private UnidadDeVenta unidad;
	
	
	public Plato() {
	}

	public Plato(String nombre, double precioVenta, double costoProduccion, UnidadDeVenta unidad) {
	    super();
	    this.nombre = nombre;
	    this.precioVenta = precioVenta;
	    this.costoProduccion = costoProduccion;
	    this.unidad = unidad;
	}


	public long getIdPlato() {
		return idPlato;
	}

	protected void setIdPlato(long idPlato) {
	    this.idPlato = idPlato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public double getCostoProduccion() {
		return costoProduccion;
	}

	public void setCostoProduccion(double costoProduccion) {
		this.costoProduccion = costoProduccion;
	}

	public UnidadDeVenta getUnidad() {
		return unidad;
	}

	public void setUnidad(UnidadDeVenta unidad) {
		this.unidad = unidad;
	}

	@Override
	public String toString() {
		return "Plato [idPlato=" + idPlato + ", nombre=" + nombre + ", precioVenta=" + precioVenta
				+ ", costoProduccion=" + costoProduccion + ", unidad=" + unidad + "]";
	}



	
}