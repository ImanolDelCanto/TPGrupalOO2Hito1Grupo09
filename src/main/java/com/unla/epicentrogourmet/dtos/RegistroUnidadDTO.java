package com.unla.epicentrogourmet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Viaja desde el formulario de registro hasta el service.
@Getter @Setter @NoArgsConstructor
public class RegistroUnidadDTO {

    // ---- datos del responsable (se convierten en un Usuario) ----
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // ---- datos de la unidad ----
    @NotBlank(message = "El nombre comercial es obligatorio")
    private String nombreComercial;

    @NotBlank(message = "El código es obligatorio")
    @Size(min = 10, max = 10, message = "El código debe tener exactamente 10 caracteres")
    private String codigoUnico;

    @Positive(message = "La superficie debe ser mayor a 0")
    private double superficie;

    @NotNull(message = "Elegí un festival")
    private Long idFestival;

    @NotNull(message = "Elegí el tipo de unidad")
    private TipoUnidad tipo;

    // ---- solo si es Food Truck ----
    private String patente;
    private boolean requiereElectricidad;

    // ---- solo si es Puesto desarmable ----
    private Integer cantidadCarpas;
    private Integer tiempoMontaje;
}
