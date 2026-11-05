package com.unla.epicentrogourmet.services;

import com.unla.epicentrogourmet.dtos.CierreCajaDTO;

public interface ICierreCajaPdfService {

	// Devuelve el comprobante del cierre ya armado, como bytes.
	// Que hacer con esos bytes lo decide el controller.
	byte[] generar(CierreCajaDTO cierre, String emitidoPor);
}