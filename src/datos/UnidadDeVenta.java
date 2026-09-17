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
	    asignarResponsable(responsable);
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
	// lo usa Hibernate para reconstruir el objeto al leerlo de la base,
	// el staff puede no estar cargado todavia, por eso no puede exigir pertenencia al staff
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
	
	// Lo que le cuesta al festival tener esta unidad en el predio.
	// Cada tipo paga distinto, por eso es abstracto.
	public abstract double getCostoOperativo();
	
    public abstract String getDetalleEspecifico();
    
    public boolean validarCodigo() {
    	//Valida si el codigo es distinto de nulo y que tenga 10 caracteres
    	return this.codigoUnico != null && this.codigoUnico.length() == 10;
    }
	
    // asigna el responsable validando que sea parte del staff de esta unidad.
    // Es el metodo que hay que usar desde el codigo de aplicacion (no el setter)
	public void asignarResponsable(Personal responsable) {
	    if (responsable != null && !perteneceAlStaff(responsable)) {
	        throw new IllegalArgumentException("El responsable debe ser parte del staff de la unidad: " + responsable.getNombre());
	    }
	    this.responsable = responsable;
	}
	
	// se compara por id para que ande incluso si el staff y el responsable
	// vienen de consultas distintas (mismo empleado, objetos distintos)
	private boolean perteneceAlStaff(Personal p) {
	    for (Personal miembro : staff)
	        if (miembro.getIdPersonal() == p.getIdPersonal())
	            return true;
	    return false;
	}
	
	// agrega el empleado al staff y deja los dos lados apuntandose:
	// sin el setUnidad, la FK idUnidad de ese empleado queda nula
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
    
	@Override
	public String toString() {
		return "UnidadDeVenta [idUnidad=" + idUnidad + ", nombreComercial=" + nombreComercial + ", superficie="
				+ superficie + ", codigoUnico=" + codigoUnico + "]";
	}
	
	
}
