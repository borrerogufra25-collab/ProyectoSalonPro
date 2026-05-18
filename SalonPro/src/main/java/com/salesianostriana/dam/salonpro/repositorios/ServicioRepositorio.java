package com.salesianostriana.dam.salonpro.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Servicio;

@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, Long> {

}
