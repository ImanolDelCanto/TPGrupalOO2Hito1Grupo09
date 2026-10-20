package com.unla.epicentrogourmet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Mismo patron que el UserRole de la catedra: un usuario puede tener varios roles,
// cada uno es una fila. La diferencia: ellos guardan el rol como texto libre
// ("ROLE_ADMIN") y nosotros como el enum Rol, asi no se puede guardar mal escrito.
// El unique impide que el mismo usuario tenga dos veces el mismo rol.
@Entity
@Table(name = "usuario_rol", uniqueConstraints = @UniqueConstraint(columnNames = { "rol", "id_usuario" }))
public class UsuarioRol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuarioRol;

	// el nombre ya va en snake_case para que coincida con el del unique de arriba
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "rol", nullable = false)
	private Rol rol;

	public UsuarioRol() {
	}

	public UsuarioRol(Usuario usuario, Rol rol) {
		this.usuario = usuario;
		this.rol = rol;
	}

	public Long getIdUsuarioRol() {
		return idUsuarioRol;
	}

	protected void setIdUsuarioRol(Long idUsuarioRol) {
		this.idUsuarioRol = idUsuarioRol;
	}

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Rol getRol() { return rol; }
	public void setRol(Rol rol) { this.rol = rol; }
}
