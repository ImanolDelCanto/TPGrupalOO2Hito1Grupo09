package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
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
	// El encargado de la unidad. Ahora apunta a Usuario y no a Personal:
	// en el Hito 2 el responsable se registra junto con la unidad, antes de
	// que exista ningun empleado, asi que todavia no es parte del staff.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idResponsable")
	protected Usuario responsable;
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

	// sin responsable: en el registro se crea primero la unidad y despues
	// el Usuario del responsable, asi que se asigna con el setter.
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

	public Usuario getResponsable() {
		return responsable;
	}

	public void setResponsable(Usuario responsable) {
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
	
	// El canon: lo que la unidad paga por estar en el predio.
	// Cada tipo lo calcula distinto, por eso es abstracto. Los valores
	// ($ por m2, plus de electricidad) salen de los Costo del festival.
	public abstract BigDecimal getCostoOperativo();
	
    public abstract String getDetalleEspecifico();

	public abstract String getTipoUnidad();
    
    public boolean validarCodigo() {
    	//Valida si el codigo es distinto de nulo y que tenga 10 caracteres
    	return this.codigoUnico != null && this.codigoUnico.length() == 10;
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

	public int getCantidadDeStaff() {
		return staff.size();
	}
    
	@Override
	public String toString() {
		return "UnidadDeVenta [idUnidad=" + idUnidad + ", nombreComercial=" + nombreComercial + ", superficie="
				+ superficie + ", codigoUnico=" + codigoUnico + "]";
	}
	
	
}
