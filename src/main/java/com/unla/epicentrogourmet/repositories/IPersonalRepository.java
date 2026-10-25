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

	// El staff activo de una unidad, filtrando por apellido. Con apellido
	// vacio el LIKE '%%' los trae a todos, asi que el listado sin filtros
	// es el mismo metodo: no hacen falta dos consultas.
	// El "UsuarioActivoTrue" es la baja logica: los eliminados no vuelven.
	public abstract List<Personal> findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
			Long idUnidad, String apellido);

	// los dados de baja de esa unidad
	public abstract List<Personal> findByUnidadIdUnidadAndUsuarioActivoFalseOrderByApellidoAsc(Long idUnidad);
}