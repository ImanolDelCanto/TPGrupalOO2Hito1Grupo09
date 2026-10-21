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
import com.unla.epicentrogourmet.dtos.RegistroUnidadDTO;
import com.unla.epicentrogourmet.entities.Festival;
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
			throw new IllegalArgumentException("El código no tiene un formato válido");

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
}