package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaService extends BaseServiciosImpl<Cita, Long, CitaRepositorio> {

	private boolean seSolapan(LocalDateTime inicioA, LocalDateTime finA, LocalDateTime inicioB, LocalDateTime finB) {
		return inicioA.isBefore(finB) && finA.isAfter(inicioB);
	}

	public boolean tieneConflictoHorario(Long clienteID, LocalDateTime inicioNuevaCita) {

		Servicio servicio = new Servicio();
		long duracion = servicio.getDuracion()
				.toMinutes();

		LocalDateTime finNuevaCita = inicioNuevaCita.plusHours(duracion);

		List<Cita> todasLasCitas = this.findAll();

		return todasLasCitas.stream()
				.filter(cita -> cita.getCliente() != null && cita.getCliente()
						.getId()
						.equals(clienteID))
				.anyMatch(citaExistente -> {
					LocalDateTime inicioExistente = citaExistente.getFecha();
					LocalDateTime finExistente = inicioExistente.plusHours(duracion);
					return seSolapan(inicioNuevaCita, finNuevaCita, inicioExistente, finExistente);
				});
	}

}