package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "cajero")
public class Cajero extends Personal {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Turno turno;

	public Cajero() {
	}

	public Cajero(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
			LocalDate fechaIngreso, Turno turno) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso);
		this.turno = turno;
	}

	public Turno getTurno() { return turno; }
	public void setTurno(Turno turno) { this.turno = turno; }

	// El cajero no cobra plus por categoria: cobra antiguedad.
	// Un monto por cada anio trabajado, definido por el festival.
	@Override
	public BigDecimal getPlus(Festival festival) {
		return festival.getValorCosto(TipoCosto.ANIO_ANTIGUEDAD)
				.multiply(BigDecimal.valueOf(getAntiguedad()));
	}

	@Override
	public String toString() {
		return "Cajero " + super.toString() + " - turno " + turno;
	}
}
