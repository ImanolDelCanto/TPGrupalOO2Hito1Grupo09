package test;

import java.util.List;

import datos.Cajero;
import datos.Festival;
import negocio.FestivalABM;
import negocio.PersonalABM;

public class CasoDeUso_CajerosAntiguosFoodTruck {

	public static void main(String[] args) {

		PersonalABM abm = new PersonalABM();
		FestivalABM festivalABM = new FestivalABM();

		String turno = "noche";
		int antiguedadMinima = 2;

		// buscamos el festival real ya cargado en la base, por nombre
		Festival festival = buscarFestivalPorNombre(festivalABM, "Sabores de Verano");

		if (festival == null) {
			System.out.printf("No se encontro el festival indicado.%n");
			System.exit(1);
		}

		List<Cajero> cajeros = abm.traerCajerosDeFoodTruckEnFestival(turno, antiguedadMinima, festival);

		System.out.printf("%n=== CAJEROS DE TURNO %s, MAS DE %d ANIOS, EN FOOD TRUCKS DEL FESTIVAL %s ===%n%n",
				turno.toUpperCase(), antiguedadMinima, festival.getNombre());

		if (cajeros.isEmpty())
			System.out.printf("Ningun cajero cumple la condicion.%n");

		for (Cajero c : cajeros)
			System.out.printf("%s%n   unidad: %s%n", c, c.getUnidad());

		System.out.printf("%nTotal: %d cajeros%n", cajeros.size());

		System.exit(0);
	}

	// busca un festival por nombre entre todos los cargados; devuelve null si no lo encuentra
	private static Festival buscarFestivalPorNombre(FestivalABM festivalABM, String nombre) {
		for (Festival f : festivalABM.traerTodos())
			if (f.getNombre().equalsIgnoreCase(nombre))
				return f;
		return null;
	}
}

