package com.salesianostriana.dam.salonpro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesianostriana.dam.salonpro.modelo.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	@Query("SELECT a FROM Alumno a WHERE MONTH(a.fechaNacimiento) = :mes AND DAY(a.fechaNacimiento) = :dia")
	List<Cliente> findByCumple(@Param("mes") int mes, @Param("dia") int dia);
}