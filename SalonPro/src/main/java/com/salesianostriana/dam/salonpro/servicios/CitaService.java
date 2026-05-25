package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.CitaServicio;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaService extends BaseServiciosImpl<Cita, Long, CitaRepositorio> {

	private final ServiciosServicio serviciosServicio;
	private final ClienteServicio clienteServicio;

	private boolean seSolapan(LocalDateTime inicioA, LocalDateTime finA, LocalDateTime inicioB, LocalDateTime finB) {
		return inicioA.isBefore(finB) && finA.isAfter(inicioB);
	}

	public boolean tieneConflictoHorario(LocalDateTime inicioNuevaCita, List<Servicio> servicios) {

		long minutosTotales = servicios.stream()
				.mapToLong(s -> s.getDuracion()
						.toMinutes())
				.sum();

		LocalDateTime finNuevaCita = inicioNuevaCita.plusMinutes(minutosTotales);
		List<Cita> todasLasCitas = this.findAll();

		return todasLasCitas.stream()
				.anyMatch(citaExistente -> {

					long duracionExistente = citaExistente.getCitaServicios()
							.stream()
							.mapToLong(cs -> cs.getServicio()
									.getDuracion()
									.toMinutes())
							.sum();
					LocalDateTime finExistente = citaExistente.getFecha()
							.plusMinutes(duracionExistente);

					return seSolapan(inicioNuevaCita, finNuevaCita, citaExistente.getFecha(), finExistente);
				});
	}

	public void registrarCita(Cita cita, Long clienteId, Long servicioId, String observaciones) {

		Servicio servicio = serviciosServicio.findById(servicioId)
				.orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado"));
		Cliente cliente = clienteServicio.findById(clienteId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

		cita.setCliente(cliente);
		cita.setPrecioTotal(servicio.getPrecio());

		CitaServicio detalle = CitaServicio.builder()
				.cita(cita)
				.servicio(servicio)
				.observaciones(observaciones)
				.build();
		cita.setCitaServicios(new ArrayList<>(List.of(detalle)));

		if (this.tieneConflictoHorario(cita.getFecha(), List.of(servicio))) {
			throw new ConflictoFechaException(cita);
		}

		this.save(cita);
	}

}