package test;

import java.util.List;

import datos.Personal;
import datos.PuestoDesarmable;
import negocio.UnidadDeVentaABM;

// Caso de uso: que puestos de un festival se pueden montar dentro de un tiempo
// dado. Sirve para planificar el armado del predio.
//
// La consulta cruza puestoDesarmable + unidadDeVenta + festival + personal
// (y cocinero/cajero por la herencia) en un solo HQL: el filtro por festival y
// por tiempo de montaje lo resuelve la base, no un for en Java.
public class CasoDeUso_MontajePorFestival {

	public static void main(String[] args) {

		UnidadDeVentaABM abm = new UnidadDeVentaABM();

		String festival = "Sabores de Verano";
		int minutos = 60;

		List<PuestoDesarmable> puestos = abm.traerPuestosPorTiempoDeMontaje(festival, minutos);

		System.out.printf("%n=== PUESTOS DE '%s' QUE SE MONTAN EN %d MINUTOS O MENOS ===%n%n",
				festival, minutos);

		if (puestos.isEmpty())
			System.out.printf("Ningun puesto cumple la condicion.%n");

		int totalMontaje = 0;

		for (PuestoDesarmable p : puestos) {
			System.out.printf("%s%n", p);
			System.out.printf("   montaje: %d min   carpas: %d   staff: %d empleados%n",
					p.getTiempoMontaje(), p.getCantidadCarpas(), p.getCantidadDeStaff());
			for (Personal e : p.getStaff())
				System.out.printf("      %s%n", e);
			totalMontaje += p.getTiempoMontaje();
		}

		System.out.printf("%nPuestos que entran: %d   -   Tiempo total de montaje: %d min%n",
				puestos.size(), totalMontaje);

		System.exit(0);
	}
}
