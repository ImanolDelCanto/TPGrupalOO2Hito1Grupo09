package test;

import java.util.List;

import datos.Personal;
import datos.UnidadDeVenta;
import negocio.UnidadDeVentaABM;

// Caso de uso: cuanto cuesta en sueldos operar cada unidad de venta.
// El total depende del tipo real de cada empleado: el cocinero suma su plus.
public class CasoDeUso_CostoSalarialPorUnidad {
	//IMANOL DEL CANTO
	public static void main(String[] args) {

		UnidadDeVentaABM abm = new UnidadDeVentaABM();
		List<UnidadDeVenta> unidades = abm.traerTodasConStaff();

		System.out.printf("%n=== COSTO SALARIAL MENSUAL POR UNIDAD DE VENTA ===%n");

		UnidadDeVenta laMasCara = null;
		double totalGeneral = 0;

		for (UnidadDeVenta u : unidades) {

			System.out.printf("%n%s%n", u);
			System.out.printf("  responsable: %s%n", u.getResponsable());
			System.out.printf("  staff: %d empleados%n", u.getCantidadDeStaff());

			for (Personal p : u.getStaff())
				System.out.printf("    %s cobra %.2f%n", p, p.getSueldoTotal());

			System.out.printf("  COSTO SALARIAL: %.2f%n", u.getCostoSalarial());

			totalGeneral += u.getCostoSalarial();
			if (laMasCara == null || u.getCostoSalarial() > laMasCara.getCostoSalarial())
				laMasCara = u;
		}

		System.out.printf("%n=== RESUMEN ===%n");
		System.out.printf("Costo salarial total del predio: %.2f%n", totalGeneral);
		System.out.printf("Unidad mas costosa: %s (%.2f)%n",
				laMasCara.getNombreComercial(), laMasCara.getCostoSalarial());

		System.exit(0);
	}
}
