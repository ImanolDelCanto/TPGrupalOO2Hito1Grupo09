package com.unla.epicentrogourmet.services.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;

import com.unla.epicentrogourmet.services.IEmailService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.components.UnidadDeVentaFactory;
import com.unla.epicentrogourmet.dtos.ModificarUnidadDTO;
import com.unla.epicentrogourmet.dtos.RegistroUnidadDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.FoodTruck;
import com.unla.epicentrogourmet.entities.PuestoDesarmable;
import com.unla.epicentrogourmet.entities.Rol;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.entities.Usuario;
import com.unla.epicentrogourmet.repositories.IFestivalRepository;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;
import com.unla.epicentrogourmet.services.IUnidadDeVentaService;

@Service("unidadDeVentaService")
public class UnidadDeVentaService implements IUnidadDeVentaService {

	private static final Logger log = LoggerFactory.getLogger(UnidadDeVentaService.class);

	private final IUnidadDeVentaRepository unidadDeVentaRepository;
	private final IUsuarioRepository usuarioRepository;
	private final IFestivalRepository festivalRepository;
	private final PasswordEncoder passwordEncoder;
	private final UnidadDeVentaFactory unidadFactory;
	private final IEmailService emailService;

	public UnidadDeVentaService(IUnidadDeVentaRepository unidadDeVentaRepository,
			IUsuarioRepository usuarioRepository,
			IFestivalRepository festivalRepository,
			PasswordEncoder passwordEncoder,
			UnidadDeVentaFactory unidadFactory,
			IEmailService emailService) {
		this.unidadDeVentaRepository = unidadDeVentaRepository;
		this.usuarioRepository = usuarioRepository;
		this.festivalRepository = festivalRepository;
		this.passwordEncoder = passwordEncoder;
		this.unidadFactory = unidadFactory;
		this.emailService = emailService;
	}

	@Override
	public List<UnidadDeVenta> getAll() {
		return unidadDeVentaRepository.findAll();
	}

	@Override
	public List<UnidadDeVenta> findByFestival(Long idFestival) {
		return unidadDeVentaRepository.findByFestivalIdFestival(idFestival);
	}

	@Override
	public Optional<UnidadDeVenta> findByResponsable(String email) {
		return unidadDeVentaRepository.findByResponsableEmail(email);
	}

	@Override
	@Transactional
	public UnidadDeVenta registrarConResponsable(RegistroUnidadDTO dto) {
		// 1. validaciones que necesitan la base de datos
		if (usuarioRepository.existsByEmail(dto.getEmail()))
			throw new IllegalArgumentException("Ya existe un usuario con ese email");
		if (unidadDeVentaRepository.existsByCodigoUnico(dto.getCodigoUnico()))
			throw new IllegalArgumentException("Ese código ya está en uso por otra unidad");

		Festival festival = festivalRepository.findById(dto.getIdFestival())
				.orElseThrow(() -> new IllegalArgumentException("El festival elegido no existe"));
		if (festival.estaFinalizado())
			throw new IllegalArgumentException("No se puede registrar una unidad en un festival que ya terminó");

		// 2. la unidad: la Factory decide que subclase crear
		UnidadDeVenta unidad = unidadFactory.crear(dto, festival);
		if (!unidad.validarCodigo())
			throw new IllegalArgumentException("El código de un " + unidad.getTipoUnidad() + " tiene que ser "
					+ unidad.getPrefijoCodigo() + " seguido de 7 letras o números (por ejemplo "
					+ unidad.getPrefijoCodigo() + "ABC1234)");

		// 3. el usuario del responsable, con la clave encriptada y su rol
		Usuario responsable = new Usuario(dto.getEmail(), passwordEncoder.encode(dto.getPassword()));
		responsable.agregarRol(Rol.RESPONSABLE);
		responsable.setPrimerIngresoHecho(true);
		usuarioRepository.save(responsable);

		// 4. se vinculan y se guarda la unidad
		unidad.setResponsable(responsable);
		UnidadDeVenta guardada = unidadDeVentaRepository.save(unidad);

		// 5. aviso por mail: si falla, el registro sigue siendo valido
		try {
			emailService.enviarBienvenidaResponsable(responsable.getEmail(), guardada.getNombreComercial());
		} catch (MailException e) {
			log.warn("No se pudo enviar el mail de bienvenida a {}: {}", responsable.getEmail(), e.getMessage());
		}
		return guardada;
	}

		@Override
	@Transactional(readOnly = true)
	public UnidadDeVenta buscarPorId(long idUnidad) {
		return unidadDeVentaRepository.findById(idUnidad)
				.orElseThrow(() -> new IllegalArgumentException("La unidad de venta no existe"));
	}

