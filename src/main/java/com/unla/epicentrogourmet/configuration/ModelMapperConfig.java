package com.unla.epicentrogourmet.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring inyecta donde haga falta convertir una entidad en DTO o al reves
@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
