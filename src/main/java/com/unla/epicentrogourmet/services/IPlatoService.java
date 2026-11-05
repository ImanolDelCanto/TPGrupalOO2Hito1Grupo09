package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.AltaPlatoDTO;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;

public interface IPlatoService {

	// el menu de la unidad del responsable logueado, filtrado por nombre.
	// Con el nombre vacio o en null trae el menu completo.
	List<Plato> menuDeResponsable(String emailResponsable, String nombre);

	//alta de un plato en el menu de su unidad
	Plato altaPlato(String emailResponsable, AltaPlatoDTO dto);

	// la unidad del responsable logueado (la vista le pregunta si su festival termino)
	UnidadDeVenta unidadDeResponsable(String emailResponsable);
}