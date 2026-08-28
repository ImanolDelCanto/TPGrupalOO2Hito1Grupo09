package test;

import java.time.LocalDate;

import datos.Cajero;
import datos.Cocinero;
import negocio.PersonalABM;

/** Carga empleados de prueba pasando por la capa de negocio. */
public class CargarPersonal {

	public static void main(String[] args) {

		PersonalABM abm = new PersonalABM();

		abm.agregar(new Cocinero("Juan", "Perez", "30111222",
				LocalDate.of(1985, 4, 10), LocalDate.of(2019, 3, 1), 800000, "Parrilla", 150000));

		abm.agregar(new Cocinero("Lucia", "Gomez", "33444555",
				LocalDate.of(1988, 9, 3), LocalDate.of(2021, 7, 15), 780000, "Pasteleria", 120000));

		abm.agregar(new Cocinero("Marcos", "Diaz", "35222111",
				LocalDate.of(1991, 1, 20), LocalDate.of(2023, 2, 1), 750000, "Parrilla", 90000));

		abm.agregar(new Cajero("Ana", "Lopez", "32444555",
				LocalDate.of(1990, 8, 22), LocalDate.of(2022, 6, 15), 700000, "noche"));

		abm.agregar(new Cajero("Pedro", "Sosa", "36777888",
				LocalDate.of(1995, 5, 5), LocalDate.of(2024, 1, 10), 690000, "mañana"));

		System.out.println("\n>>> 5 empleados cargados (3 cocineros, 2 cajeros)");
		System.exit(0);
	}
}
