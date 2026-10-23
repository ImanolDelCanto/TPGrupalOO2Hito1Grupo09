package com.unla.epicentrogourmet.services.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.TrabajoEnFestivalDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.FoodTruck;
import com.unla.epicentrogourmet.entities.Personal;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.repositories.IPersonalRepository;
import com.unla.epicentrogourmet.services.IEmpleadoService;
import com.unla.epicentrogourmet.services.ILiquidacionService;

// Lo que el empleado ve de si mismo: donde trabajo, cuanto cobro
// y si hoy le toca trabajar.
@Service("empleadoService")
public class EmpleadoService implements IEmpleadoService {

	private final IPersonalRepository personalRepository;
	private final ILiquidacionService liquidacionService;

	public EmpleadoService(IPersonalRepository personalRepository,
			ILiquidacionService liquidacionService) {
		this.personalRepository = personalRepository;
		this.liquidacionService = liquidacionService;
	}

	// readOnly = true: getUnidad() y getFestival() son LAZY, y sin una
	// transaccion abierta tocarlos tira LazyInitializationException.
	@Override
	@Transactional(readOnly = true)
	public List<TrabajoEnFestivalDTO> trabajosPorEmail(String email) {
		List<TrabajoEnFestivalDTO> trabajos = new ArrayList<>();

		for (Personal asignacion : personalRepository.findAllByUsuarioEmail(email)) {
			// un empleado sin unidad todavia no tiene festival ni sueldo que mostrar
			if (asignacion.getUnidad() == null)
				continue;
			trabajos.add(armarTrabajo(asignacion));
		}

		// el mas reciente primero
		trabajos.sort(Comparator.comparing(TrabajoEnFestivalDTO::getFechaInicio).reversed());
		return trabajos;
	}

	@Override
	public TrabajoEnFestivalDTO trabajoDeHoy(List<TrabajoEnFestivalDTO> trabajos) {
		for (TrabajoEnFestivalDTO trabajo : trabajos) {
			if (trabajo.isActivo())
				return trabajo;
		}
		return null;
	}

	private TrabajoEnFestivalDTO armarTrabajo(Personal asignacion) {
		UnidadDeVenta unidad = asignacion.getUnidad();
		Festival festival = unidad.getFestival();

		TrabajoEnFestivalDTO trabajo = new TrabajoEnFestivalDTO();
		trabajo.setFestival(festival.getNombre());
		trabajo.setTemporada(festival.getTemporada().name());
		trabajo.setFechaInicio(festival.getFechaInicio());
		trabajo.setFechaFin(festival.getFechaFin());
		trabajo.setDiasFestival(festival.getDiasDuracion());
		// lo decide el festival comparando con la fecha de hoy
		trabajo.setActivo(festival.estaActivo());
		trabajo.setUnidad(unidad.getNombreComercial());
		trabajo.setTipoUnidad(unidad instanceof FoodTruck ? "Food Truck" : "Puesto desarmable");
		// el sueldo NO se recalcula aca: se le pide a LiquidacionService,
		// asi la formula del enunciado sigue escrita en un solo lugar
		trabajo.setTotalACobrar(
				liquidacionService.liquidarPorIdPersonal(asignacion.getIdPersonal()).getTotalACobrar());

		return trabajo;
	}
}