package datos;

import java.time.LocalDate;
import java.time.Period;

public abstract class Personal {

	protected long idPersonal;
	protected String nombre;
	protected String apellido;
	protected String dni;
	protected LocalDate fechaNacimiento;
	protected LocalDate fechaIngreso;
	protected double sueldoBase;

	// cada empleado pertenece a una unidad de venta
	protected UnidadDeVenta unidad;

	public Personal() {
	}

	public Personal(String nombre, String apellido, String dni,
			LocalDate fechaNacimiento, LocalDate fechaIngreso, double sueldoBase) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaIngreso = fechaIngreso;
		this.sueldoBase = sueldoBase;
	}

	public long getIdPersonal() {
		return idPersonal;
	}

	protected void setIdPersonal(long idPersonal) {
		this.idPersonal = idPersonal;
	}

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getApellido() { return apellido; }
	public void setApellido(String apellido) { this.apellido = apellido; }

	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public LocalDate getFechaNacimiento() { return fechaNacimiento; }
	public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

	public LocalDate getFechaIngreso() { return fechaIngreso; }
	public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

	public double getSueldoBase() { return sueldoBase; }
	public void setSueldoBase(double sueldoBase) { this.sueldoBase = sueldoBase; }

	public UnidadDeVenta getUnidad() { return unidad; }
	public void setUnidad(UnidadDeVenta unidad) { this.unidad = unidad; }

	public int getAntiguedad() {
		return Period.between(fechaIngreso, LocalDate.now()).getYears();
	}

	// cada subclase lo calcula a su manera
	public abstract double getSueldoTotal();

	public boolean esMayorDeEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
	}

	@Override
	public String toString() {
		return apellido + ", " + nombre + " - DNI " + dni
				+ " - ingreso " + fechaIngreso + " (" + getAntiguedad() + " anios)";
	}
}
