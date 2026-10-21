package com.unla.epicentrogourmet.services;

public interface IEmailService {

	// el "motor": manda cualquier mail de texto
	void enviar(String destinatario, String asunto, String cuerpo);

	// los mails concretos del sistema
	void enviarBienvenidaResponsable(String email, String nombreUnidad);
}