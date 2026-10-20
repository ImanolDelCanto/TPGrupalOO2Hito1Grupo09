package com.unla.epicentrogourmet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// La cuenta con la que se entra al sistema. Es independiente de Personal
// porque el Administrador tiene usuario pero no es empleado de ninguna unidad.
@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;

	// el email es el nombre de usuario con el que se inicia sesion
	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	// STRING y no ORDINAL: en la base se guarda "RESPONSABLE", no un numero.
	// Si manana agregamos un rol en el medio del enum, los datos viejos siguen bien.
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Rol rol;

	// la baja es logica: no se borra la fila, se desactiva
	@Column(nullable = false)
	private Boolean activo = true;

	// el empleado recibe una clave generada por mail; esto marca si ya la cambio
	@Column(nullable = false)
	private Boolean primerIngresoHecho = false;

	public Usuario() {
	}

	public Usuario(String email, String password, Rol rol) {
		this.email = email;
		this.password = password;
		this.rol = rol;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	protected void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public Rol getRol() { return rol; }
	public void setRol(Rol rol) { this.rol = rol; }

	public Boolean getActivo() { return activo; }
	public void setActivo(Boolean activo) { this.activo = activo; }

	public Boolean getPrimerIngresoHecho() { return primerIngresoHecho; }
	public void setPrimerIngresoHecho(Boolean primerIngresoHecho) { this.primerIngresoHecho = primerIngresoHecho; }

	@Override
	public String toString() {
		return email + " (" + rol + ")";
	}
}