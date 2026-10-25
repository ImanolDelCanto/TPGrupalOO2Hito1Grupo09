package com.unla.epicentrogourmet.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.unla.epicentrogourmet.entities.RolCocina;
import com.unla.epicentrogourmet.entities.Turno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Lo que llega del formulario de modificacion. No persiste.
// Es parecido al de alta pero NO tiene el campo "tipo", y la diferencia es 
// el alta necesita el tipo para que la Factory decida que
// subclase construir, la modificacion no, porque la subclase ya esta
// decidida y una fila no cambia de clase.
public class ModificarEmpleadoDTO {

	// de que empleado se trata. Viaja escondido en el formulario, pero el
	// service igual chequea que sea de la unidad del responsable.
	private long idPersonal;

	@NotBlank(message = "El nombre es obligatorio")
	private String nombre;

	@NotBlank(message = "El apellido es obligatorio")
	private String apellido;

	@NotBlank(message = "El DNI es obligatorio")
	@Pattern(regexp = "\\d{7,8}", message = "El DNI son 7 u 8 numeros, sin puntos")
	private String dni;

	@NotNull(message = "La fecha de nacimiento es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaNacimiento;

	@NotNull(message = "La fecha de ingreso es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fechaIngreso;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "El email no tiene un formato valido")
	private String email;

	// Solo uno de los dos tiene sentido, segun que sea el empleado.
	// Cual corresponde lo sabe el service mirando la subclase, no este campo.
	private RolCocina rolCocina;
	private Turno turno;

	// solo para que la vista sepa que combo mostrar. El service no lo mira:
	// la subclase la lee de la base, no de lo que manda el formulario.
	private TipoPersonal tipo;

	public ModificarEmpleadoDTO() {
	}

	public long getIdPersonal() { return idPersonal; }
	public void setIdPersonal(long idPersonal) { this.idPersonal = idPersonal; }

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

	public RolCocina getRolCocina() { return rolCocina; }
	public void setRolCocina(RolCocina rolCocina) { this.rolCocina = rolCocina; }

	public Turno getTurno() { return turno; }
	public void setTurno(Turno turno) { this.turno = turno; }

	public TipoPersonal getTipo() { return tipo; }
	public void setTipo(TipoPersonal tipo) { this.tipo = tipo; }
}