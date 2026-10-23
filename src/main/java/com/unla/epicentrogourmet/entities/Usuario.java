package com.unla.epicentrogourmet.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// los roles van en una tabla aparte (UsuarioRol), no adentro de esta clase.
@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;

	// el email es el nombre de usuario con el que se inicia sesion
	@Column(nullable = false, unique = true)
	private String email;

	// 60 porque un hash BCrypt mide siempre exactamente 60 caracteres
	@Column(nullable = false, length = 60)
	private String password;

	// la baja es logica: no se borra la fila, se desactiva
	@Column(nullable = false)
	private Boolean activo = true;

	// el empleado recibe una clave generada por mail; esto marca si ya la cambio
	@Column(nullable = false)
	private Boolean primerIngresoHecho = false;

	// cascade ALL: un rol no existe sin su usuario. Guardar el usuario guarda sus roles.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UsuarioRol> roles = new HashSet<UsuarioRol>();

	public Usuario() {
	}

	public Usuario(String email, String password) {
		this.email = email;
		this.password = password;
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

	public Boolean getActivo() { return activo; }
	public void setActivo(Boolean activo) { this.activo = activo; }

	public Boolean getPrimerIngresoHecho() { return primerIngresoHecho; }
	public void setPrimerIngresoHecho(Boolean primerIngresoHecho) { this.primerIngresoHecho = primerIngresoHecho; }

	public Set<UsuarioRol> getRoles() { return roles; }
	public void setRoles(Set<UsuarioRol> roles) { this.roles = roles; }

	// deja los dos lados apuntandose
	public void agregarRol(Rol rol) {
		roles.add(new UsuarioRol(this, rol));
	}

	public boolean tieneRol(Rol rol) {
		for (UsuarioRol ur : roles)
			if (ur.getRol() == rol)
				return true;
		return false;
	}

	@Override
	public String toString() {
		return email;
	}
}
