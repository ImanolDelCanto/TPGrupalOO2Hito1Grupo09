package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "puestoDesarmable")
public class PuestoDesarmable extends UnidadDeVenta{
	@Column(nullable = false)
	private int cantidadCarpas;
	@Column(nullable = false)
	private int tiempoMontaje;
	
	public PuestoDesarmable() {
	}

	public PuestoDesarmable(String nombreComercial, double superficie, String codigoUnico, Festival festival,
			int cantidadCarpas, int tiempoMontaje) {
        super(nombreComercial, superficie, codigoUnico, festival);
		this.cantidadCarpas = cantidadCarpas;
		this.tiempoMontaje = tiempoMontaje;
	}

	public int getCantidadCarpas() {
		return cantidadCarpas;
	}

	public void setCantidadCarpas(int cantidadCarpas) {
		this.cantidadCarpas = cantidadCarpas;
	}

	public int getTiempoMontaje() {
		return tiempoMontaje;
	}

	public void setTiempoMontaje(int tiempoMontaje) {
		this.tiempoMontaje = tiempoMontaje;
	}

	@Override
	public BigDecimal getCostoOperativo() {
		BigDecimal porSuperficie = BigDecimal.valueOf(getSuperficie())
				.multiply(getFestival().getValorCosto(TipoCosto.POR_SUPERFICIE));
		BigDecimal porMontaje = BigDecimal.valueOf(tiempoMontaje)
				.multiply(getFestival().getValorCosto(TipoCosto.POR_MONTAJE));
		return porSuperficie.subtract(porMontaje);
	}

	@Override
	public String getDetalleEspecifico() {
	    return "carpas: " + cantidadCarpas + ", tiempo de montaje: " + tiempoMontaje + " min";
	}

	  @Override
  public String getTipoUnidad() {
      return "Puesto desarmable";
  }
	
	@Override
	public String toString() {
		return "PuestoDesarmable [" + super.toString() + ", cantidadCarpas=" + cantidadCarpas + ", tiempoMontaje=" + tiempoMontaje + "]";
	}
	
	
}
