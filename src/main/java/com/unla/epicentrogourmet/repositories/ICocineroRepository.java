package com.unla.epicentrogourmet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Cocinero;

// Un repositorio de Cocinero trae SOLO cocineros: como la herencia esta
// mapeada con @Inheritance(JOINED), JPA agrega el join con la tabla
// cocinero y filtra por tipo sin que haya que escribir nada.
// Por eso el filtro por puesto no necesita una consulta con condiciones.
@Repository("cocineroRepository")
public interface ICocineroRepository extends JpaRepository<Cocinero, Long> {

	public abstract List<Cocinero> findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
			Long idUnidad, String apellido);
}