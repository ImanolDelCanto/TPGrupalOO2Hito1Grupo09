package com.unla.epicentrogourmet.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.repositories.IFestivalRepository;
import com.unla.epicentrogourmet.services.IFestivalService;

@Service("festivalService")
public class FestivalService implements IFestivalService {

	private final IFestivalRepository festivalRepository;

	public FestivalService(IFestivalRepository festivalRepository) {
		this.festivalRepository = festivalRepository;
	}

	@Override
	public List<Festival> getAll() {
		return festivalRepository.findAllByOrderByFechaInicioDesc();
	}

	@Override
	public Optional<Festival> findById(Long id) {
		return festivalRepository.findById(id);
	}

	@Override
	public Festival insertOrUpdate(Festival festival) {
		if (!festival.periodoValido())
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
		return festivalRepository.save(festival);
	}
}
