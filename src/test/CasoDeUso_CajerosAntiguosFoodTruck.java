package test;
import java.util.List;

import datos.Cajero;
import negocio.PersonalABM;

public class CasoDeUso_CajerosAntiguosFoodTruck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//ENZO DIAZ

				PersonalABM abm = new PersonalABM();

				String turno = "noche";
				int antiguedadMinima = 2;

				List<Cajero> cajeros = abm.traerCajerosDeFoodTruck(turno, antiguedadMinima);

				System.out.printf("%n=== CAJEROS DE TURNO %s CON MAS DE %d ANIOS, EN FOOD TRUCKS ===%n%n",
						turno.toUpperCase(), antiguedadMinima);

				if (cajeros.isEmpty())
					System.out.printf("Ningun cajero cumple la condicion.%n");

				for (Cajero c : cajeros)
					System.out.printf("%s%n   unidad: %s%n", c, c.getUnidad());

				System.out.printf("%nTotal: %d cajeros%n", cajeros.size());

				System.exit(0);
			}
		
	}

