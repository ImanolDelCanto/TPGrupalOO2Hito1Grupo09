package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "foodTruck")
public class FoodTruck extends UnidadDeVenta{
	@Column(nullable = false, unique = true)
	private String patente;
	@Column(nullable = false)
	private boolean requiereElectricidad;
	
	public FoodTruck() {
	}

	public FoodTruck(String nombreComercial, double superficie, String codigoUnico, Festival festival,
			String patente, boolean requiereElectricidad) {
		super(nombreComercial, superficie, codigoUnico, festival);
		this.patente = patente;
		this.requiereElectricidad = requiereElectricidad;
	}

	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public boolean isRequiereElectricidad() {
		return requiereElectricidad;
	}

	public void setRequiereElectricidad(boolean requiereElectricidad) {
		this.requiereElectricidad = requiereElectricidad;
	}
	
	@Override
	public String getDetalleEspecifico() {
	    return "patente: " + patente + ", requiere electricidad: " + (requiereElectricidad ? "si" : "no");
	}

	// Canon = superficie x $/m2, y si necesita electricidad suma el plus.
	// Los dos valores salen de los Costo del festival, no estan escritos aca.
	@Override
	public BigDecimal getCostoOperativo() {
		BigDecimal canon = BigDecimal.valueOf(getSuperficie())
				.multiply(getFestival().getValorCosto(TipoCosto.POR_SUPERFICIE));
		if (requiereElectricidad)
			canon = canon.add(getFestival().getValorCosto(TipoCosto.PLUS_ELECTRICIDAD));
		return canon;
	}

	@Override
	public String toString() {
		return "FoodTruck [" + super.toString() + ", patente=" + patente + ", requiereElectricidad=" + requiereElectricidad + "]";
	}
	
	
}
