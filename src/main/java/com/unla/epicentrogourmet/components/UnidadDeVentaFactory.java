package com.unla.epicentrogourmet.components;

import org.springframework.stereotype.Component;

import com.unla.epicentrogourmet.dtos.RegistroUnidadDTO;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.FoodTruck;
import com.unla.epicentrogourmet.entities.PuestoDesarmable;
import com.unla.epicentrogourmet.entities.UnidadDeVenta;

// Patron Factory (GoF, creacional): el UNICO lugar del sistema que decide
// que subclase de UnidadDeVenta se crea. Quien la usa recibe una
// UnidadDeVenta y no necesita saber si es FoodTruck o Puesto.
@Component
public class UnidadDeVentaFactory {

    public UnidadDeVenta crear(RegistroUnidadDTO dto, Festival festival) {
        return switch (dto.getTipo()) {
            case FOOD_TRUCK -> crearFoodTruck(dto, festival);
            case PUESTO     -> crearPuesto(dto, festival);
        };
    }

    private FoodTruck crearFoodTruck(RegistroUnidadDTO dto, Festival festival) {
        if (dto.getPatente() == null || dto.getPatente().isBlank())
            throw new IllegalArgumentException("Un Food Truck necesita patente");

        return new FoodTruck(dto.getNombreComercial(), dto.getSuperficie(),
                dto.getCodigoUnico(), festival,
                dto.getPatente().trim().toUpperCase(), dto.isRequiereElectricidad());
    }

    private PuestoDesarmable crearPuesto(RegistroUnidadDTO dto, Festival festival) {
        if (dto.getCantidadCarpas() == null || dto.getCantidadCarpas() <= 0)
            throw new IllegalArgumentException("Un puesto necesita al menos una carpa");
        if (dto.getTiempoMontaje() == null || dto.getTiempoMontaje() < 0)
            throw new IllegalArgumentException("Indicá el tiempo de montaje en minutos");

        return new PuestoDesarmable(dto.getNombreComercial(), dto.getSuperficie(),
                dto.getCodigoUnico(), festival,
                dto.getCantidadCarpas(), dto.getTiempoMontaje());
    }
}
