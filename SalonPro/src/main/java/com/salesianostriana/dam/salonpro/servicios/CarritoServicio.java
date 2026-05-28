package com.salesianostriana.dam.salonpro.servicios;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoServicio extends BaseServiciosImpl<Servicio, Long, ServicioRepositorio> {

	private Map<Servicio, Integer> servicio = new HashMap<>();

	public void addProducto(Servicio s) {
		if (servicio.containsKey(s)) {
			servicio.replace(s, servicio.get(s) + 1);
		} else {
			servicio.put(s, 1);
		}
	}

	public void removeProducto(Servicio s) {
		if (servicio.containsKey(s)) {
			if (servicio.get(s) > 1)
				servicio.replace(s, servicio.get(s) - 1);
			else if (servicio.get(s) == 1) {
				servicio.remove(s);
			}
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

}
