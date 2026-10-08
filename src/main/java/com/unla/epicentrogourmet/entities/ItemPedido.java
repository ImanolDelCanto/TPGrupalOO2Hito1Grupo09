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

// <class name="datos.ItemPedido" table="itemPedido"> del Hito 1
@Entity
@Table(name = "itemPedido")
public class ItemPedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idItemPedido;
	@Column(nullable = false)
	private int cantidad;
	@Column(nullable = false)
	private double subtotal;
	// * (ItemPedido) a 1 (Pedido). Lado dueño: la FK idPedido vive en esta tabla
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPedido", nullable = false)
	private Pedido pedido;
	// * (ItemPedido) a 1 (Plato)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlato", nullable = false)
	private Plato plato;
	
	public ItemPedido() {
	}

	public ItemPedido(int cantidad, Pedido pedido, Plato plato) {
	    super();
	    this.cantidad = cantidad;
	    this.pedido = pedido;
	    this.plato = plato;
	    this.subtotal = cantidad * plato.getPrecioVenta();
	}

	public long getIdItemPedido() {
		return idItemPedido;
	}

	protected void setIdItemPedido(long idItemPedido) {
		this.idItemPedido = idItemPedido;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Plato getPlato() {
		return plato;
	}

	public void setPlato(Plato plato) {
		this.plato = plato;
	}

	@Override
	public String toString() {
		return "ItemPedido [idItemPedido=" + idItemPedido + ", cantidad=" + cantidad + ", subtotal=" + subtotal
				+ ", pedido=" + pedido + ", plato=" + plato + "]";
	}
	
	
}