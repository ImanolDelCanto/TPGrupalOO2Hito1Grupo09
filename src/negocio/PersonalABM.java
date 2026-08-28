package negocio;

import java.util.List;

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
}
