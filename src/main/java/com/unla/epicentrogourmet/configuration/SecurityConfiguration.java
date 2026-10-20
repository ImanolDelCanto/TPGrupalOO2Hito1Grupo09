package com.unla.epicentrogourmet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.unla.epicentrogourmet.services.implementation.UsuarioService;

// Misma estructura que la SecurityConfiguration del repo de la catedra.
// Diferencia: la pagina de inicio y el registro son publicos, porque el
// enunciado dice que se ven sin iniciar sesion.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // habilita @PreAuthorize arriba de los controllers
public class SecurityConfiguration {

	private final UsuarioService usuarioService;

	public SecurityConfiguration(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				// CSRF deshabilitado como en el repo de la catedra
				.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> {
					// publico: inicio, registro de unidad, recursos estaticos y paginas de error
					auth.requestMatchers("/", "/index", "/registro/**", "/error/**", "/error",
							"/css/**", "/imgs/**", "/js/**", "/vendor/**").permitAll();
					// todo lo demas pide estar logueado. QUE rol hace falta para cada
					// zona lo dice el @PreAuthorize de cada controller.
					auth.anyRequest().authenticated();
				})
				.formLogin(login -> {
					login.loginPage("/login");
					login.loginProcessingUrl("/loginprocess");
					login.usernameParameter("username");
					login.passwordParameter("password");
					login.defaultSuccessUrl("/loginsuccess");
					login.permitAll();
				})
				.logout(logout -> {
					logout.logoutUrl("/logout");
					logout.logoutSuccessUrl("/login?logout");
					logout.permitAll();
				})
				.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// El que autentica: busca el usuario con nuestro UsuarioService y compara
	// la clave con BCrypt
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(usuarioService);
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
