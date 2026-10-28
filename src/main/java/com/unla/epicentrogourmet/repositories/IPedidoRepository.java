package com.unla.epicentrogourmet.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Pedido;

@Repository("pedidoRepository")
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

	public abstract List<Pedido> findByUnidadIdUnidad(Long idUnidad);

	public abstract List<Pedido> findByUnidadIdUnidadAndFechaBetween(Long idUnidad, LocalDate desde, LocalDate hasta);

	// los pedidos de una unidad, del mas nuevo al mas viejo (para el listado)
	public abstract List<Pedido> findByUnidadIdUnidadOrderByFechaDescIdPedidoDesc(Long idUnidad);

	// todos los pedidos de las unidades de un festival
	public abstract List<Pedido> findByUnidadFestivalIdFestival(Long idFestival);
}
