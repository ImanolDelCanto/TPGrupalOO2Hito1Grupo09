package com.unla.epicentrogourmet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "festival")
public class Festival {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFestival;

	@Column(nullable = false)
	private String nombre;

	// para la pagina de inicio
	@Column(length = 1000)
	private String descripcion;

	private String imagenUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Temporada temporada;

	@Column(nullable = false)
	private LocalDate fechaInicio;

	@Column(nullable = false)
	private LocalDate fechaFin;

	// los cuatro valores sueltos del Hito 1 (costoPorSuperficie, costoPorMontaje,
	// plusElectricidad, costoSueldoBase) ahora son filas de esta coleccion
	@OneToMany(mappedBy = "festival", fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	private Set<Costo> costos = new HashSet<Costo>();

	@OneToMany(mappedBy = "festival", fetch = FetchType.LAZY,
			cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<UnidadDeVenta> unidades = new HashSet<UnidadDeVenta>();

	public Festival() {
	}

	public Festival(String nombre, Temporada temporada, LocalDate fechaInicio, LocalDate fechaFin) {
		this.nombre = nombre;
		this.temporada = temporada;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public Long getIdFestival() { return idFestival; }
	protected void setIdFestival(Long idFestival) { this.idFestival = idFestival; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getImagenUrl() { return imagenUrl; }
	public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

	public Temporada getTemporada() { return temporada; }
	public void setTemporada(Temporada temporada) { this.temporada = temporada; }

	public LocalDate getFechaInicio() { return fechaInicio; }
	public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

	public LocalDate getFechaFin() { return fechaFin; }
	public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

	public Set<Costo> getCostos() { return costos; }
	public void setCostos(Set<Costo> costos) { this.costos = costos; }

	public Set<UnidadDeVenta> getUnidades() { return unidades; }
	public void setUnidades(Set<UnidadDeVenta> unidades) { this.unidades = unidades; }

	// deja los dos lados apuntandose
	public void agregarCosto(Costo c) {
		costos.add(c);
		c.setFestival(this);
	}

	// El unico lugar del sistema que sabe buscar un valor economico.
	// Si el festival no lo tiene cargado devuelve cero en vez de romper.
	public BigDecimal getValorCosto(TipoCosto tipo) {
		for (Costo c : costos)
			if (c.getTipo() == tipo)
				return c.getValor();
		return BigDecimal.ZERO;
	}

	// dias que dura el evento, contando el primero y el ultimo
	public int getDiasDuracion() {
		return (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
	}

	// el periodo es valido si termina despues de empezar
	public boolean periodoValido() {
		return fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio);
	}

	public boolean estaActivo() {
		LocalDate hoy = LocalDate.now();
		return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
	}

	// ya termino: su fecha de fin quedo en el pasado
	public boolean estaFinalizado() {
    return fechaFin.isBefore(LocalDate.now());
	}

	@Override
	public String toString() {
		return nombre + " (" + temporada + ", " + fechaInicio + " a " + fechaFin + ")";
	}
}