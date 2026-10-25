package com.unla.epicentrogourmet.services.implementation;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.components.PersonalFactory;
import com.unla.epicentrogourmet.dtos.AltaEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoDeStaffDTO;
import com.unla.epicentrogourmet.dtos.ModificarEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.TipoPersonal;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.Cocinero;
import com.unla.epicentrogourmet.entities.Personal;
import com.unla.epicentrogourmet.entities.Rol;
import com.unla.epicentrogourmet.entities.RolCocina;
import com.unla.epicentrogourmet.entities.Turno;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;
import com.unla.epicentrogourmet.entities.Usuario;
import com.unla.epicentrogourmet.repositories.ICajeroRepository;
import com.unla.epicentrogourmet.repositories.ICocineroRepository;
import com.unla.epicentrogourmet.repositories.IPersonalRepository;
import com.unla.epicentrogourmet.repositories.IUnidadDeVentaRepository;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;
import com.unla.epicentrogourmet.services.IEmailService;
import com.unla.epicentrogourmet.services.IPersonalService;

// ABM del staff de una unidad de venta, a cargo de su responsable.
@Service("personalService")
public class PersonalService implements IPersonalService {

	private static final String ALFABETO = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
	private static final int LARGO_CLAVE = 10;
	private static final String ASUNTO = "Tu acceso a Epicentro Gourmet";

	private final IPersonalRepository personalRepository;
	private final ICocineroRepository cocineroRepository;
	private final ICajeroRepository cajeroRepository;
	private final IUsuarioRepository usuarioRepository;
	private final IUnidadDeVentaRepository unidadDeVentaRepository;
	private final PersonalFactory personalFactory;
	private final PasswordEncoder passwordEncoder;
	private final IEmailService emailService;

	public PersonalService(IPersonalRepository personalRepository,
			ICocineroRepository cocineroRepository,
			ICajeroRepository cajeroRepository,
			IUsuarioRepository usuarioRepository,
			IUnidadDeVentaRepository unidadDeVentaRepository,
			PersonalFactory personalFactory,
			PasswordEncoder passwordEncoder,
			IEmailService emailService) {
		this.personalRepository = personalRepository;
		this.cocineroRepository = cocineroRepository;
		this.cajeroRepository = cajeroRepository;
		this.usuarioRepository = usuarioRepository;
		this.unidadDeVentaRepository = unidadDeVentaRepository;
		this.personalFactory = personalFactory;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	// ---------- consultas y filtros ----------

	// El listado y la busqueda son el mismo metodo: sin filtros, el apellido
	// viaja vacio y el tipo en null, y trae todo el staff activo.
	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoDeStaffDTO> buscarEnStaff(String emailResponsable, String apellido, TipoPersonal tipo) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		Long idUnidad = unidad.getIdUnidad();
		String texto = apellido == null ? "" : apellido.trim();

		// El filtro por puesto lo resuelve la base: un repositorio de Cocinero
		// trae solo cocineros porque la herencia esta mapeada.
		if (tipo == TipoPersonal.COCINERO)
			return armarFilas(cocineroRepository
					.findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
							idUnidad, texto));

