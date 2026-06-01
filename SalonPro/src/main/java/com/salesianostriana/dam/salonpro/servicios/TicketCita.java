package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.Map;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;

public record TicketCita(
		Cliente cliente,
		Map<Servicio, Integer> carrito,
		double totalBase,
		Cita cita,
		LocalDateTime fecha,
		String observaciones) {

	public double total() {
		return cita.getPrecioTotal();
	}

	public double descuentoAplicado() {
		return totalBase - total();
	}
}
