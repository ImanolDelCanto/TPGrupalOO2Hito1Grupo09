package com.unla.epicentrogourmet.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lo que viaja desde el formulario de alta de plato hasta el service.
// La unidad NO viene en el formulario: es la del responsable logueado.
@Getter @Setter @NoArgsConstructor
public class AltaPlatoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre puede tener hasta 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;

    @NotNull(message = "El costo de producción es obligatorio")
    @Positive(message = "El costo de producción debe ser mayor a 0")
    private BigDecimal costoProduccion;
}
