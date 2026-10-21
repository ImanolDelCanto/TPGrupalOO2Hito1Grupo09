package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.entities.Plato;

public interface IPedidoService {

	// el menu de la unidad del responsable, para armar el formulario
	List<Plato> platosDeResponsable(String emailResponsable);

	// los pedidos ya cargados de su unidad, para el listado
	List<Pedido> pedidosDeResponsable(String emailResponsable);

	// el alta: los platos elegidos y la cantidad de cada uno
	Pedido altaPedidoPorResponsable(String emailResponsable, List<Long> idsPlatos, List<Integer> cantidades);
}