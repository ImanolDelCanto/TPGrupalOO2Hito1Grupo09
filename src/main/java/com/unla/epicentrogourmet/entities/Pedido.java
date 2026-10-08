package com.unla.epicentrogourmet.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// <class name="datos.Pedido" table="pedido"> del Hito 1
@Entity
@Table(name = "pedido")
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPedido;
	@Column(nullable = false)
	private LocalDate fecha;
	// * (Pedido) a 1 (UnidadDeVenta)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUnidad", nullable = false)
	private UnidadDeVenta unidad;
	// 1 (Pedido) a * (ItemPedido): composicion. cascade="all-delete-orphan" del
	// Hito 1 se escribe en JPA como cascade=ALL + orphanRemoval=true: si se borra
	// el pedido se borran sus items, y sacar un item de la coleccion lo borra
	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY,
			cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ItemPedido> items = new HashSet<ItemPedido>();
	@Column(nullable = false)
	private double total;
	
	public Pedido() {
	}

	public Pedido(LocalDate fecha, UnidadDeVenta unidad) {
	    super();
	    this.fecha = fecha;
	    this.unidad = unidad;
	    this.total = 0;
	}

	public long getIdPedido() {
		return idPedido;
	}

	protected void setIdPedido(long idPedido) {
	    this.idPedido = idPedido;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public UnidadDeVenta getUnidad() {
		return unidad;
	}

	public void setUnidad(UnidadDeVenta unidad) {
		this.unidad = unidad;
	}

	public Set<ItemPedido> getItems() {
		return items;
	}

	public void setItems(Set<ItemPedido> items) {
		this.items = items;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	// deja los dos lados apuntandose y actualiza el total del pedido
	public void agregarItem(ItemPedido item) {
	    items.add(item);
	    item.setPedido(this);
	    this.total += item.getSubtotal();
	}

	@Override
	public String toString() {
		return "Pedido [idPedido=" + idPedido + ", fecha=" + fecha + ", unidad=" + unidad + ", total=" + total + "]";
	}
	
	
	
}