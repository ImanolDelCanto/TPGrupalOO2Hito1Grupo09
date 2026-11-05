package com.unla.epicentrogourmet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lo que llega del formulario de edicion de una unidad (lo usa el admin). No persiste.
// No tiene tipo, festival ni responsable: eso no se cambia, una fila no cambia de clase.
@Getter @Setter @NoArgsConstructor
public class ModificarUnidadDTO {

	// de que unidad se trata: lo pone el controller con el id de la direccion
	private long idUnidad;

	@NotBlank(message = "El nombre comercial es obligatorio")
	private String nombreComercial;

	@NotBlank(message = "El código es obligatorio")
	@Size(min = 10, max = 10, message = "El código debe tener exactamente 10 caracteres")
	private String codigoUnico;

	@Positive(message = "La superficie debe ser mayor a 0")
	private double superficie;

	// solo Food Truck
	private String patente;
	private boolean requiereElectricidad;

	// solo Puesto
	private Integer cantidadCarpas;
	private Integer tiempoMontaje;
}