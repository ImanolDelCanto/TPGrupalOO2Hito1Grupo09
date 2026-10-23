package com.unla.epicentrogourmet.services.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unla.epicentrogourmet.entities.Usuario;
import com.unla.epicentrogourmet.entities.UsuarioRol;
import com.unla.epicentrogourmet.repositories.IUsuarioRepository;

// Es el puente entre NUESTRA tabla usuario y Spring Security: Spring no conoce
// nuestras clases, le devolvemos un User (el de Spring) y el hace el resto.
@Service("usuarioService")
public class UsuarioService implements UserDetailsService {

	private final IUsuarioRepository usuarioRepository;

	public UsuarioService(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	// Lo llama Spring solo, cuando alguien manda el formulario de login
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmailAndFetchRolesEagerly(email);
		if (usuario == null)
			throw new UsernameNotFoundException("No existe el usuario: " + email);
		return buildUser(usuario, buildGrantedAuthorities(usuario.getRoles()));
	}

	private User buildUser(Usuario usuario, List<GrantedAuthority> grantedAuthorities) {
		return new User(usuario.getEmail(), usuario.getPassword(),
				Boolean.TRUE.equals(usuario.getActivo()), // enabled: aca se aplica la baja logica
				true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
				grantedAuthorities);
	}

	// Cada rol del usuario se convierte en una autoridad de Spring.
	// y le agregamos el prefijo ROLE_ aca.
	private List<GrantedAuthority> buildGrantedAuthorities(Set<UsuarioRol> roles) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (UsuarioRol usuarioRol : roles) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + usuarioRol.getRol().name()));
		}
		return new ArrayList<>(grantedAuthorities);
	}
}
