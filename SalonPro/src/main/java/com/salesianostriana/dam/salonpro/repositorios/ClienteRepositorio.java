package com.salesianostriana.dam.salonpro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	@Query("SELECT c FROM Cliente c WHERE MONTH(c.cumple) = :mes AND DAY(c.cumple) = :dia")
	List<Cliente> findByCumple(@Param("mes") int mes, @Param("dia") int dia);
}