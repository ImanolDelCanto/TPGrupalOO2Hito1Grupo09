package test;

import java.util.List;

import negocio.UnidadDeVentaABM;

// CASO DE USO: Rendimiento economico por unidad de venta en cierto festival(nombre, tipo,
// cantidad de pedidos, facturacion total y margen), calculado con
// agregacion HQL (count/sum/group by) directamente en el DAO.
// cada fila del resultado es un Object[] que se castea aca.

public class CasoDeUso_RendimientoEconomicoPorUnidadHQL {
	// ERIKA BAEZ
	public static void main(String[] args) {

		String festival = "Sabores de Verano"; 

		UnidadDeVentaABM abm = new UnidadDeVentaABM();
		List<Object[]> rendimientos = abm.obtenerRendimientoEconomicoPorUnidad(festival);

		System.out.printf("%n====== RENDIMIENTO ECONOMICO POR UNIDAD DE VENTA ======%n");
		System.out.printf("%n========== FESTIVAL: %s ==========%n", festival);
		String nombreDeMayorMargen = null;
		String tipoDeMayorMargen = null;
		double mayorMargen = 0;
		double facturacionGeneral = 0;
		double margenGeneral = 0;

		for (Object[] fila : rendimientos) {

			// el orden de las columnas es el mismo que el del select del HQL
		    String nombreComercial = (String) fila[0];
		    //String nombreFestival = (String) fila[1];
		    String tipoUnidad = (String) fila[2];
		    long cantidadPedidos = (Long) fila[3];
		    double facturacionTotal = (Double) fila[4];
		    double margenTotal = (Double) fila[5];

		    System.out.printf("%n%s%n", nombreComercial);
		    System.out.printf("  Tipo: %s%n", tipoUnidad);
		    System.out.printf("  Cantidad de pedidos: %d%n", cantidadPedidos);
		    System.out.printf("  FACTURACION: %.2f%n", facturacionTotal);
		    System.out.printf("  MARGEN DE GANANCIA: %.2f%n", margenTotal);
		    
			System.out.printf("%n---------------------------------%n");

		    facturacionGeneral += facturacionTotal;
		    margenGeneral += margenTotal;

		    if (nombreDeMayorMargen == null || margenTotal > mayorMargen) {
		        nombreDeMayorMargen = nombreComercial;
		        tipoDeMayorMargen = tipoUnidad;
		        mayorMargen = margenTotal;
		    }
		}

		System.out.printf("%n========= SINTESIS DEL FESTIVAL %s =========%n", festival);
		System.out.printf("Facturacion total: %.2f%n", facturacionGeneral);
		System.out.printf("Margen total de ganancia: %.2f%n", margenGeneral);
		if (nombreDeMayorMargen != null) {
			System.out.printf("Unidad mas rentable: %s (%s) con margen de %.2f%n",
					nombreDeMayorMargen, tipoDeMayorMargen, mayorMargen);
		}

		System.exit(0);
	}
}