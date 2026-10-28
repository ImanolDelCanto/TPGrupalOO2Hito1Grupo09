package com.unla.epicentrogourmet.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.repositories.IFestivalRepository;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.services.IReporteService;

@Service("reporteService")
public class ReporteService implements IReporteService {

	private final IFestivalRepository festivalRepository;
	private final IUnidadDeVentaRepository unidadDeVentaRepository;

	public ReporteService(IFestivalRepository festivalRepository,
			IUnidadDeVentaRepository unidadDeVentaRepository) {
		this.festivalRepository = festivalRepository;
		this.unidadDeVentaRepository = unidadDeVentaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Festival buscarFestival(Long idFestival) {
		if (idFestival == null)
			throw new IllegalArgumentException("Elegí un festival");
		return festivalRepository.findById(idFestival)
				.orElseThrow(() -> new IllegalArgumentException("El festival elegido no existe"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UnidadDeVenta> unidadesDeFestival(Long idFestival) {
		buscarFestival(idFestival);
		return unidadDeVentaRepository.findByFestivalIdFestival(idFestival);
	}
}