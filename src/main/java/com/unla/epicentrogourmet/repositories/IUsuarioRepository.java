package com.unla.epicentrogourmet.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Usuario;

@Repository("usuarioRepository")
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

	// Igual que findByUsernameAndFetchUserRolesEagerly de la catedra.
	// JOIN FETCH trae los roles en la misma consulta: Spring Security los
	// necesita apenas carga el usuario, y la relacion es LAZY.
	@Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.email = (:email)")
	public abstract Usuario findByEmailAndFetchRolesEagerly(@Param("email") String email);

	public abstract Optional<Usuario> findByEmail(String email);

	public abstract boolean existsByEmail(String email);
}
