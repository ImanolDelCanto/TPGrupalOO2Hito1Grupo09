package negocio;

import java.time.LocalDate;
import java.util.List;

import dao.FestivalDao;
import datos.Festival;

public class FestivalABM {

	private FestivalDao dao = new FestivalDao();

	public long agregar(Festival f) {
		if (f.getFechaFin().isBefore(f.getFechaInicio()))
			throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
		return dao.agregar(f);
	}

	public Festival traer(long idFestival) {
		return dao.traer(idFestival);
	}

	public List<Festival> traerTodos() {
		return dao.traerTodos();
	}

	public List<Festival> traerPorRango(LocalDate desde, LocalDate hasta) {
		return dao.traerPorRango(desde, hasta);
	}
}
