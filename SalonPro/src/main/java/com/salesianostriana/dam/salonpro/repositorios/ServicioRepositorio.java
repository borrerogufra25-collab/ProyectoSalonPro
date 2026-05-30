package com.salesianostriana.dam.salonpro.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Servicio;

@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, Long> {

	@Query("""
			SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END
			FROM CitaServicio cs
			WHERE cs.servicio.id = :servicioId
			""")
	boolean existsCitaByServicioId(@Param("servicioId") Long servicioId);

}
