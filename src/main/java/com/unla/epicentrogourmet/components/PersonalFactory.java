package com.unla.epicentrogourmet.components;

import org.springframework.stereotype.Component;

import com.unla.epicentrogourmet.dtos.AltaEmpleadoDTO;
import com.unla.epicentrogourmet.dtos.TipoPersonal;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.Cocinero;
import com.unla.epicentrogourmet.entities.Personal;


@Component
public class PersonalFactory {

	public Personal crear(AltaEmpleadoDTO dto) {
		validarDatosDelPuesto(dto);

		return switch (dto.getTipo()) {
			case COCINERO -> new Cocinero(dto.getNombre().trim(), dto.getApellido().trim(),
					dto.getDni().trim(), dto.getFechaNacimiento(), dto.getFechaIngreso(),
					dto.getRolCocina());
			case CAJERO -> new Cajero(dto.getNombre().trim(), dto.getApellido().trim(),
					dto.getDni().trim(), dto.getFechaNacimiento(), dto.getFechaIngreso(),
					dto.getTurno());
		};
	}

	// Esta aparte porque tambien se usa al REACTIVAR un empleado: ahi no se
	// construye ninguno nuevo, pero los datos del puesto tienen que venir igual.
	public void validarDatosDelPuesto(AltaEmpleadoDTO dto) {
		if (dto.getTipo() == TipoPersonal.COCINERO && dto.getRolCocina() == null)
			throw new IllegalArgumentException("Elegi el rol de cocina: cocinero, ayudante o lavaplatos");

		if (dto.getTipo() == TipoPersonal.CAJERO && dto.getTurno() == null)
			throw new IllegalArgumentException("Elegi el turno del cajero: manana o noche");
	}
}