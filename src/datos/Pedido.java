package datos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Pedido {
	private long idPedido;
	private LocalDate fecha;
	private Festival festival;
	private UnidadDeVenta unidad;
	private Set<ItemPedido> items = new HashSet<ItemPedido>();
	private double total;
	
	public Pedido() {
	}

	public Pedido(long idPedido, LocalDate fecha, Festival festival, UnidadDeVenta unidad, double total) {
		super();
		this.idPedido = idPedido;
		this.fecha = fecha;
		this.festival = festival;
		this.unidad = unidad;
		this.total = total;
	}

	public long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(long idPedido) {
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

	public Festival getFestival() {
		return festival;
	}

	public void setFestival(Festival festival) {
		this.festival = festival;
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
