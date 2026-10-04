package datos;

public class ItemPedido {
	private long idItemPedido;
	private int cantidad;
	private double subtotal;
	private Pedido pedido;
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
