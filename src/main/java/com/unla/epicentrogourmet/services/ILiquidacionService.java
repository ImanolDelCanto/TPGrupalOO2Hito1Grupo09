package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.LiquidacionHaberesDTO;

public interface ILiquidacionService {

	LiquidacionHaberesDTO liquidarPorEmail(String email);

	LiquidacionHaberesDTO liquidarPorIdPersonal(Long idPersonal);
}
