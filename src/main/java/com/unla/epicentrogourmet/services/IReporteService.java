package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.RankingUnidadDTO;
import com.unla.epicentrogourmet.dtos.ReporteMayoresCanonDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;



public interface IReporteService {

	// el festival de un reporte (titulo de la pantalla)
	Festival buscarFestival(Long idFestival);

	// las unidades de venta de un festival (admin)
	List<UnidadDeVenta> unidadesDeFestival(Long idFestival);

	// las x unidades que mas pagan de canon en un festival (admin)
	List<ReporteMayoresCanonDTO> unidadesConMayorCanon(Long idFestival, int cantidad);

	// las unidades de un festival ordenadas por recaudacion, de mayor a menor (admin)
	List<RankingUnidadDTO> rankingDeUnidades(Long idFestival);
}