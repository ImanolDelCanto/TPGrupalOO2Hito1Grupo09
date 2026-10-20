package com.unla.epicentrogourmet.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

@Service("unidadDeVentaService")
public class UnidadDeVentaService implements IUnidadDeVentaService {

	private final IUnidadDeVentaRepository unidadDeVentaRepository;

	public UnidadDeVentaService(IUnidadDeVentaRepository unidadDeVentaRepository) {
		this.unidadDeVentaRepository = unidadDeVentaRepository;
	}

	@Override
	public List<UnidadDeVenta> getAll() {
		return unidadDeVentaRepository.findAll();
	}

	@Override
	public List<UnidadDeVenta> findByFestival(Long idFestival) {
		return unidadDeVentaRepository.findByFestivalIdFestival(idFestival);
	}

	@Override
	public Optional<UnidadDeVenta> findByResponsable(String email) {
		return unidadDeVentaRepository.findByResponsableEmail(email);
	}
}
