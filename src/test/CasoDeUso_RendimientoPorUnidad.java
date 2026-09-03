package test;

import java.util.List;

import datos.UnidadDeVenta;
import negocio.UnidadDeVentaABM;

// Caso de uso: rendimiento economico (facturacion y margen) de cada unidad de venta.
// Herencia: el detalle mostrado depende del tipo real de la unidad (FoodTruck o PuestoDesarmable).
// Uno a muchos: UnidadDeVenta -> Pedido -> ItemPedido.
public class CasoDeUso_RendimientoPorUnidad {

    public static void main(String[] args) {

        UnidadDeVentaABM abm = new UnidadDeVentaABM();
        List<UnidadDeVenta> unidades = abm.traerTodasConPedidos();

        System.out.printf("%n=== RENDIMIENTO ECONOMICO POR UNIDAD DE VENTA ===%n");

        UnidadDeVenta laDeMayorMargen = null;
        double facturacionGeneral = 0;
        double margenGeneral = 0;

        for (UnidadDeVenta u : unidades) {

            System.out.printf("%n%s%n", u);
            System.out.printf("  tipo: %s (%s)%n", u.getClass().getSimpleName(), u.getDetalleEspecifico());
            System.out.printf("  cantidad de pedidos: %d%n", u.getCantidadDePedidos());
            System.out.printf("  FACTURACION: %.2f%n", u.getFacturacionTotal());
            System.out.printf("  MARGEN: %.2f%n", u.getMargenTotal());

            facturacionGeneral += u.getFacturacionTotal();
            margenGeneral += u.getMargenTotal();

            if (laDeMayorMargen == null || u.getMargenTotal() > laDeMayorMargen.getMargenTotal())
                laDeMayorMargen = u;
        }

        System.out.printf("%n=== RESUMEN DEL PREDIO ===%n");
        System.out.printf("Facturacion total: %.2f%n", facturacionGeneral);
        System.out.printf("Margen total: %.2f%n", margenGeneral);
        if (laDeMayorMargen != null) {
            System.out.printf("Unidad mas rentable: %s (%s) con margen %.2f%n",
                    laDeMayorMargen.getNombreComercial(),
                    laDeMayorMargen.getClass().getSimpleName(),
                    laDeMayorMargen.getMargenTotal());
        }

        System.exit(0);
    }
}
