package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// <joined-subclass name="datos.Cocinero" table="cocinero"> del Hito 1
@Entity
@Table(name = "cocinero")
public class Cocinero extends Personal {

	private String especialidad;
	private double plusFijo;

	public Cocinero() {
	}

	public Cocinero(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
			LocalDate fechaIngreso, double sueldoBase, String especialidad, double plusFijo) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso, sueldoBase);
		this.especialidad = especialidad;
		this.plusFijo = plusFijo;
	}

	public String getEspecialidad() { return especialidad; }
	public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

	public double getPlusFijo() { return plusFijo; }
	public void setPlusFijo(double plusFijo) { this.plusFijo = plusFijo; }

	@Override
	public double getSueldoTotal() {
		return getSueldoBase() + plusFijo;
	}

	@Override
	public String toString() {
		return "Cocinero " + super.toString() + " - " + especialidad;
	}
}