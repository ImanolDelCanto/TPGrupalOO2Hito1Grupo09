package com.unla.epicentrogourmet.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.epicentrogourmet.entities.Festival;

@Repository("festivalRepository")
public interface IFestivalRepository extends JpaRepository<Festival, Long> {

	public abstract List<Festival> findAllByOrderByFechaInicioDesc();

	// los festivales que estan ocurriendo en una fecha
	public abstract List<Festival> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fecha1, LocalDate fecha2);
}
