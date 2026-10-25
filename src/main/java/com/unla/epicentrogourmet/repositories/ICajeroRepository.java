package com.unla.epicentrogourmet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Cajero;

// Igual que ICocineroRepository pero para los cajeros.
@Repository("cajeroRepository")
public interface ICajeroRepository extends JpaRepository<Cajero, Long> {

	public abstract List<Cajero> findByUnidadIdUnidadAndUsuarioActivoTrueAndApellidoContainingIgnoreCaseOrderByApellidoAsc(
			Long idUnidad, String apellido);
}