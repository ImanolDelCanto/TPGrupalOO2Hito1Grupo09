package com.unla.epicentrogourmet.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unla.epicentrogourmet.dtos.AltaEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.ModificarEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.TipoPersonal;
import com.unla.epicentrogourmet.entities.RolCocina;
import com.unla.epicentrogourmet.entities.Turno;
import com.unla.epicentrogourmet.helpers.ViewRouteHelper;
import com.unla.epicentrogourmet.services.IPersonalService;

import jakarta.validation.Valid;

// El ABM del staff lo hace el responsable, pero vive en su propio
// controller y no dentro de ResponsableController: son dos casos de uso
// distintos y asi cada uno queda en un archivo.
@Controller
@PreAuthorize("hasRole('ROLE_RESPONSABLE')")
@RequestMapping("/empleados")
public class PersonalController {

	private final IPersonalService personalService;

	public PersonalController(IPersonalService personalService) {
		this.personalService = personalService;
	}

	// GET /empleados -> el staff de su unidad.
	// Los dos filtros son opcionales: sin ellos es el listado completo.
	@GetMapping("")
	public ModelAndView lista(Authentication auth,
			@RequestParam(required = false) String apellido,
			@RequestParam(required = false) TipoPersonal tipo) {

		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PERSONAL_LISTA);
		try {
			mAV.addObject("empleados", personalService.buscarEnStaff(auth.getName(), apellido, tipo));
			mAV.addObject("bajas", personalService.bajasDeResponsable(auth.getName()));
		} catch (IllegalArgumentException e) {
			mAV.addObject("error", e.getMessage());
		}
		// para que el formulario de busqueda quede como lo dejo el usuario
		mAV.addObject("apellido", apellido);
		mAV.addObject("tipo", tipo);
		mAV.addObject("tipos", TipoPersonal.values());
		return mAV;
	}

	// GET /empleados/nuevo -> el formulario vacio
	@GetMapping("/nuevo")
	public ModelAndView formulario() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PERSONAL_FORMULARIO);
		mAV.addObject("alta", new AltaEmpleadoDTO());
		agregarOpciones(mAV);
		return mAV;
	}

	// POST /empleados/nuevo -> da de alta, o reactiva si el DNI ya existia
	@PostMapping("/nuevo")
	public ModelAndView alta(Authentication auth,
			@Valid @ModelAttribute("alta") AltaEmpleadoDTO alta,
			BindingResult result,
			RedirectAttributes redirect) {

		// @Valid chequea las anotaciones del DTO y deja los errores aca
		if (!result.hasErrors()) {
			try {
				personalService.altaEmpleado(auth.getName(), alta);
				redirect.addFlashAttribute("exito",
						"Listo. Le mandamos un mail a " + alta.getEmail()
								+ " con su usuario y su contrasena.");
				return new ModelAndView("redirect:" + ViewRouteHelper.PERSONAL_ROOT);
			} catch (IllegalArgumentException e) {
				// error de negocio: DNI repetido, menor de edad, mail que no sale...
				result.reject("alta", e.getMessage());
			}
		}

		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PERSONAL_FORMULARIO);
		agregarOpciones(mAV);
		return mAV;
	}

	// GET /empleados/{id}/editar -> el formulario con los datos actuales
	@GetMapping("/{idPersonal}/editar")
	public ModelAndView formularioDeEdicion(Authentication auth,
			@PathVariable long idPersonal,
			RedirectAttributes redirect) {
		try {
			ModelAndView mAV = new ModelAndView(ViewRouteHelper.PERSONAL_EDITAR);
			mAV.addObject("empleado", personalService.datosDe(auth.getName(), idPersonal));
			agregarOpciones(mAV);
			return mAV;
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("error", e.getMessage());
			return new ModelAndView("redirect:" + ViewRouteHelper.PERSONAL_ROOT);
		}
	}

	// POST /empleados/{id}/editar -> guarda los cambios
	@PostMapping("/{idPersonal}/editar")
	public ModelAndView editar(Authentication auth,
			@Valid @ModelAttribute("empleado") ModificarEmpleadoDTO empleado,
			BindingResult result,
			RedirectAttributes redirect) {

		if (!result.hasErrors()) {
			try {
				personalService.modificarEmpleado(auth.getName(), empleado);
				redirect.addFlashAttribute("exito", "Datos actualizados.");
				return new ModelAndView("redirect:" + ViewRouteHelper.PERSONAL_ROOT);
			} catch (IllegalArgumentException e) {
				result.reject("empleado", e.getMessage());
			}
		}

		ModelAndView mAV = new ModelAndView(ViewRouteHelper.PERSONAL_EDITAR);
		agregarOpciones(mAV);
		return mAV;
	}

	// POST /empleados/{id}/baja -> baja logica
	@PostMapping("/{idPersonal}/baja")
	public ModelAndView baja(Authentication auth,
			@PathVariable long idPersonal,
			RedirectAttributes redirect) {
		try {
			personalService.bajaEmpleado(auth.getName(), idPersonal);
			redirect.addFlashAttribute("exito",
					"Empleado dado de baja. Queda en el listado de bajas y ya no puede iniciar sesion.");
		} catch (IllegalArgumentException e) {
			redirect.addFlashAttribute("error", e.getMessage());
		}
		return new ModelAndView("redirect:" + ViewRouteHelper.PERSONAL_ROOT);
	}

	// las listas de los combos, para no escribirlas en la vista
	private void agregarOpciones(ModelAndView mAV) {
		mAV.addObject("tipos", TipoPersonal.values());
		mAV.addObject("rolesCocina", RolCocina.values());
		mAV.addObject("turnos", Turno.values());
	}
}