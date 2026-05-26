package com.salesianostriana.dam.salonpro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Cita;

@Repository
public interface CitaRepositorio extends JpaRepository<Cita, Long> {

	List<Cita> findByClienteId(Long clienteId);

	List<Cita> findAllByOrderByFechaDesc();

}
