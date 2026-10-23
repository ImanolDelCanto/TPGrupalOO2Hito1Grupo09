package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.AltaPlatoDTO;
import com.unla.epicentrogourmet.entities.Plato;

public interface IPlatoService {

	//el menu de la unidad del responsable logueado
	List<Plato> menuDeResponsable(String emailResponsable);

	//alta de un plato en el menu de su unidad
	Plato altaPlato(String emailResponsable, AltaPlatoDTO dto);
}