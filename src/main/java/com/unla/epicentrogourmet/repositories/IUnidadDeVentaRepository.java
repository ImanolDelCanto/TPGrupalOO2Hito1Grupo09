package com.unla.epicentrogourmet.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.UnidadDeVenta;

@Repository("unidadDeVentaRepository")
public interface IUnidadDeVentaRepository extends JpaRepository<UnidadDeVenta, Long> {

	public abstract List<UnidadDeVenta> findByFestivalIdFestival(Long idFestival);

	public abstract Optional<UnidadDeVenta> findByCodigoUnico(String codigoUnico);

	public abstract boolean existsByCodigoUnico(String codigoUnico);

	// la unidad que tiene a cargo un responsable
	public abstract Optional<UnidadDeVenta> findByResponsableEmail(String email);
}
