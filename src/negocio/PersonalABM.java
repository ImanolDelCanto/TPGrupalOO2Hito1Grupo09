package negocio;

import java.util.List;
import java.util.ArrayList;
import datos.Cajero;
import datos.FoodTruck;

import dao.PersonalDao;
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
	
	public List<Cajero> traerCajerosDeFoodTruck(String turno, int antiguedadMinima) {
		List<Cajero> resultado = new ArrayList<Cajero>();
		// el turno y la antiguedad ya vienen filtrados por el Dao; aca solo
		// queda el tipo de unidad, que no se puede preguntar con el HQL de la catedra
		for (Cajero c : dao.traerCajerosPorTurnoConUnidad(turno, antiguedadMinima))
			if (c.getUnidad() instanceof FoodTruck)
				resultado.add(c);
		return resultado;
	}
}
