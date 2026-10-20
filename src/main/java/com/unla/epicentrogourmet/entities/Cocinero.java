package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "cocinero")
public class Cocinero extends Personal {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RolCocina rolCocina;

	public Cocinero() {
	}

	public Cocinero(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
			LocalDate fechaIngreso, RolCocina rolCocina) {
		super(nombre, apellido, dni, fechaNacimiento, fechaIngreso);
		this.rolCocina = rolCocina;
	}

	public RolCocina getRolCocina() { return rolCocina; }
	public void setRolCocina(RolCocina rolCocina) { this.rolCocina = rolCocina; }

	// El cocinero cobra un plus segun su categoria. El monto no esta escrito
	// aca: se busca en los Costo del festival.
	@Override
	public BigDecimal getPlus(Festival festival) {
		if (rolCocina == RolCocina.COCINERO)
			return festival.getValorCosto(TipoCosto.PLUS_COCINERO);
		if (rolCocina == RolCocina.AYUDANTE)
			return festival.getValorCosto(TipoCosto.PLUS_COCINERO_AYUDANTE);
		if (rolCocina == RolCocina.LAVAPLATOS)
			return festival.getValorCosto(TipoCosto.PLUS_LAVAPLATOS);
		return BigDecimal.ZERO;
	}

	@Override
	public String toString() {
		return "Cocinero " + super.toString() + " - " + rolCocina;
	}
}
