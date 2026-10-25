package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

// El puesto ya viene armado como texto ("Cocina - COCINERO"): asi la vista
// no tiene que preguntar si el empleado es Cocinero o Cajero.
public class EmpleadoDeStaffDTO {

	private long idPersonal;
	private String apellidoNombre;
	private String dni;
	private String puesto;
	private String email;
	private LocalDate fechaIngreso;
	private int antiguedad;
	private boolean activo;

	public EmpleadoDeStaffDTO() {
	}

	public long getIdPersonal() { return idPersonal; }
	public void setIdPersonal(long idPersonal) { this.idPersonal = idPersonal; }

	public String getApellidoNombre() { return apellidoNombre; }
	public void setApellidoNombre(String apellidoNombre) { this.apellidoNombre = apellidoNombre; }

	public String getDni() { return dni; }
	public void setDni(String dni) { this.dni = dni; }

	public String getPuesto() { return puesto; }
	public void setPuesto(String puesto) { this.puesto = puesto; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public LocalDate getFechaIngreso() { return fechaIngreso; }
	public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

	public int getAntiguedad() { return antiguedad; }
	public void setAntiguedad(int antiguedad) { this.antiguedad = antiguedad; }

	public boolean isActivo() { return activo; }
	public void setActivo(boolean activo) { this.activo = activo; }
}