package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.unla.epicentrogourmet.entities.RolCocina;
import com.unla.epicentrogourmet.entities.Turno;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AltaEmpleadoDTO {

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	private String apellido;

	@NotBlank(message = "El DNI es obligatorio")
	@Pattern(regexp = "\\d{7,8}", message = "El DNI son 7 u 8 numeros, sin puntos")
	private String dni;

	// el navegador manda las fechas como 2001-09-12 (formato ISO)
	@NotNull(message = "La fecha de nacimiento es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaNacimiento;

	@NotNull(message = "La fecha de ingreso es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaIngreso;

	// va a ser su usuario para iniciar sesion
	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email no tiene un formato valido")
	private String email;

	@NotNull(message = "Elegi si trabaja en cocina o en caja")
	private TipoPersonal tipo;

	// uno de los dos, segun el tipo. La Factory se encarga de exigir el que va.
	private RolCocina rolCocina;
	private Turno turno;

	public AltaEmpleadoDTO() {
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

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public TipoPersonal getTipo() { return tipo; }
	public void setTipo(TipoPersonal tipo) { this.tipo = tipo; }

	public RolCocina getRolCocina() { return rolCocina; }
	public void setRolCocina(RolCocina rolCocina) { this.rolCocina = rolCocina; }

	public Turno getTurno() { return turno; }
	public void setTurno(Turno turno) { this.turno = turno; }
}