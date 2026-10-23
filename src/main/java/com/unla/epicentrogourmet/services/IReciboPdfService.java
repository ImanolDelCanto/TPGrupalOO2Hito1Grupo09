package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;

public interface IReciboPdfService {

	// Devuelve el PDF ya armado, como bytes. El que decide que hacer con esos bytes es el controller.
	byte[] generar(LiquidacionHaberesDTO liquidacion);
}