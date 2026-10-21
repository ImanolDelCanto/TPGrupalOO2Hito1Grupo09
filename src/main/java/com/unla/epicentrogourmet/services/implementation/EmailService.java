package com.unla.epicentrogourmet.services.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.services.IEmailService;

// UNICA clase del sistema que maneja el envio de mails.
// Los datos de conexion (Gmail, puerto, usuario, clave) los toma Spring
// solo del application.yml, que a su vez los lee de las variables de entorno.
@Service("emailService")
public class EmailService implements IEmailService {

	private final JavaMailSender mailSender;
	private final String remitente;

	public EmailService(JavaMailSender mailSender,
			@Value("${spring.mail.username}") String remitente) {
		this.mailSender = mailSender;
		this.remitente = remitente;
	}

	@Override
	public void enviar(String destinatario, String asunto, String cuerpo) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setFrom("Epicentro Gourmet <" + remitente + ">");
		mensaje.setTo(destinatario);
		mensaje.setSubject(asunto);
		mensaje.setText(cuerpo);
		mailSender.send(mensaje);
	}

	@Override
	public void enviarBienvenidaResponsable(String email, String nombreUnidad) {
		String asunto = "Bienvenido a Epicentro Gourmet";
		String cuerpo = "¡Hola!\n\n"
				+ "Tu unidad de venta \"" + nombreUnidad + "\" quedó registrada correctamente.\n\n"
				+ "Ya podés iniciar sesión con este email (" + email + ") y la contraseña que elegiste:\n"
				+ "http://localhost:8080/login\n\n"
				+ "Saludos,\nEl equipo de Epicentro Gourmet";
		enviar(email, asunto, cuerpo);
	}
}
