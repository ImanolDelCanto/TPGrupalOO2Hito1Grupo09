package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.unla.epicentrogourmet.entities.Pedido;

// resumen de los pedidos del dia de una unidad
public class CierreCajaDTO {

	private String nombreUnidad;
	private LocalDate fecha;
	private List<Pedido> pedidos;
	private BigDecimal total;

	public CierreCajaDTO(String nombreUnidad, LocalDate fecha, List<Pedido> pedidos, BigDecimal total) {
		this.nombreUnidad = nombreUnidad;
		this.fecha = fecha;
		this.pedidos = pedidos;
		this.total = total;
	}

	public String getNombreUnidad() {
		return nombreUnidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public int getCantidadPedidos() {
		return pedidos.size();
	}

	public BigDecimal getTotal() {
		return total;
	}
}