		if (tipo == TipoPersonal.CAJERO)
			return armarFilas(cajeroRepository
					.findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
							idUnidad, texto));

		return armarFilas(personalRepository
				.findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
						idUnidad, texto));
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoDeStaffDTO> bajasDeResponsable(String emailResponsable) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		return armarFilas(personalRepository
				.findByUnidadIdUnidadAndUsuarioActivoFalseOrderByApellidoAsc(unidad.getIdUnidad()));
	}

	// ---------- alta ----------

	// Todo pasa en una sola transaccion: si algo falla, no queda ni el
	// usuario ni el empleado a medio crear.
	@Override
	@Transactional
	public Personal altaEmpleado(String emailResponsable, AltaEmpleadoDTO dto) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);

		if (unidad.getFestival().estaFinalizado())
			throw new IllegalArgumentException(
					"El festival de tu unidad ya termino: no se pueden dar de alta empleados");

		String dni = dto.getDni().trim();
		Optional<Personal> yaCargado = personalRepository.findByDni(dni);

		// Si el DNI ya existe hay dos casos bien distintos
		if (yaCargado.isPresent()) {
			Personal empleado = yaCargado.get();

			if (Boolean.TRUE.equals(empleado.getUsuario().getActivo()))
				throw new IllegalArgumentException("Ya hay un empleado activo con el DNI " + dni);

			// esta dado de baja: se reactiva el que esta, no se crea otro
			return reactivarEmpleado(empleado, unidad, dto);
		}

		return crearEmpleado(unidad, dto);
	}

	private Personal crearEmpleado(UnidadDeVenta unidad, AltaEmpleadoDTO dto) {
		String email = dto.getEmail().trim().toLowerCase();
		if (usuarioRepository.existsByEmail(email))
			throw new IllegalArgumentException("Ya existe un usuario con el email " + email);

		// la Factory decide si es Cocinero o Cajero
		Personal empleado = personalFactory.crear(dto);
		validarEmpleado(empleado);

		// su usuario, con una clave al azar
		String claveEnClaro = generarClave();
		Usuario usuario = new Usuario(email, passwordEncoder.encode(claveEnClaro));
		usuario.agregarRol(Rol.EMPLEADO);
		// queda en false a proposito: marca que todavia usa la clave generada
		usuario.setPrimerIngresoHecho(false);
		usuarioRepository.save(usuario);

		// se vinculan las tres puntas y se guarda
		empleado.setUsuario(usuario);
		unidad.agregarAlStaff(empleado);
		Personal guardado = personalRepository.save(empleado);

		enviarClave(usuario, cuerpoDelMail(guardado, unidad, claveEnClaro, "Te damos de alta en"));
		return guardado;
	}

	// El "metodo de actualizacion especifico" que pide el enunciado: cuando se
	// quiere dar de alta a alguien que ya existe pero esta eliminado, no se
	// agrega otra fila, se actualiza la que ya esta.
	private Personal reactivarEmpleado(Personal empleado, UnidadDeVenta unidad, AltaEmpleadoDTO dto) {
		// Cocinero y Cajero son dos clases distintas, y una fila no cambia
		// de clase: si venia como cocinero no puede volver como cajero.
		if (!coincideElPuesto(empleado, dto))
			throw new IllegalArgumentException("El DNI " + empleado.getDni() + " esta dado de baja como "
					+ describirPuesto(empleado) + ". Para reactivarlo hay que darle el mismo tipo de puesto.");

		// sus datos quedan como los del formulario
		empleado.setNombre(dto.getNombre().trim());
		empleado.setApellido(dto.getApellido().trim());
		empleado.setFechaNacimiento(dto.getFechaNacimiento());
		empleado.setFechaIngreso(dto.getFechaIngreso());
		actualizarPuesto(empleado, dto.getRolCocina(), dto.getTurno());
		validarEmpleado(empleado);

		// vuelve al staff de la unidad del responsable que lo reactiva
		unidad.agregarAlStaff(empleado);

		Usuario usuario = empleado.getUsuario();
		cambiarEmailSiHaceFalta(usuario, dto.getEmail());

		// clave nueva: la vieja quedo de la etapa anterior y el empleado
		// seguramente no la recuerda
		String claveEnClaro = generarClave();
		usuario.setPassword(passwordEncoder.encode(claveEnClaro));
		usuario.setActivo(true);
		usuario.setPrimerIngresoHecho(false);
		usuarioRepository.save(usuario);

		Personal guardado = personalRepository.save(empleado);
		enviarClave(usuario, cuerpoDelMail(guardado, unidad, claveEnClaro, "Te reincorporamos a"));
		return guardado;
	}

	// ---------- modificacion ----------

	// los datos actuales del empleado, para prellenar el formulario
	@Override
	@Transactional(readOnly = true)
	public ModificarEmpleadoDTO datosDe(String emailResponsable, long idPersonal) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		Personal empleado = buscarEnLaUnidad(unidad, idPersonal);

		ModificarEmpleadoDTO dto = new ModificarEmpleadoDTO();
		dto.setIdPersonal(empleado.getIdPersonal());
		dto.setNombre(empleado.getNombre());
		dto.setApellido(empleado.getApellido());
		dto.setDni(empleado.getDni());
		dto.setFechaNacimiento(empleado.getFechaNacimiento());
		dto.setFechaIngreso(empleado.getFechaIngreso());
		dto.setEmail(empleado.getUsuario().getEmail());

		if (empleado instanceof Cocinero cocinero) {
			dto.setTipo(TipoPersonal.COCINERO);
			dto.setRolCocina(cocinero.getRolCocina());
		} else if (empleado instanceof Cajero cajero) {
			dto.setTipo(TipoPersonal.CAJERO);
			dto.setTurno(cajero.getTurno());
		}
		return dto;
	}

	@Override
	@Transactional
	public Personal modificarEmpleado(String emailResponsable, ModificarEmpleadoDTO dto) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		Personal empleado = buscarEnLaUnidad(unidad, dto.getIdPersonal());

		// el DNI puede corregirse, pero no puede chocar con el de otro
		String dni = dto.getDni().trim();
		if (!empleado.getDni().equals(dni)) {
			Optional<Personal> otro = personalRepository.findByDni(dni);
			if (otro.isPresent() && otro.get().getIdPersonal() != empleado.getIdPersonal())
				throw new IllegalArgumentException("Ya hay otro empleado con el DNI " + dni);
			empleado.setDni(dni);
		}

		empleado.setNombre(dto.getNombre().trim());
		empleado.setApellido(dto.getApellido().trim());
		empleado.setFechaNacimiento(dto.getFechaNacimiento());
		empleado.setFechaIngreso(dto.getFechaIngreso());
		// la subclase sale de la base, NO del campo tipo que manda el formulario
		actualizarPuesto(empleado, dto.getRolCocina(), dto.getTurno());
		validarEmpleado(empleado);

		Usuario usuario = empleado.getUsuario();
		if (cambiarEmailSiHaceFalta(usuario, dto.getEmail()))
			usuarioRepository.save(usuario);

		return personalRepository.save(empleado);
	}

	// ---------- baja ----------

	@Override
	@Transactional
	public void bajaEmpleado(String emailResponsable, long idPersonal) {
		UnidadDeVenta unidad = unidadDeResponsable(emailResponsable);
		Personal empleado = buscarEnLaUnidad(unidad, idPersonal);

		// Baja logica: la fila no se borra. Al quedar el usuario en activo =
		// false, UsuarioService lo arma como deshabilitado y Spring Security
		// no lo deja iniciar sesion, aunque la clave sea correcta.
		Usuario usuario = empleado.getUsuario();
		usuario.setActivo(false);
		usuarioRepository.save(usuario);
	}

	// ---------- ayudantes ----------

	// Busca el empleado y ademas chequea que sea de la unidad del responsable.
	// Sin esto, alguien manda el id de otra unidad en la URL y le toca el staff.
	private Personal buscarEnLaUnidad(UnidadDeVenta unidad, long idPersonal) {
		Personal empleado = personalRepository.findById(idPersonal)
				.orElseThrow(() -> new IllegalArgumentException("Ese empleado no existe"));

		if (empleado.getUnidad() == null
				|| empleado.getUnidad().getIdUnidad() != unidad.getIdUnidad())
			throw new IllegalArgumentException("Ese empleado no es de tu unidad");

		return empleado;
	}

	// devuelve true si lo cambio
	private boolean cambiarEmailSiHaceFalta(Usuario usuario, String emailDelFormulario) {
		String email = emailDelFormulario.trim().toLowerCase();
		if (usuario.getEmail().equals(email))
			return false;

		if (usuarioRepository.existsByEmail(email))
			throw new IllegalArgumentException("Ya existe otro usuario con el email " + email);

		usuario.setEmail(email);
		return true;
	}

	private List<EmpleadoDeStaffDTO> armarFilas(List<? extends Personal> empleados) {
		List<EmpleadoDeStaffDTO> filas = new ArrayList<>();
		for (Personal empleado : empleados)
			filas.add(armarFila(empleado));
		return filas;
	}

	private EmpleadoDeStaffDTO armarFila(Personal empleado) {
		EmpleadoDeStaffDTO fila = new EmpleadoDeStaffDTO();
		fila.setIdPersonal(empleado.getIdPersonal());
		fila.setApellidoNombre(empleado.getApellido() + ", " + empleado.getNombre());
		fila.setDni(empleado.getDni());
		fila.setPuesto(describirPuesto(empleado));
		fila.setEmail(empleado.getUsuario().getEmail());
		fila.setFechaIngreso(empleado.getFechaIngreso());
		fila.setAntiguedad(empleado.getAntiguedad());
		fila.setActivo(Boolean.TRUE.equals(empleado.getUsuario().getActivo()));
		return fila;
	}

	// el unico lugar donde se pregunta la subclase, y es para ponerle
	// nombre al puesto. Mismo criterio que LiquidacionService.
	private String describirPuesto(Personal empleado) {
		if (empleado instanceof Cocinero cocinero)
			return "Cocina - " + cocinero.getRolCocina();
		if (empleado instanceof Cajero cajero)
			return "Caja - turno " + cajero.getTurno();
		return "Personal";
	}

	private boolean coincideElPuesto(Personal empleado, AltaEmpleadoDTO dto) {
		if (empleado instanceof Cocinero)
			return dto.getTipo() == TipoPersonal.COCINERO;
		if (empleado instanceof Cajero)
			return dto.getTipo() == TipoPersonal.CAJERO;
		return false;
	}

	// Pone el dato del puesto que corresponda a la subclase real del empleado,
	// y exige que haya venido. Lo usan la reactivacion y la modificacion.
	private void actualizarPuesto(Personal empleado, RolCocina rolCocina, Turno turno) {
		if (empleado instanceof Cocinero cocinero) {
			if (rolCocina == null)
				throw new IllegalArgumentException("Elegi el rol de cocina: cocinero, ayudante o lavaplatos");
			cocinero.setRolCocina(rolCocina);

		} else if (empleado instanceof Cajero cajero) {
			if (turno == null)
				throw new IllegalArgumentException("Elegi el turno del cajero: manana o noche");
			cajero.setTurno(turno);
		}
	}

	// las validaciones del empleado viven en la entidad, no aca
	private void validarEmpleado(Personal empleado) {
		if (!empleado.esMayorDeEdad())
			throw new IllegalArgumentException("El empleado tiene que ser mayor de edad");

		if (empleado.getFechaIngreso().isAfter(LocalDate.now()))
			throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
	}

	private String cuerpoDelMail(Personal empleado, UnidadDeVenta unidad, String clave, String primeraLinea) {
		return "Hola " + empleado.getNombre() + ",\n\n"
				+ primeraLinea + " \"" + unidad.getNombreComercial() + "\", del festival "
				+ unidad.getFestival().getNombre() + ".\n\n"
				+ "Para entrar a la aplicacion:\n"
				+ "  Usuario: " + empleado.getUsuario().getEmail() + "\n"
				+ "  Contrasena: " + clave + "\n\n"
				+ "http://localhost:8080/login\n\n"
				+ "Guardala, es la unica vez que te la mandamos.\n\n"
				+ "Saludos,\nEl equipo de Epicentro Gourmet";
	}

	// Si el mail no sale, la excepcion se propaga y @Transactional revierte
	// TODO: sin el mail el empleado nunca se entera de su contrasena y
	// quedaria un usuario que nadie puede usar.
	private void enviarClave(Usuario usuario, String cuerpo) {
		try {
			emailService.enviar(usuario.getEmail(), ASUNTO, cuerpo);
		} catch (MailException e) {
			throw new IllegalArgumentException("No se pudo enviar el mail a " + usuario.getEmail()
					+ ", asi que no se guardo nada. Revisa la direccion e intenta de nuevo.");
		}
	}

	// SecureRandom y no Random: es una contrasena, no puede ser predecible
	private String generarClave() {
		SecureRandom azar = new SecureRandom();
		StringBuilder clave = new StringBuilder(LARGO_CLAVE);
		for (int i = 0; i < LARGO_CLAVE; i++)
			clave.append(ALFABETO.charAt(azar.nextInt(ALFABETO.length())));
		return clave.toString();
	}

	private UnidadDeVenta unidadDeResponsable(String emailResponsable) {
		return unidadDeVentaRepository.findByResponsableEmail(emailResponsable)
				.orElseThrow(() -> new IllegalArgumentException("No tenes una unidad de venta asignada"));
	}
}