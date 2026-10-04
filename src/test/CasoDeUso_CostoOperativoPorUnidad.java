package test;

import java.util.List;

import datos.Festival;
import datos.UnidadDeVenta;
import negocio.FestivalABM;

// Caso de uso: cuanto le cuesta al festival tener cada unidad en el predio.
// El monto depende del tipo real de la unidad: el food truck paga el plus de
// electricidad y el puesto paga por su tiempo de montaje.
public class CasoDeUso_CostoOperativoPorUnidad {
	//IMANOL DEL CANTO
	
	
	public static void main(String[] args) {

		FestivalABM abm = new FestivalABM();
		List<Festival> festivales = abm.traerTodosConUnidades();

		System.out.printf("%n=== COSTO OPERATIVO POR UNIDAD DE VENTA ===%n");

		for (Festival f : festivales) {

			System.out.printf("%n%s%n", f.getNombre());
			System.out.printf("  superficie: %.2f por m2   montaje: %.2f por minuto   electricidad: %.2f%n",
					f.getCostoPorSuperficie(), f.getCostoPorMontaje(), f.getPlusElectricidad());

			UnidadDeVenta laMasCara = null;
			double total = 0;

			for (UnidadDeVenta u : f.getUnidades()) {
				System.out.printf("    %-24s %10.2f m2 %14.2f%n",
						u.getNombreComercial(), u.getSuperficie(), u.getCostoOperativo());
				total += u.getCostoOperativo();
				if (laMasCara == null || u.getCostoOperativo() > laMasCara.getCostoOperativo())
					laMasCara = u;
			}

			System.out.printf("  COSTO OPERATIVO DEL FESTIVAL: %.2f%n", total);
			if (laMasCara != null)
				System.out.printf("  La mas cara de operar: %s (%.2f)%n",
						laMasCara.getNombreComercial(), laMasCara.getCostoOperativo());
		}

		System.exit(0);
	}
}
