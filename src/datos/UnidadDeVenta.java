package datos;

import java.util.HashSet;
import java.util.Set;

public abstract class UnidadDeVenta {

	protected long idUnidad;
	protected String nombreComercial;
	protected double superficie;
	protected String codigoUnico;
	protected Festival festival;
	protected Personal responsable;
	protected Set<Plato> platos = new HashSet<Plato>();
	protected Set<Personal> staff = new HashSet<Personal>();
	protected Set<Pedido> pedidos = new HashSet<Pedido>();

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

	protected void setIdUnidad(long idUnidad) {
		this.idUnidad = idUnidad;
	}
	
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

	// deja los dos lados apuntandose: sin el setUnidad la FK idUnidad queda nula
	public void agregarAlStaff(Personal p) {
		staff.add(p);
		p.setUnidad(this);
	}
	
	// idem para pedidos: sin el setUnidad la FK idUnidad del pedido queda nula
	public void agregarPedido(Pedido p) {
	    pedidos.add(p);
	    p.setUnidad(this);
	}

	// cada empleado sabe calcular su propio sueldo
	public double getCostoSalarial() {
		double total = 0;
		for (Personal p : staff)
			total += p.getSueldoTotal();
		return total;
	}

	public int getCantidadDeStaff() {
		return staff.size();
	}

    public boolean validarCodigo() {
    	//Valida si el codigo es distinto de nulo y que tenga 10 caracteres
    	return this.codigoUnico != null && this.codigoUnico.length() == 10;
    }
	
    public abstract String getDetalleEspecifico();

    public double getFacturacionTotal() {
        double total = 0;
        for (Pedido p : pedidos)
            for (ItemPedido i : p.getItems())
                total += i.getSubtotal();
        return total;
    }

    public double getMargenTotal() {
        double margen = 0;
        for (Pedido p : pedidos)
            for (ItemPedido i : p.getItems())
                margen += (i.getPlato().getPrecioVenta() - i.getPlato().getCostoProduccion()) * i.getCantidad();
        return margen;
    }

    public int getCantidadDePedidos() {
        return pedidos.size();
    }
	
    
	@Override
	public String toString() {
		return "UnidadDeVenta [idUnidad=" + idUnidad + ", nombreComercial=" + nombreComercial + ", superficie="
				+ superficie + ", codigoUnico=" + codigoUnico + "]";
	}
	
	
}
