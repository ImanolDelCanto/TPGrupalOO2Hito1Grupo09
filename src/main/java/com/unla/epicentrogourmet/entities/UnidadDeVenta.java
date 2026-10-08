package com.unla.epicentrogourmet.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "unidadDeVenta")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UnidadDeVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long idUnidad;
	@Column(nullable = false)
	protected String nombreComercial;
	@Column(nullable = false)
	protected double superficie;
	@Column(nullable = false, unique = true)
	protected String codigoUnico;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFestival", nullable = false)
	protected Festival festival;
	// * (UnidadDeVenta) a 1 (Personal): el encargado de la unidad.
	// La FK idResponsable vive en esta tabla. El Hito 1 tenia not-null="false",
	// por eso el JoinColumn va sin nullable
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idResponsable")
	protected Personal responsable;
	// 1 (UnidadDeVenta) a * (Plato). mappedBy es el inverse="true" del Hito 1:
	// la FK idUnidad la escribe Plato.unidad, esta punta solo la lee
	@OneToMany(mappedBy = "unidad", fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	protected Set<Plato> platos = new HashSet<Plato>();
	// 1 (UnidadDeVenta) a * (Personal). mappedBy es el inverse="true" del Hito 1:
	// la FK idUnidad la escribe Personal.unidad, esta punta solo la lee
	@OneToMany(mappedBy = "unidad", fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	protected Set<Personal> staff = new HashSet<Personal>();
	// 1 (UnidadDeVenta) a * (Pedido). mappedBy es el inverse="true" del Hito 1:
	// la FK idUnidad la escribe Pedido.unidad, esta punta solo la lee
	@OneToMany(mappedBy = "unidad", fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	protected Set<Pedido> pedidos = new HashSet<Pedido>();

	public UnidadDeVenta() {
	}

	// sin responsable: al construir la unidad el staff todavia esta vacio, asi que
	// ningun empleado podria pasar la validacion de asignarResponsable. Se asigna
	// despues, cuando el staff ya esta cargado.
	public UnidadDeVenta(String nombreComercial, double superficie, String codigoUnico, Festival festival) {
	    super();
	    this.nombreComercial = nombreComercial;
	    this.superficie = superficie;
	    this.codigoUnico = codigoUnico;
	    this.festival = festival;
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