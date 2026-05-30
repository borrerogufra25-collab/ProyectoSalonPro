package com.salesianostriana.dam.salonpro.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.repositorios.DatosMaestroRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosMaestroServicio extends BaseServiciosImpl<DatosMaestro, Long, DatosMaestroRepositorio> {

	private static final double DESCUENTO_CUMPLE_DEFAULT = 10.0;
	private static final int CORTES_POR_PUNTO_DEFAULT = 10;
	private static final int PUNTOS_PARA_CUPON_DEFAULT = 10;
	private static final double DESCUENTO_CUPON_DEFAULT = 15.0;

	public DatosMaestro obtenerConfiguracion() {

		return repository.findById(1L)
				.orElseGet(() -> {

					DatosMaestro dm = DatosMaestro.builder()
							.id(1L)
							.descuentoCumple(DESCUENTO_CUMPLE_DEFAULT)
							.cortesPorPunto(CORTES_POR_PUNTO_DEFAULT)
							.puntosParaCupon(PUNTOS_PARA_CUPON_DEFAULT)
							.descuentoCupon(DESCUENTO_CUPON_DEFAULT)
							.build();

					return repository.save(dm);
				});
	}

	public DatosMaestro edit(DatosMaestro datos) {
		datos.setId(1L);
		return repository.save(datos);
	}
}
