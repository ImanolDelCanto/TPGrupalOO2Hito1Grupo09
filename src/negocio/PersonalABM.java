package negocio;

import java.util.List;

import dao.PersonalDao;
import datos.Cajero;
import datos.Cocinero;
import datos.Personal;

public class PersonalABM {

	private PersonalDao dao = new PersonalDao();

	public long agregar(Personal p) {
		if (!p.esMayorDeEdad()) {
			throw new IllegalArgumentException(
					"No se puede dar de alta a un menor de edad: " + p.getNombre()
					+ " " + p.getApellido());
		}
		return dao.agregar(p);
	}

	public Personal traer(long idPersonal) {
		return dao.traer(idPersonal);
	}

	public List<Personal> traerTodos() {
		return dao.traerTodos();
	}

	public List<Cocinero> traerCocinerosPorEspecialidad(String especialidad) {
		return dao.traerCocinerosPorEspecialidad(especialidad);
	}
	
	public List<Cajero> traerCajerosDeFoodTruckEnFestival(String turno, int antiguedadMinima, String temporada) {
		// el turno, el tipo de unidad, la antiguedad y el festival los filtra
		// el Dao en un solo HQL; aca solo validamos lo que entra
		if (antiguedadMinima < 0)
			throw new IllegalArgumentException("La antiguedad minima no puede ser negativa");
		return dao.traerCajerosPorTurnoUnidadYFestival(turno, antiguedadMinima, temporada);
	}
}
