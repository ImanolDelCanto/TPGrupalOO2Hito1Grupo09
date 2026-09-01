package datos;

import java.util.HashSet;
import java.util.Set;

// provisorio: la version definitiva la hace otro integrante
public abstract class UnidadDeVenta {

	private long idUnidad;
	private String nombreComercial;
	private double superficie;
	private String codigoUnico;

	private Personal responsable;

	private Set<Personal> staff = new HashSet<Personal>();

	public UnidadDeVenta() {
	}

	public UnidadDeVenta(String nombreComercial, double superficie, String codigoUnico) {
		this.nombreComercial = nombreComercial;
		this.superficie = superficie;
		this.codigoUnico = codigoUnico;
	}

	public long getIdUnidad() { return idUnidad; }
	protected void setIdUnidad(long idUnidad) { this.idUnidad = idUnidad; }

	public String getNombreComercial() { return nombreComercial; }
	public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

	public double getSuperficie() { return superficie; }
	public void setSuperficie(double superficie) { this.superficie = superficie; }

	public String getCodigoUnico() { return codigoUnico; }
	public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }

	public Personal getResponsable() { return responsable; }
	public void setResponsable(Personal responsable) { this.responsable = responsable; }

	public Set<Personal> getStaff() { return staff; }
	public void setStaff(Set<Personal> staff) { this.staff = staff; }

	// hay que setear los dos lados o la FK queda nula
	public void agregarAlStaff(Personal p) {
		staff.add(p);
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
		return codigoUnico != null && codigoUnico.trim().length() == 10;
	}

	@Override
	public String toString() {
		return nombreComercial + " [" + codigoUnico + "] - " + superficie + " m2";
	}
}
