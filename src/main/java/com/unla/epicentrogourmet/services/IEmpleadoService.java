package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.TrabajoEnFestivalDTO;

public interface IEmpleadoService {

	// los festivales y unidades en los que trabajo, con lo que cobro en cada uno
	List<TrabajoEnFestivalDTO> trabajosPorEmail(String email);

	// de esa misma lista, el festival que esta en curso hoy. null si no hay ninguno.
	// Recibe la lista ya cargada para no volver a consultar la base.
	TrabajoEnFestivalDTO trabajoDeHoy(List<TrabajoEnFestivalDTO> trabajos);
}