package com.salesianostriana.dam.salonpro.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.repositorios.DatosMaestroRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosMaestroServicio extends BaseServiciosImpl<DatosMaestro, Long, DatosMaestroRepositorio> {

	// Esto solo para crearlo por defecto
	private static final double DESCUENTO_CUMPLE_DEFAULT = 10.0;

	public DatosMaestro obtenerConfiguracion() {
		return repository.findAll()
				.stream()
				.findFirst()
				.orElseGet(() -> repository.save(DatosMaestro.builder()
						.descuentoCumple(DESCUENTO_CUMPLE_DEFAULT)
						.build()));
	}

}
