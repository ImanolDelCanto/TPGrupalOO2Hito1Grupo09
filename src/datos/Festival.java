package datos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Festival {
	private long idFestival;
	private String nombre;
	private String temporada;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private double costoPorSuperficie;
	private double costoPorMontaje;
	private double plusElectricidad;
	private double costoSueldoBase;
	private Set<UnidadDeVenta> unidades = new HashSet<UnidadDeVenta>();
	
	public Festival() {

	}

	public Festival(long idFestival, String nombre, String temporada, LocalDate fechaInicio, LocalDate fechaFin, double costoPorSuperficie,
			double costoPorMontaje, double plusElectricidad, double costoSueldoBase) {
		super();
		this.idFestival = idFestival;
		this.nombre = nombre;
		this.temporada = temporada;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.costoPorSuperficie = costoPorSuperficie;
		this.costoPorMontaje = costoPorMontaje;
		this.plusElectricidad = plusElectricidad;
		this.costoSueldoBase = costoSueldoBase;
	}

	public long getIdFestival() {
		return idFestival;
	}

	public void setIdFestival(long idFestival) {
		this.idFestival = idFestival;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getCostoPorSuperficie() {
		return costoPorSuperficie;
	}

	public void setCostoPorSuperficie(double costoPorSuperficie) {
		this.costoPorSuperficie = costoPorSuperficie;
	}

	public double getCostoPorMontaje() {
		return costoPorMontaje;
	}

	public void setCostoPorMontaje(double costoPorMontaje) {
		this.costoPorMontaje = costoPorMontaje;
	}

	public double getPlusElectricidad() {
		return plusElectricidad;
	}

	public void setPlusElectricidad(double plusElectricidad) {
		this.plusElectricidad = plusElectricidad;
	}

	public double getCostoSueldoBase() {
		return costoSueldoBase;
	}

	public void setCostoSueldoBase(double costoSueldoBase) {
		this.costoSueldoBase = costoSueldoBase;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Set<UnidadDeVenta> getUnidades() {
		return unidades;
	}

	public void setUnidades(Set<UnidadDeVenta> unidades) {
		this.unidades = unidades;
	}

	@Override
	public String toString() {
		return "Festival [idFestival=" + idFestival + ", nombre=" + nombre + ", temporada=" + temporada + ", fechaFin="
				+ fechaFin + ", costoPorSuperficie=" + costoPorSuperficie + ", costoPorMontaje=" + costoPorMontaje
				+ ", plusElectricidad=" + plusElectricidad + ", costoSueldoBase=" + costoSueldoBase + "]";
	}
	
	
}
