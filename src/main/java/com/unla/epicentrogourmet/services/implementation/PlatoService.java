package com.unla.epicentrogourmet.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.AltaPlatoDTO;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.repositories.IPlatoRepository;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.services.IPlatoService;

@Service("platoService")
public class PlatoService implements IPlatoService {

	private final IPlatoRepository platoRepository;
	private final IUnidadDeVentaRepository unidadDeVentaRepository;

	public PlatoService(IPlatoRepository platoRepository, IUnidadDeVentaRepository unidadDeVentaRepository) {
		this.platoRepository = platoRepository;
		this.unidadDeVentaRepository = unidadDeVentaRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Plato> menuDeResponsable(String emailResponsable) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return platoRepository.findByUnidadIdUnidadOrderByNombreAsc(unidad.getIdUnidad());
	}

	@Override
	@Transactional
	public Plato altaPlato(String emailResponsable, AltaPlatoDTO dto) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);

		if (unidad.getFestival().estaFinalizado())
			throw new IllegalArgumentException("El festival de tu unidad ya terminó: no se pueden agregar platos");

		String nombre = dto.getNombre() == null ? "" : dto.getNombre().trim();
		if (platoRepository.existsByUnidadIdUnidadAndNombreIgnoreCase(unidad.getIdUnidad(), nombre))
			throw new IllegalArgumentException("Ya tenés un plato llamado \"" + nombre + "\" en tu menú");

		// el constructor valida que el precio sea mayor que el costo
		Plato plato = new Plato(nombre, dto.getPrecioVenta(), dto.getCostoProduccion(), unidad);
		return platoRepository.save(plato);
	}

	private UnidadDeVenta unidadDeResponsable(String emailResponsable) {
		return unidadDeVentaRepository.findByResponsableEmail(emailResponsable)
				.orElseThrow(() -> new IllegalArgumentException("No tenés una unidad de venta asignada"));
	}
}