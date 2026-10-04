package test;

import java.time.LocalDate;

import datos.Cajero;
import datos.Cocinero;
import datos.Festival;
import datos.FoodTruck;
import datos.PuestoDesarmable;
import negocio.FestivalABM;
import negocio.PersonalABM;
import negocio.UnidadDeVentaABM;

// el responsable se asigna al final: antes ese empleado no existe en la base
public class CargarUnidadesYStaff {

	public static void main(String[] args) {

		FestivalABM festivalABM = new FestivalABM();
		UnidadDeVentaABM unidadABM = new UnidadDeVentaABM();
		PersonalABM personalABM = new PersonalABM();

		// 1) el festival: las unidades no pueden existir sin uno
		Festival verano = new Festival("Sabores de Verano", "Verano", LocalDate.of(2026, 1, 10), LocalDate.of(2026, 2, 28), 1500, 800, 12000, 650000);
		festivalABM.agregar(verano);

		// 2) las unidades. El responsable se asigna al final: todavia no hay staff
		FoodTruck truck = new FoodTruck("La Parrilla Rodante", 25.5, "FT00000001", verano, "AB123CD", true);
		unidadABM.agregar(truck);

		PuestoDesarmable puesto = new PuestoDesarmable("Empanadas del Norte", 40.0, "PD00000001", verano, 3, 90);
		unidadABM.agregar(puesto);

		FoodTruck truck2 = new FoodTruck("Sushi al Paso", 18.0, "FT00000002", verano, "CD456EF", true);
		unidadABM.agregar(truck2);

		PuestoDesarmable puesto2 = new PuestoDesarmable("Cerveza Artesanal", 20.0, "PD00000002", verano, 1, 30);
		unidadABM.agregar(puesto2);

		// 3) el staff, ya asignado a su unidad
		Cocinero juan = new Cocinero("Juan", "Perez", "30111222", LocalDate.of(1985, 4, 10), LocalDate.of(2019, 3, 1), 800000, "Parrilla", 150000);
		truck.agregarAlStaff(juan);
		personalABM.agregar(juan);

		Cocinero marcos = new Cocinero("Marcos", "Diaz", "35222111", LocalDate.of(1991, 1, 20), LocalDate.of(2023, 2, 1), 750000, "Parrilla", 90000);
		truck.agregarAlStaff(marcos);
		personalABM.agregar(marcos);

		Cajero ana = new Cajero("Ana", "Lopez", "32444555", LocalDate.of(1990, 8, 22), LocalDate.of(2022, 6, 15), 700000, "noche", 2000);
		truck.agregarAlStaff(ana);
		personalABM.agregar(ana);

		Cocinero lucia = new Cocinero("Lucia", "Gomez", "33444555", LocalDate.of(1988, 9, 3), LocalDate.of(2021, 7, 15), 780000, "Pasteleria", 120000);
		puesto.agregarAlStaff(lucia);
		personalABM.agregar(lucia);

		Cajero pedro = new Cajero("Pedro", "Sosa", "36777888", LocalDate.of(1995, 5, 5), LocalDate.of(2024, 1, 10), 690000, "ma\u00f1ana", 3555);
		puesto.agregarAlStaff(pedro);
		personalABM.agregar(pedro);

		Cocinero martina = new Cocinero("Martina", "Vera", "31555666", LocalDate.of(1993, 6, 12), LocalDate.of(2024, 9, 1), 820000, "Sushi", 130000);
		truck2.agregarAlStaff(martina);
		personalABM.agregar(martina);

		Cajero facundo = new Cajero("Facundo", "Ibarra", "34777123", LocalDate.of(1997, 11, 2), LocalDate.of(2025, 3, 1), 690000, "manana", 1800);
		truck2.agregarAlStaff(facundo);
		personalABM.agregar(facundo);

		Cajero rocio = new Cajero("Rocio", "Molina", "35888234", LocalDate.of(1996, 2, 18), LocalDate.of(2024, 11, 1), 700000, "noche", 2500);
		puesto2.agregarAlStaff(rocio);
		personalABM.agregar(rocio);

		// 4) se asigna el responsable
		truck.asignarResponsable(juan);
		unidadABM.actualizar(truck);

		puesto.asignarResponsable(lucia);
		unidadABM.actualizar(puesto);

		truck2.asignarResponsable(martina);
		unidadABM.actualizar(truck2);

		puesto2.asignarResponsable(rocio);
		unidadABM.actualizar(puesto2);

		System.out.printf("%nDatos cargados: 1 festival, 4 unidades de venta y 8 empleados%n");
		System.exit(0);
	
	}
}
