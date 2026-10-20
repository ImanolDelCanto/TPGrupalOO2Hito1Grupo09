package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;

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

// Un parametro economico de UN festival: "en este festival el m2 cuesta 500".
// Es lo que pide la nota 4 del enunciado: no tener los numeros escritos
// a mano en los metodos.
@Entity
@Table(name = "costo")
public class Costo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCosto;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoCosto tipo;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal valor;

	// la FK idFestival vive en esta tabla
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFestival", nullable = false)
	private Festival festival;

	public Costo() {
	}

	public Costo(TipoCosto tipo, BigDecimal valor, Festival festival) {
		this.tipo = tipo;
		this.valor = valor;
		this.festival = festival;
	}

	public Long getIdCosto() {
		return idCosto;
	}

	protected void setIdCosto(Long idCosto) {
		this.idCosto = idCosto;
	}

	public TipoCosto getTipo() { return tipo; }
	public void setTipo(TipoCosto tipo) { this.tipo = tipo; }

	public BigDecimal getValor() { return valor; }
	public void setValor(BigDecimal valor) { this.valor = valor; }

	public Festival getFestival() { return festival; }
	public void setFestival(Festival festival) { this.festival = festival; }

	@Override
	public String toString() {
		return tipo + " = " + valor;
	}
}