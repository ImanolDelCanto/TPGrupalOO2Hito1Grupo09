package datos;

import java.time.LocalDate;
import java.time.Period;

//Clase base de la jerarquia de empleados.

public abstract class Personal {

	private long idPersonal;
	private String nombre;
	private String apellido;
	private String dni;
	private LocalDate fechaNacimiento;
	private LocalDate fechaIngreso;
	private double sueldoBase;

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

	public int getAntiguedad() {
		return Period.between(fechaIngreso, LocalDate.now()).getYears();
	}

	public boolean esMayorDeEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
	}

	@Override
	public String toString() {
		return apellido + ", " + nombre + " - DNI " + dni
				+ " - ingreso " + fechaIngreso + " (" + getAntiguedad() + " anios)";
	}
}
