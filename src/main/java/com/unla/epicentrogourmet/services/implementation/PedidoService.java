package com.unla.epicentrogourmet.services.implementation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.CierreCajaDTO;
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

	// limites para cuando el usuario deja una fecha vacia
	private static final LocalDate SIN_LIMITE_ATRAS = LocalDate.of(2000, 1, 1);
	private static final LocalDate SIN_LIMITE_ADELANTE = LocalDate.of(2999, 12, 31);

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
	public Page<Pedido> pedidosDeResponsable(String emailResponsable, LocalDate desde, LocalDate hasta, Pageable pageable) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return pedidosEntreFechas(unidad, desde, hasta, pageable);
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
	public Page<Pedido> pedidosDeCajero(String emailCajero, LocalDate desde, LocalDate hasta, Pageable pageable) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		return pedidosEntreFechas(unidad, desde, hasta, pageable);
	}

	@Override
	@Transactional
	public Pedido altaPedidoPorCajero(String emailCajero, List<Long> idsPlatos, List<Integer> cantidades) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		return crearPedido(unidad, idsPlatos, cantidades);
	}

	@Override
	@Transactional(readOnly = true)
	public CierreCajaDTO cierreDeCaja(String emailCajero) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);
		LocalDate hoy = LocalDate.now();
		List<Pedido> pedidosDeHoy = pedidoRepository.findByUnidadIdUnidadAndFechaBetween(unidad.getIdUnidad(), hoy, hoy);
		BigDecimal total = BigDecimal.ZERO;
		for (Pedido pedido : pedidosDeHoy) {
			total = total.add(pedido.getTotal());
		}
		return new CierreCajaDTO(unidad.getNombreComercial(), hoy, pedidosDeHoy, total, unidad.cajaCerradaHoy());
	}

	@Override
	@Transactional
	public void cerrarCaja(String emailCajero) {
		UnidadDeVenta unidad = unidadDeCajero(emailCajero);   // valida que sea cajero
		unidad.cerrarCaja();                                  // la unidad valida que no este cerrada
		unidadDeVentaRepository.save(unidad);
	}


	//Como se arma un pedido valido para una unidad
	private Pedido crearPedido(UnidadDeVenta unidad, List<Long> idsPlatos, List<Integer> cantidades) {
		if (!unidad.getFestival().estaActivo())
			throw new IllegalArgumentException(
					"El festival de tu unidad no está en curso: solo se cargan pedidos durante el festival");
		if (unidad.cajaCerradaHoy())
			throw new IllegalArgumentException(
					"La caja de hoy ya está cerrada: no se pueden cargar más pedidos hasta mañana");
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

	// El buscador de los dos listados. 
	private Page<Pedido> pedidosEntreFechas(UnidadDeVenta unidad, LocalDate desde, LocalDate hasta, Pageable pageable) {
		LocalDate inicio = desde != null ? desde : SIN_LIMITE_ATRAS;
		LocalDate fin = hasta != null ? hasta : SIN_LIMITE_ADELANTE;
		if (inicio.isAfter(fin))
			throw new IllegalArgumentException("La fecha \"desde\" no puede ser posterior a la fecha \"hasta\"");
		return pedidoRepository.findByUnidadIdUnidadAndFechaBetweenOrderByFechaDescIdPedidoDesc(
				unidad.getIdUnidad(), inicio, fin, pageable);
	}

	private UnidadDeVenta unidadDeResponsable(String emailResponsable) {
		return unidadDeVentaRepository.findByResponsableEmail(emailResponsable)
				.orElseThrow(() -> new IllegalArgumentException("No tenés una unidad de venta asignada"));
	}

	// la unidad del cajero logueado; un cocinero no puede cargar pedidos
	@Override
	@Transactional(readOnly = true)
	public UnidadDeVenta unidadDeCajero(String emailCajero) {
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