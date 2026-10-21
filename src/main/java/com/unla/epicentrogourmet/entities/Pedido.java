package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
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

import java.util.Collections;

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
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal total;
	
	// JPA necesita un constructor vacio para leer pedidos de la base
	// protected: Hibernate lo puede usar, pero desde el codigo no
	protected Pedido() {
	}

	// La fecha la pone el pedido: siempre del dia en que se carga
	public Pedido(UnidadDeVenta unidad) {
		this.fecha = LocalDate.now();
		this.unidad = unidad;
		this.total = BigDecimal.ZERO;
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

	public UnidadDeVenta getUnidad() {
		return unidad;
	}

	public void setUnidad(UnidadDeVenta unidad) {
		this.unidad = unidad;
	}

	// se devuelve una vista de solo lectura: desde afuera se puede recorrer,
	// pero no agregar ni sacar items sin pasar por agregarItem()
	public Set<ItemPedido> getItems() {
		return Collections.unmodifiableSet(items);
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	// El pedido crea sus propios items, porque los contiene
	// Valida, crea el item y actualiza el total: el pedido nunca queda inconsistente
	public void agregarItem(Plato plato, int cantidad) {
		if (cantidad <= 0)
			throw new IllegalArgumentException("La cantidad de " + plato.getNombre() + " debe ser mayor a 0");
		if (plato.getUnidad().getIdUnidad() != unidad.getIdUnidad())
			throw new IllegalArgumentException("El plato " + plato.getNombre() + " no es de esta unidad de venta");

		ItemPedido item = new ItemPedido(cantidad, this, plato);
		items.add(item);
		total = total.add(item.getSubtotal());
	}

	public boolean tieneItems() {
		return !items.isEmpty();
	}

	@Override
	public String toString() {
		return "Pedido [idPedido=" + idPedido + ", fecha=" + fecha + ", unidad=" + unidad + ", total=" + total + "]";
	}
	
	
	
}
