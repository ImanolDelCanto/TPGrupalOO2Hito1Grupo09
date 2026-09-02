package datos;

import java.util.Set;

public abstract class UnidadDeVenta {

	protected long idUnidad;
	protected String nombreComercial;
	protected double superficie;
	protected String codigoUnico;
	protected Festival festival;
	protected Personal responsable;
	protected Set<Plato> platos;
	protected Set<Personal> staff;
	protected Set<Pedido> pedidos;
	
	public UnidadDeVenta() {
	}

	public UnidadDeVenta(String nombreComercial, double superficie, String codigoUnico, Festival festival,
			Personal responsable) {
		super();
		this.nombreComercial = nombreComercial;
		this.superficie = superficie;
		this.codigoUnico = codigoUnico;
		this.festival = festival;
		this.responsable = responsable;
	}

	public long getIdUnidad() {
		return idUnidad;
	}

	/*
	public void setIdUnidad(long idUnidad) {
		this.idUnidad = idUnidad;
	}
*/
	
	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public Festival getFestival() {
		return festival;
	}

	public void setFestival(Festival festival) {
		this.festival = festival;
	}

	public Personal getResponsable() {
		return responsable;
	}

	public void setResponsable(Personal responsable) {
		this.responsable = responsable;
	}

	public Set<Plato> getPlatos() {
		return platos;
	}

	public void setPlatos(Set<Plato> platos) {
		this.platos = platos;
	}

	public Set<Personal> getStaff() {
		return staff;
	}

	public void setStaff(Set<Personal> staff) {
		this.staff = staff;
	}

	public Set<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(Set<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

    public boolean validarCodigo() {
    	//Valida si el codigo es distinto de nulo y que tenga 10 caracteres
    	return this.codigoUnico != null && this.codigoUnico.length() == 10;
    }
	
	@Override
	public String toString() {
		return "UnidadDeVenta [idUnidad=" + idUnidad + ", nombreComercial=" + nombreComercial + ", superficie="
				+ superficie + ", codigoUnico=" + codigoUnico + ", responsable=" + responsable + "]";
	}
	
	
}
