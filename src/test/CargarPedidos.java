package test;

import java.time.LocalDate;
import java.util.List;

import datos.ItemPedido;
import datos.Pedido;
import datos.Plato;
import datos.UnidadDeVenta;
import negocio.PedidoABM;
import negocio.PlatoABM;
import negocio.UnidadDeVentaABM;

// requiere haber corrido antes CargarUnidadesYStaff (unidades y su festival ya existentes)
public class CargarPedidos {

	public static void main(String[] args) {

		UnidadDeVentaABM unidadABM = new UnidadDeVentaABM();
		PlatoABM platoABM = new PlatoABM();
		PedidoABM pedidoABM = new PedidoABM();

		List<UnidadDeVenta> unidades = unidadABM.traerTodasConPedidos();
		UnidadDeVenta truck = null;
		UnidadDeVenta puesto = null;
		UnidadDeVenta truck2 = null;
		UnidadDeVenta puesto2 = null;
		for (UnidadDeVenta u : unidades) {
			if (u.getNombreComercial().equals("La Parrilla Rodante")) truck = u;
			if (u.getNombreComercial().equals("Empanadas del Norte")) puesto = u;
			if (u.getNombreComercial().equals("Sushi al Paso")) truck2 = u;
			if (u.getNombreComercial().equals("Cerveza Artesanal")) puesto2 = u;
		}
		if (truck == null || puesto == null || truck2 == null || puesto2 == null) {
			System.out.println("No se encontraron todas las unidades. Corre primero CargarUnidadesYStaff.");
			return;
		}

		// 1) platos, uno por unidad
		Plato milanesa = new Plato( "Milanesa con papas", 6500, 3200, truck);
		platoABM.agregar(milanesa);

		Plato choripan = new Plato( "Choripan", 3800, 1500, truck);
		platoABM.agregar(choripan);

		Plato empanadas = new Plato( "Empanadas x6", 4800, 2100, puesto);
		platoABM.agregar(empanadas);

		Plato alfajor = new Plato( "Alfajor artesanal", 1500, 500, puesto);
		platoABM.agregar(alfajor);

		// 2) pedidos del FoodTruck
		Pedido pedidoTruck1 = new Pedido(LocalDate.of(2026, 1, 15), truck);
		pedidoTruck1.agregarItem(new ItemPedido(3, pedidoTruck1, milanesa));
		pedidoTruck1.agregarItem(new ItemPedido(5, pedidoTruck1, choripan));
		truck.agregarPedido(pedidoTruck1);
		pedidoABM.agregar(pedidoTruck1);

		Pedido pedidoTruck2 = new Pedido(LocalDate.of(2026, 1, 20), truck);
		pedidoTruck2.agregarItem(new ItemPedido(2, pedidoTruck2, milanesa));
		truck.agregarPedido(pedidoTruck2);
		pedidoABM.agregar(pedidoTruck2);

		// 3) pedidos del PuestoDesarmable
		Pedido pedidoPuesto1 = new Pedido(LocalDate.of(2026, 1, 16), puesto);
		pedidoPuesto1.agregarItem(new ItemPedido(10, pedidoPuesto1, empanadas));
		pedidoPuesto1.agregarItem(new ItemPedido(15, pedidoPuesto1, alfajor));
		puesto.agregarPedido(pedidoPuesto1);
		pedidoABM.agregar(pedidoPuesto1);

		// 4) platos de la segunda tanda
		Plato sushi = new Plato( "Bandeja de sushi x20", 9500, 4200, truck2);
		platoABM.agregar(sushi);

		Plato gyoza = new Plato( "Gyozas x8", 4200, 1800, truck2);
		platoABM.agregar(gyoza);

		Plato cervezaIPA = new Plato( "Pinta IPA", 3500, 1200, puesto2);
		platoABM.agregar(cervezaIPA);

		Plato tabla = new Plato( "Tabla de fiambres", 6800, 3000, puesto2);
		platoABM.agregar(tabla);

		// 5) pedidos del segundo FoodTruck
		Pedido pedidoTruck2a = new Pedido(LocalDate.of(2026, 1, 17), truck2);
		pedidoTruck2a.agregarItem(new ItemPedido(4, pedidoTruck2a, sushi));
		pedidoTruck2a.agregarItem(new ItemPedido(6, pedidoTruck2a, gyoza));
		truck2.agregarPedido(pedidoTruck2a);
		pedidoABM.agregar(pedidoTruck2a);

		// 6) pedidos del segundo PuestoDesarmable
		Pedido pedidoPuesto2a = new Pedido(LocalDate.of(2026, 1, 18), puesto2);
		pedidoPuesto2a.agregarItem(new ItemPedido(20, pedidoPuesto2a, cervezaIPA));
		pedidoPuesto2a.agregarItem(new ItemPedido(5, pedidoPuesto2a, tabla));
		puesto2.agregarPedido(pedidoPuesto2a);
		pedidoABM.agregar(pedidoPuesto2a);

		System.out.printf("%nDatos cargados: 8 platos y 5 pedidos%n");
		System.exit(0);
	}
}