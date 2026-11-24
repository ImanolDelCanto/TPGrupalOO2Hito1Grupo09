package com.unla.epicentrogourmet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

// La portada de Swagger: el titulo y la descripcion que se ven arriba de todo
@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI epicentroOpenApi() {
		return new OpenAPI().info(new Info()
				.title("Epicentro Gourmet - API REST")
				.version("v1")
				.description("Hito 3 de Orientación a Objetos II - Grupo 09"));
	}
}