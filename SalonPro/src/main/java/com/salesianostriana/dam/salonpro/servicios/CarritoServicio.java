package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoServicio extends BaseServiciosImpl<Servicio, Long, ServicioRepositorio> {

	private final ClienteServicio clienteServicio;
	private final CitaService citaService;

	private Map<Servicio, Integer> servicio = new HashMap<>();

	public void addProducto(Servicio s) {
		if (servicio.containsKey(s)) {
			servicio.replace(s, servicio.get(s) + 1);
		} else {
			servicio.put(s, 1);
		}
	}

	public void removeProductoPorId(Long id) {
		if (this.servicio == null || id == null) {
			return;
		}

		this.servicio.keySet()
				.stream()
				.filter(producto -> producto.getId()
						.equals(id))
				.findFirst()
				.ifPresent(producto -> this.servicio.remove(producto));
	}

	public Map<Servicio, Integer> getProductsInCart() {
		return Collections.unmodifiableMap(servicio);
	}

	public Double calcularTotal() {
		if (servicio == null) {
			return 0.0;
		}

		return servicio.entrySet()
				.stream()
				.mapToDouble(entry -> entry.getKey()
						.getPrecio() * entry.getValue())
				.sum();
	}

	public void vaciarCarrito() {
		if (this.servicio != null) {
			this.servicio.clear();
		}
	}

	public TicketCita tramitarCarrito(LocalDateTime fechaHora, String observaciones, String emailCliente) {

		if (getProductsInCart().isEmpty()) {
			throw new IllegalArgumentException("Debes añadir servicios antes de reservar");
		}

		if (fechaHora.isBefore(LocalDateTime.now())) {
			throw new ConflictoFechaException("La fecha seleccionada no puede ser pasada");
		}

		Cliente cliente = clienteServicio.findByEmail(emailCliente)
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con email: " + emailCliente));

		Map<Servicio, Integer> carrito = new HashMap<>(getProductsInCart());
		double totalBase = calcularTotal();

		Cita cita = new Cita();
		cita.setFecha(fechaHora);

		citaService.registrarCita(cita, cliente.getId(), carrito, observaciones);
		vaciarCarrito();

		return new TicketCita(cliente, carrito, totalBase, cita, fechaHora, observaciones);
	}

}
