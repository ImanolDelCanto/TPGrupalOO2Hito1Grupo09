package com.unla.epicentrogourmet.services.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;
import com.unla.epicentrogourmet.entities.Cajero;
import com.unla.epicentrogourmet.entities.Cocinero;
import com.unla.epicentrogourmet.entities.Festival;
import com.unla.epicentrogourmet.entities.Personal;
import com.unla.epicentrogourmet.entities.TipoCosto;
import com.unla.epicentrogourmet.repositories.IPersonalRepository;
import com.unla.epicentrogourmet.services.ILiquidacionService;

@Service("liquidacionService")
public class LiquidacionService implements ILiquidacionService {

	private static final BigDecimal MEDIO = new BigDecimal("0.50");
	private static final int DIAS_POR_TRAMO = 15;

	private final IPersonalRepository personalRepository;

	public LiquidacionService(IPersonalRepository personalRepository) {
		this.personalRepository = personalRepository;
	}

	// readOnly = true: transaccion de solo lectura. Hace falta porque getUnidad(),
	// getFestival() y getCostos() son LAZY: sin transaccion abierta, tocarlos
	// tira LazyInitializationException.
	@Override
	@Transactional(readOnly = true)
	public LiquidacionHaberesDTO liquidarPorEmail(String email) {
		Personal p = personalRepository.findByUsuarioEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("No hay un empleado asociado a " + email));
		return liquidar(p);
	}

	@Override
	@Transactional(readOnly = true)
	public LiquidacionHaberesDTO liquidarPorIdPersonal(Long idPersonal) {
		Personal p = personalRepository.findById(idPersonal)
				.orElseThrow(() -> new IllegalArgumentException("No existe el empleado " + idPersonal));
		return liquidar(p);
	}

	private LiquidacionHaberesDTO liquidar(Personal p) {
		if (p.getUnidad() == null)
			throw new IllegalArgumentException("El empleado no esta asignado a ninguna unidad de venta");

		Festival f = p.getUnidad().getFestival();
		int dias = f.getDiasDuracion();

		// (int(dias / 15) + 1) * 50%  -> el factor de la formula del enunciado
		BigDecimal factor = BigDecimal.valueOf((dias / DIAS_POR_TRAMO) + 1).multiply(MEDIO);

		// los montos salen de los Costo del festival, no estan escritos en el codigo
		BigDecimal sueldoBase = f.getValorCosto(TipoCosto.SUELDO_BASE);

		// getPlus() es polimorfico: el cocinero devuelve su plus por categoria,
		// el cajero lo que le corresponde por antiguedad. Aca no se pregunta el tipo.
		BigDecimal adicional = p.getPlus(f);

		BigDecimal total = factor.multiply(sueldoBase.add(adicional)).setScale(2, RoundingMode.HALF_UP);

		LiquidacionHaberesDTO dto = new LiquidacionHaberesDTO();
		dto.setPersonal(p.getApellido() + ", " + p.getNombre());
		dto.setFestival(f.getNombre());
		dto.setDiasFestival(dias);
		dto.setSueldoBase(sueldoBase);
		dto.setTotalACobrar(total);

		// el unico lugar que pregunta el tipo, y es para ponerle nombre
		// al concepto del recibo, no para calcular
		if (p instanceof Cocinero c) {
			dto.setRol("Cocina - " + c.getRolCocina());
			dto.setPlus(adicional);
			dto.setAntiguedad(BigDecimal.ZERO);
		} else if (p instanceof Cajero c) {
			dto.setRol("Caja - turno " + c.getTurno());
			dto.setPlus(BigDecimal.ZERO);
			dto.setAntiguedad(adicional);
		} else {
			dto.setRol("Personal");
			dto.setPlus(adicional);
			dto.setAntiguedad(BigDecimal.ZERO);
		}
		return dto;
	}
}
