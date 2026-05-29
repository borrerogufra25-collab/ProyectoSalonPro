package com.salesianostriana.dam.salonpro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Cita;

@Repository
public interface CitaRepositorio extends JpaRepository<Cita, Long> {

	@Query("""
			SELECT DISTINCT c
			FROM Cita c
			LEFT JOIN FETCH c.citaServicios cs
			LEFT JOIN FETCH cs.servicio
			WHERE c.cliente.id = :clienteId
			ORDER BY c.fecha DESC
			""")
	List<Cita> findByClienteId(@Param("clienteId") Long clienteId);

	List<Cita> findAllByOrderByFechaDesc();

}
