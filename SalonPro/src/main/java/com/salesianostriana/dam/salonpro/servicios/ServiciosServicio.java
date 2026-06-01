package com.salesianostriana.dam.salonpro.servicios;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiciosServicio extends BaseServiciosImpl<Servicio, Long, ServicioRepositorio> {

	private final CitaRepositorio citaRepositorio;
	private final CuponServicio cuponServicio;

	public boolean tieneCitasAsociadas(Long servicioId) {
		return repository.existsCitaByServicioId(servicioId);
	}

	public Map<Long, Boolean> obtenerMapaServiciosConCitas(List<Servicio> servicios) {
		return servicios.stream()
				.collect(Collectors.toMap(Servicio::getId, s -> tieneCitasAsociadas(s.getId())));
	}

	public void borrarServicioYCitasAsociadas(Servicio servicio) {
		List<Cita> citasAsociadas = citaRepositorio.findByServicioId(servicio.getId());

		citasAsociadas.forEach(cita -> {
			if (cita.getCliente() != null && cita.getCliente()
					.getListaCitas() != null) {
				cita.getCliente()
						.getListaCitas()
						.remove(cita);
			}
			cuponServicio.liberarCuponesDeCita(cita);
		});

		citaRepositorio.deleteAll(citasAsociadas);
		repository.delete(servicio);
	}

	public void borrarServicioYCitasAsociadasSiExiste(Long id) {
		findById(id).ifPresent(this::borrarServicioYCitasAsociadas);
	}

}
