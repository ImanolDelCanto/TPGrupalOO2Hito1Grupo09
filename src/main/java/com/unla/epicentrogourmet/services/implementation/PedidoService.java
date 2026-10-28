package com.unla.epicentrogourmet.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.Pedido;
import com.unla.epicentrogourmet.entities.Personal;
import com.unla.epicentrogourmet.entities.Plato;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.repositories.IPedidoRepository;
import com.unla.epicentrogourmet.repositories.IPersonalRepository;
import com.unla.epicentrogourmet.repositories.IPlatoRepository;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.services.IPedidoService;

@Service("pedidoService")
public class PedidoService implements IPedidoService {

	private final IPedidoRepository pedidoRepository;
	private final IPlatoRepository platoRepository;
	private final IUnidadDeVentaRepository unidadDeVentaRepository;
	private final IPersonalRepository personalRepository;

	public PedidoService(IPedidoRepository pedidoRepository,
			IPlatoRepository platoRepository,
			IUnidadDeVentaRepository unidadDeVentaRepository,
			IPersonalRepository personalRepository) {
		this.pedidoRepository = pedidoRepository;
		this.platoRepository = platoRepository;
		this.unidadDeVentaRepository = unidadDeVentaRepository;
		this.personalRepository = personalRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Plato> platosDeResponsable(String emailResponsable) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return platoRepository.findByUnidadIdUnidadOrderByNombreAsc(unidad.getIdUnidad());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Pedido> pedidosDeResponsable(String emailResponsable) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return pedidoRepository.findByUnidadIdUnidadOrderByFechaDescIdPedidoDesc(unidad.getIdUnidad());
	}

	//Quien carga el pedido
	@Override
	@Transactional
	public Pedido altaPedidoPorResponsable(String emailResponsable, List<Long> idsPlatos, List<Integer> cantidades) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return crearPedido(unidad, idsPlatos, cantidades);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Plato> platosDeCajero(String emailCajero) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		return platoRepository.findByUnidadIdUnidadOrderByNombreAsc(unidad.getIdUnidad());
	}

	@Override
	@Transactional(readOnly = true)
	public List<Pedido> pedidosDeCajero(String emailCajero) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		return pedidoRepository.findByUnidadIdUnidadOrderByFechaDescIdPedidoDesc(unidad.getIdUnidad());
	}

	@Override
	@Transactional
	public Pedido altaPedidoPorCajero(String emailCajero, List<Long> idsPlatos, List<Integer> cantidades) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		return crearPedido(unidad, idsPlatos, cantidades);
	}

	//Como se arma un pedido valido para una unidad
	private Pedido crearPedido(UnidadDeVenta unidad, List<Long> idsPlatos, List<Integer> cantidades) {
		if (!unidad.getFestival().estaActivo())
			throw new IllegalArgumentException(
					"El festival de tu unidad no está en curso: solo se cargan pedidos durante el festival");
		if (idsPlatos == null || cantidades == null || idsPlatos.size() != cantidades.size())
			throw new IllegalArgumentException("Elegí al menos un plato");

		Pedido pedido = new Pedido(unidad);   // la fecha la pone el pedido: hoy

		for (int i = 0; i < idsPlatos.size(); i++) {
			Long idPlato = idsPlatos.get(i);
			Integer cantidad = cantidades.get(i);
			if (idPlato == null || cantidad == null || cantidad == 0)
				continue;   // ese plato no se pidio

			Plato plato = platoRepository.findById(idPlato)
					.orElseThrow(() -> new IllegalArgumentException("Uno de los platos elegidos no existe"));
			pedido.agregarItem(plato, cantidad);   // valida cantidad y que sea de la unidad
		}

		if (!pedido.tieneItems())
			throw new IllegalArgumentException("Elegí al menos un plato con cantidad mayor a 0");

		return pedidoRepository.save(pedido);   // cascade ALL: guarda tambien los items
	}

	private UnidadDeVenta unidadDeResponsable(String emailResponsable) {
		return unidadDeVentaRepository.findByResponsableEmail(emailResponsable)
				.orElseThrow(() -> new IllegalArgumentException("No tenés una unidad de venta asignada"));
	}

	// la unidad del cajero logueado; un cocinero no puede cargar pedidos
	private UnidadDeVenta unidadDeCajero(String emailCajero) {
		Personal empleado = personalRepository.findByUsuarioEmail(emailCajero)
				.orElseThrow(() -> new IllegalArgumentException("No hay un empleado asociado a tu usuario"));
		if (!(empleado instanceof Cajero))
			throw new IllegalArgumentException("Solo los cajeros pueden cargar pedidos y cerrar la caja");
		if (empleado.getUnidad() == null)
			throw new IllegalArgumentException("No tenés una unidad de venta asignada");
		return empleado.getUnidad();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean esCajero(String email) {
		Optional<Personal> empleado = personalRepository.findByUsuarioEmail(email);
		return empleado.isPresent() && empleado.get() instanceof Cajero;
	}
}