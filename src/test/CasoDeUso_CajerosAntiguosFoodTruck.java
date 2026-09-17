package test;

import java.util.List;

import datos.Cajero;
import negocio.PersonalABM;

public class CasoDeUso_CajerosAntiguosFoodTruck {

	public static void main(String[] args) {
		//ENZO DIAZ

		PersonalABM abm = new PersonalABM();

		String turno = "noche";
		int antiguedadMinima = 2;
		String temporadaFestival = "Verano"; 

		List<Cajero> cajeros = abm.traerCajerosDeFoodTruckEnFestival(turno, antiguedadMinima, temporadaFestival);

		System.out.printf("%n=== CAJEROS DE TURNO %s, CON %d ANIOS O MAS, EN FOOD TRUCKS DEL FESTIVAL %s ===%n%n",
				turno.toUpperCase(), antiguedadMinima, temporadaFestival);

		if (cajeros.isEmpty())
			System.out.printf("Ningun cajero cumple la condicion.%n");

		for (Cajero c : cajeros)
			System.out.printf("%s%n   unidad: %s%n   festival: %s%n",
					c, c.getUnidad(), c.getUnidad().getFestival().getNombre());

		System.out.printf("%nTotal: %d cajeros%n", cajeros.size());

		System.exit(0);
	}
}

