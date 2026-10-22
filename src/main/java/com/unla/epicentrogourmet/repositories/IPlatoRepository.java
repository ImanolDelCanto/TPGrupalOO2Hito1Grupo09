package com.unla.epicentrogourmet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Plato;

@Repository("platoRepository")
public interface IPlatoRepository extends JpaRepository<Plato, Long> {

	public abstract List<Plato> findByUnidadIdUnidad(Long idUnidad);

	//el menu de una unidad, ordenado alfabeticamente (para el formulario)
	public abstract List<Plato> findByUnidadIdUnidadOrderByNombreAsc(Long idUnidad);

	//¿ya existe un plato con ese nombre en el menu de esta unidad?
	public abstract boolean existsByUnidadIdUnidadAndNombreIgnoreCase(Long idUnidad, String nombre);
}
