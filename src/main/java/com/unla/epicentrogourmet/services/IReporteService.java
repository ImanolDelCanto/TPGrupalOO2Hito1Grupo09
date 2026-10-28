package com.unla.epicentrogourmet.services;

import java.math.BigDecimal;
import java.util.List;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;
import com.unla.epicentrogourmet.dtos.RankingUnidadDTO;
import com.unla.epicentrogourmet.dtos.ReporteMayoresCanonDTO;
import com.unla.epicentrogourmet.dtos.ReporteVentaDTO;
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

	// el canon de la unidad del responsable que esta logueado
	ReporteMayoresCanonDTO canonDeUnidad(String emailResponsable);

	// la recaudacion del festival de la unidad del responsable logueado
	ReporteVentaDTO recaudacionDelFestival(String emailResponsable);

	// la liquidacion de cada empleado activo de la unidad del responsable
	List<LiquidacionHaberesDTO> liquidacionDelStaff(String emailResponsable);

	// suma lo que cobra cada empleado de una liquidacion
	BigDecimal totalDeSueldos(List<LiquidacionHaberesDTO> liquidaciones);
}