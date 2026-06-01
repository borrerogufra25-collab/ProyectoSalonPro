package com.salesianostriana.dam.salonpro.repositorios;

import java.time.LocalDateTime;
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
			LEFT JOIN c.citaServicios cs
			LEFT JOIN cs.servicio
			WHERE c.cliente.id = :clienteId
			ORDER BY c.fecha DESC
			""")
	List<Cita> findByClienteId(@Param("clienteId") Long clienteId);

	@Query("""
			SELECT DISTINCT c
			FROM Cita c
			LEFT JOIN c.cliente
			LEFT JOIN c.citaServicios cs
			LEFT JOIN cs.servicio
			WHERE c.fecha >= :fecha
			ORDER BY c.fecha ASC
			""")
	List<Cita> findTop5ByFechaGreaterThanEqualOrderByFechaAsc(@Param("fecha") LocalDateTime fecha);

	@Query("""
			SELECT DISTINCT c
			FROM Cita c
			LEFT JOIN c.cliente
			JOIN c.citaServicios cs
			WHERE cs.servicio.id = :servicioId
			""")
	List<Cita> findByServicioId(@Param("servicioId") Long servicioId);

	@Query("""
			SELECT s.id, s.nombre, COUNT(cs), COALESCE(SUM(s.precio), 0.0)
			FROM CitaServicio cs
			JOIN cs.servicio s
			GROUP BY s.id, s.nombre
			ORDER BY COUNT(cs) DESC, s.nombre ASC
			""")
	List<Object[]> findServiciosPopulares();

	@Query("""
			SELECT cl.id, cl.nombre, cl.email, COUNT(c)
			FROM Cita c
			JOIN c.cliente cl
			GROUP BY cl.id, cl.nombre, cl.email
			ORDER BY COUNT(c) DESC, cl.nombre ASC
			""")
	List<Object[]> findClientesConMasVisitas();

}
