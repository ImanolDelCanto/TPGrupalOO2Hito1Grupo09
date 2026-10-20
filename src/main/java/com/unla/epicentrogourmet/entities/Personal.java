package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// <class table="personal" abstract="true"> + <joined-subclass> del Hito 1
@Entity
@Table(name = "personal")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long idPersonal;
	@Column(nullable = false)
	protected String nombre;
	@Column(nullable = false)
	protected String apellido;
	@Column(nullable = false, unique = true)
	protected String dni;
	@Column(nullable = false)
	protected LocalDate fechaNacimiento;
	@Column(nullable = false)
	protected LocalDate fechaIngreso;
	// cada empleado pertenece a una unidad de venta
	// la FK idUnidad vive en esta tabla. Sin nullable=false: el Hito 1 tenia
	// not-null="false", un empleado puede existir sin unidad asignada
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUnidad")
	protected UnidadDeVenta unidad;

	// la cuenta con la que este empleado inicia sesion
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario")
	protected Usuario usuario;

	public Personal() {
	}

	public Personal(String nombre, String apellido, String dni,
			LocalDate fechaNacimiento, LocalDate fechaIngreso) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.fechaIngreso = fechaIngreso;
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

	public UnidadDeVenta getUnidad() { return unidad; }
	public void setUnidad(UnidadDeVenta unidad) { this.unidad = unidad; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public int getAntiguedad() {
		return Period.between(fechaIngreso, LocalDate.now()).getYears();
	}

	// Lo que este empleado cobra ADEMAS del sueldo base. Es lo unico que
	// cambia segun el tipo: el cocinero cobra plus por categoria, el cajero
	// cobra antiguedad. La formula completa de la liquidacion vive en el Service,
	// porque es la misma para los dos.
	public abstract BigDecimal getPlus(Festival festival);

	public boolean esMayorDeEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
	}

	@Override
	public String toString() {
		return apellido + ", " + nombre + " - DNI " + dni
				+ " - ingreso " + fechaIngreso + " (" + getAntiguedad() + " anios)";
	}
}
