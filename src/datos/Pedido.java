package datos;

import java.time.LocalDate;

public class Pedido {
	private long idPedido;
	private LocalDate fecha;
	private UnidadDeVenta unidad;
	private ItemPedido items;
	private double total;
	
	public Pedido() {
	}

	public Pedido(long idPedido, LocalDate fecha, UnidadDeVenta unidad, ItemPedido items, double total) {
		super();
		this.idPedido = idPedido;
		this.fecha = fecha;
		this.unidad = unidad;
		this.items = items;
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

	public ItemPedido getItems() {
		return items;
	}

	public void setItems(ItemPedido items) {
		this.items = items;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Pedido [idPedido=" + idPedido + ", fecha=" + fecha + ", unidad=" + unidad + ", total=" + total + "]";
	}
	
	
	
}
