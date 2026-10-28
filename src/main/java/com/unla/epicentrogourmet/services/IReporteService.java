package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;

public interface IReporteService {

	// el festival de un reporte (titulo de la pantalla)
	Festival buscarFestival(Long idFestival);

	// las unidades de venta de un festival (admin)
	List<UnidadDeVenta> unidadesDeFestival(Long idFestival);
}