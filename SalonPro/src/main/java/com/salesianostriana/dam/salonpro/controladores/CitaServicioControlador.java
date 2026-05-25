package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;

import com.salesianostriana.dam.salonpro.servicios.CitaServicioServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CitaServicioControlador {

	private final CitaServicioServicio citaServicioServicio;

}
