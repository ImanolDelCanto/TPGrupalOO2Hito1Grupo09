package com.unla.epicentrogourmet.services;

import java.util.List;
import java.util.Optional;

import com.unla.epicentrogourmet.entities.UnidadDeVenta;

public interface IUnidadDeVentaService {

	List<UnidadDeVenta> getAll();

	List<UnidadDeVenta> findByFestival(Long idFestival);

	Optional<UnidadDeVenta> findByResponsable(String email);
}
