package test;

import java.time.LocalDate;

import datos.Cajero;
import datos.Cocinero;
import datos.FoodTruck;
import datos.PuestoDesarmable;
import negocio.PersonalABM;
import negocio.UnidadDeVentaABM;

// el responsable se asigna al final: antes ese empleado no existe en la base
public class CargarUnidadesYStaff {

	public static void main(String[] args) {

		UnidadDeVentaABM unidadABM = new UnidadDeVentaABM();
		PersonalABM personalABM = new PersonalABM();

		// 1) las unidades, sin responsable
		FoodTruck truck = new FoodTruck("La Parrilla Rodante", 25.5, "FT00000001",
				"AB123CD", true);
		unidadABM.agregar(truck);

		PuestoDesarmable puesto = new PuestoDesarmable("Empanadas del Norte", 40.0, "PD00000001",
				3, 90);
		unidadABM.agregar(puesto);

		// 2) el staff, ya asignado a su unidad
		Cocinero juan = new Cocinero("Juan", "Perez", "30111222",
				LocalDate.of(1985, 4, 10), LocalDate.of(2019, 3, 1), 800000, "Parrilla", 150000);
		truck.agregarAlStaff(juan);
		personalABM.agregar(juan);

		Cocinero marcos = new Cocinero("Marcos", "Diaz", "35222111",
				LocalDate.of(1991, 1, 20), LocalDate.of(2023, 2, 1), 750000, "Parrilla", 90000);
		truck.agregarAlStaff(marcos);
		personalABM.agregar(marcos);

		Cajero ana = new Cajero("Ana", "Lopez", "32444555",
				LocalDate.of(1990, 8, 22), LocalDate.of(2022, 6, 15), 700000, "noche");
		truck.agregarAlStaff(ana);
		personalABM.agregar(ana);

		Cocinero lucia = new Cocinero("Lucia", "Gomez", "33444555",
				LocalDate.of(1988, 9, 3), LocalDate.of(2021, 7, 15), 780000, "Pasteleria", 120000);
		puesto.agregarAlStaff(lucia);
		personalABM.agregar(lucia);

		Cajero pedro = new Cajero("Pedro", "Sosa", "36777888",
				LocalDate.of(1995, 5, 5), LocalDate.of(2024, 1, 10), 690000, "mañana");
		puesto.agregarAlStaff(pedro);
		personalABM.agregar(pedro);

		// 3) recien ahora se puede asignar el responsable
		truck.setResponsable(juan);
		unidadABM.actualizar(truck);

		puesto.setResponsable(lucia);
		unidadABM.actualizar(puesto);

		System.out.printf("%nDatos cargados: 2 unidades y 5 empleados%n");
		System.exit(0);
	}
}
