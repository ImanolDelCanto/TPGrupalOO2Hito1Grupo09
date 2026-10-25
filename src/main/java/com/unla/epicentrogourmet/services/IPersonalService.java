package com.unla.epicentrogourmet.services;

import java.util.List;

import com.unla.epicentrogourmet.dtos.AltaEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.EmpleadoDeStaffDTO;
import com.unla.epicentrogourmet.dtos.ModificarEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.TipoPersonal;
import com.unla.epicentrogourmet.entities.Personal;

public interface IPersonalService {

	// El staff activo de la unidad del responsable, filtrado por apellido y
	// por puesto. Sin filtros (apellido vacio y tipo en null) es el listado
	// completo: es el mismo metodo.
	List<EmpleadoDeStaffDTO> buscarEnStaff(String emailResponsable, String apellido, TipoPersonal tipo);

	// los que estan dados de baja en esa misma unidad
	List<EmpleadoDeStaffDTO> bajasDeResponsable(String emailResponsable);

	// Da de alta un empleado en la unidad del responsable. Si el DNI ya existe
	// dado de baja, NO crea otro: reactiva el que estaba.
	Personal altaEmpleado(String emailResponsable, AltaEmpleadoDTO dto);

	// los datos actuales de un empleado, para prellenar el formulario
	ModificarEmpleadoDTO datosDe(String emailResponsable, long idPersonal);

	Personal modificarEmpleado(String emailResponsable, ModificarEmpleadoDTO dto);

	// baja logica: la fila queda, el usuario deja de poder entrar
	void bajaEmpleado(String emailResponsable, long idPersonal);
}