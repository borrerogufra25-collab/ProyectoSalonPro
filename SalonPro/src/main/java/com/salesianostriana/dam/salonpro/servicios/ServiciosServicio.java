package com.salesianostriana.dam.salonpro.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ServiciosServicio extends BaseServiciosImpl<Servicio, Long, ServicioRepositorio> {

	private ServicioRepositorio servicioRepo;

}