	// la entidad pasada al DTO, para que el formulario aparezca completo.
	// Igual que en PersonalService.datosDe: instanceof para los datos de cada tipo
	@Override
	@Transactional(readOnly = true)
	public ModificarUnidadDTO datosDe(long idUnidad) {
		UnidadDeVenta unidad = buscarPorId(idUnidad);
		ModificarUnidadDTO dto = new ModificarUnidadDTO();
		dto.setIdUnidad(unidad.getIdUnidad());
		dto.setNombreComercial(unidad.getNombreComercial());
		dto.setCodigoUnico(unidad.getCodigoUnico());
		dto.setSuperficie(unidad.getSuperficie());
		if (unidad instanceof FoodTruck foodTruck) {
			dto.setPatente(foodTruck.getPatente());
			dto.setRequiereElectricidad(foodTruck.isRequiereElectricidad());
		} else if (unidad instanceof PuestoDesarmable puesto) {
			dto.setCantidadCarpas(puesto.getCantidadCarpas());
			dto.setTiempoMontaje(puesto.getTiempoMontaje());
		}
		return dto;
	}

	@Override
	@Transactional
	public UnidadDeVenta modificarUnidad(ModificarUnidadDTO dto) {
		UnidadDeVenta unidad = buscarPorId(dto.getIdUnidad());

		// los datos que cambian el canon: solo si el festival no empezo.
		// Se pregunta antes de tocar, comparando con lo que tiene guardado.
		if (!unidad.puedeCambiarDatosDelCanon() && cambiaElCanon(unidad, dto))
			throw new IllegalArgumentException("El festival ya empezó: la superficie, la electricidad y el montaje "
					+ "no se pueden cambiar porque modificarían el canon ya calculado");

		// el codigo: en mayusculas, sin repetir y con el formato de su tipo.
		// Primero se busca si esta repetido y DESPUES se cambia en la unidad: si se
		// cambia antes, Hibernate lo guarda al hacer la consulta y la base lo rechaza.
		String codigo = dto.getCodigoUnico().trim().toUpperCase();
		unidadDeVentaRepository.findByCodigoUnico(codigo)
				.filter(otra -> otra.getIdUnidad() != unidad.getIdUnidad())
				.ifPresent(otra -> {
					throw new IllegalArgumentException("Ese código ya está en uso por " + otra.getNombreComercial());
				});
		unidad.setCodigoUnico(codigo);
		if (!unidad.validarCodigo())
			throw new IllegalArgumentException("El código de un " + unidad.getTipoUnidad() + " tiene que ser "
					+ unidad.getPrefijoCodigo() + " seguido de 7 letras o números (por ejemplo "
					+ unidad.getPrefijoCodigo() + "ABC1234)");

		// los datos propios de cada tipo (mismas reglas que la Factory del registro)
		if (unidad instanceof FoodTruck foodTruck) {
			if (dto.getPatente() == null || dto.getPatente().isBlank())
				throw new IllegalArgumentException("Un Food Truck necesita patente");
			foodTruck.setPatente(dto.getPatente().trim().toUpperCase());
			foodTruck.setRequiereElectricidad(dto.isRequiereElectricidad());
		} else if (unidad instanceof PuestoDesarmable puesto) {
			if (dto.getCantidadCarpas() == null || dto.getCantidadCarpas() < 1)
				throw new IllegalArgumentException("Un puesto necesita al menos una carpa");
			if (dto.getTiempoMontaje() == null || dto.getTiempoMontaje() < 0)
				throw new IllegalArgumentException("Indicá el tiempo de montaje en minutos");
			puesto.setCantidadCarpas(dto.getCantidadCarpas());
			puesto.setTiempoMontaje(dto.getTiempoMontaje());
		}

		// los datos comunes
		unidad.setNombreComercial(dto.getNombreComercial().trim());
		unidad.setSuperficie(dto.getSuperficie());
		return unidadDeVentaRepository.save(unidad);
	}

	// consulta si el formulario trae otra superficie, otra electricidad u otro montaje
	private boolean cambiaElCanon(UnidadDeVenta unidad, ModificarUnidadDTO dto) {
		if (Double.compare(unidad.getSuperficie(), dto.getSuperficie()) != 0)
			return true;
		if (unidad instanceof FoodTruck foodTruck)
			return foodTruck.isRequiereElectricidad() != dto.isRequiereElectricidad();
		if (unidad instanceof PuestoDesarmable puesto)
			return dto.getTiempoMontaje() == null || puesto.getTiempoMontaje() != dto.getTiempoMontaje();
		return false;
	}
}