package com.unla.epicentrogourmet.services;


import java.util.List;
import java.util.Optional;

import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.dtos.ModificarUnidadDTO;
import com.unla.epicentrogourmet.dtos.RegistroUnidadDTO;

public interface IUnidadDeVentaService {

	List<UnidadDeVenta> getAll();

	List<UnidadDeVenta> findByFestival(Long idFestival);

	Optional<UnidadDeVenta> findByResponsable(String email);

	UnidadDeVenta registrarConResponsable(RegistroUnidadDTO dto);

	// para el admin: la unidad que se edita, con su festival y su tipo
	UnidadDeVenta buscarPorId(long idUnidad);

	// los datos actuales de la unidad, para llenar el formulario de edicion
	ModificarUnidadDTO datosDe(long idUnidad);

	// guarda los cambios que hace el admin, con sus validaciones
	UnidadDeVenta modificarUnidad(ModificarUnidadDTO dto);
}
