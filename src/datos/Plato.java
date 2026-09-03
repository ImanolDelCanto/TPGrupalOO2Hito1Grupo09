package datos;

public class Plato {
	private long idPlato;
	private String nombre;
	private double precioVenta;
	private double costoProduccion;
	private UnidadDeVenta unidad;
	
	
	public Plato() {
	}

	public Plato(long idPlato, String nombre, double precioVenta, double costoProduccion, UnidadDeVenta unidad) {
		super();
		this.idPlato = idPlato;
		this.nombre = nombre;
		this.precioVenta = precioVenta;
		this.costoProduccion = costoProduccion;
		this.unidad = unidad;
	}


	public long getIdPlato() {
		return idPlato;
	}

	public void setIdPlato(long idPlato) {
		this.idPlato = idPlato;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public double getCostoProduccion() {
		return costoProduccion;
	}

	public void setCostoProduccion(double costoProduccion) {
		this.costoProduccion = costoProduccion;
	}

	public UnidadDeVenta getUnidad() {
		return unidad;
	}

	public void setUnidad(UnidadDeVenta unidad) {
		this.unidad = unidad;
	}

	@Override
	public String toString() {
		return "Plato [idPlato=" + idPlato + ", nombre=" + nombre + ", precioVenta=" + precioVenta
				+ ", costoProduccion=" + costoProduccion + ", unidad=" + unidad + "]";
	}



	
}
