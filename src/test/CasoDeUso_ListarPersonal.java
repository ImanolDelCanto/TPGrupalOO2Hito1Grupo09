package test;

import java.util.List;

import datos.Cocinero;
import datos.Personal;
import negocio.PersonalABM;

/**
 * CASO DE USO: listar el personal del festival.
 *
 * Demuestra la HERENCIA: una consulta sobre la clase padre devuelve
 * cocineros y cajeros, cada uno con su tipo real.
 */
public class CasoDeUso_ListarPersonal {

	public static void main(String[] args) {

		PersonalABM abm = new PersonalABM();

		//trae toda la jerarquia
		List<Personal> todos = abm.traerTodos();
		System.out.printf("%nPersonal del festival (%d empleados):%n", todos.size());
		for (Personal p : todos)
			System.out.printf("%s%n", p);

		// consulta acotada a una subclase
		String especialidad = "Parrilla";
		List<Cocinero> cocineros = abm.traerCocinerosPorEspecialidad(especialidad);
		System.out.printf("%nCocineros de %s (%d):%n", especialidad, cocineros.size());
		for (Cocinero c : cocineros)
			System.out.printf("%s%n", c);

		System.exit(0);
	}
}
