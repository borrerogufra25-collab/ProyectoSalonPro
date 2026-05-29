package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	private final ClienteServicio clienteServicio;

	public List<Cita> listarCitasDeCliente(Long clienteId) {
		return repository.findByClienteId(clienteId);
	}

	private boolean seSolapan(LocalDateTime inicioA, LocalDateTime finA, LocalDateTime inicioB, LocalDateTime finB) {
		return inicioA.isBefore(finB) && finA.isAfter(inicioB);
	}

	private List<Servicio> expandServicios(Map<Servicio, Integer> servicios) {
		return servicios.entrySet()
				.stream()
				.flatMap(entry -> IntStream.range(0, entry.getValue())
						.mapToObj(i -> entry.getKey()))
				.collect(Collectors.toList());
	}

	public boolean tieneConflictoHorario(LocalDateTime inicioNuevaCita, Map<Servicio, Integer> servicios) {

		long minutosTotales = expandServicios(servicios).stream()
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

	public void registrarCita(Cita cita, Long clienteId, Map<Servicio, Integer> servicios, String observaciones) {

		Cliente cliente;
		double precioFinal, precioBase;
		List<CitaServicio> detalles = new ArrayList<>();

		if (servicios == null || servicios.isEmpty()) {
			throw new IllegalArgumentException("No se han seleccionado servicios para la cita");
		}

		cliente = clienteServicio.findById(clienteId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

		cita.setCliente(cliente);

		// Calculeo
		precioBase = servicios.entrySet()
				.stream()
				.mapToDouble(entry -> entry.getKey()
						.getPrecio() * entry.getValue())
				.sum();

		precioFinal = clienteServicio.aplicarDescuentoCumple(cliente, precioBase);

		cita.setPrecioTotal(precioFinal);

		// Crearlo
		servicios.forEach((servicio, cantidad) -> {
			IntStream.range(0, cantidad)
					.forEach(i -> detalles.add(CitaServicio.builder()
							.cita(cita)
							.servicio(servicio)
							.observaciones(observaciones)
							.build()));
		});

		cita.setCitaServicios(detalles);

		if (this.tieneConflictoHorario(cita.getFecha(), servicios)) {
			throw new ConflictoFechaException(cita);
		}

		this.save(cita);

		clienteServicio.aumentarPelados(cliente);
	}

}
