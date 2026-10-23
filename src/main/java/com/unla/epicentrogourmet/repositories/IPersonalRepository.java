package com.unla.epicentrogourmet.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Personal;

@Repository("personalRepository")
public interface IPersonalRepository extends JpaRepository<Personal, Long> {

	// el empleado que corresponde al usuario logueado
	public abstract Optional<Personal> findByUsuarioEmail(String email);

	public abstract List<Personal> findAllByUsuarioEmail(String email);

	public abstract Optional<Personal> findByDni(String dni);

	// solo los activos: la baja logica vive en Usuario.activo
	public abstract List<Personal> findByUnidadIdUnidadAndUsuarioActivoTrue(Long idUnidad);
}