package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// <joined-subclass name="datos.Cajero" table="cajero"> del Hito 1
@Entity
@Table(name = "cajero")
public class Cajero extends Personal {

	private String turno;
	private double recaudacion;

	public Cajero() {
	}

	public Cajero(String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso,
			double sueldoBase, String turno, double recaudacion) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso, sueldoBase);
		this.turno = turno;
		this.recaudacion = recaudacion;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public double getRecaudacion() {
		return recaudacion;
	}

	public void setRecaudacion(double recaudacion) {
		this.recaudacion = recaudacion;
	}

	@Override
	public double getSueldoTotal() {
		return getSueldoBase();
	}

	@Override
	public String toString() {
		return "Cajero [" + super.toString() + ", turno=" + turno + ", recaudacion=" + recaudacion + "]";
	}
}