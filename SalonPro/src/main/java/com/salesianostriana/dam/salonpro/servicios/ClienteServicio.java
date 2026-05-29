package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServicio extends BaseServiciosImpl<Cliente, Long, ClienteRepositorio> {

	private final DatosMaestroServicio datosMaestroServicio;

	public Optional<Cliente> findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public List<Cliente> obtenerCumple() {
		LocalDate hoy = LocalDate.now();
		return repository.findByCumple(hoy.getMonthValue(), hoy.getDayOfMonth());
	}

	public boolean esCumple(Cliente cliente) {
		if (cliente.getCumple() == null)
			return false;

		LocalDate hoy = LocalDate.now();
		LocalDate cumple = cliente.getCumple();

		return cumple.getMonth() == hoy.getMonth() && cumple.getDayOfMonth() == hoy.getDayOfMonth();
	}

	public void aumentarPelados(Cliente cliente) {
		cliente.setNumCortes(cliente.getNumCortes() + 1);
		save(cliente);
	}

	public double aplicarDescuentoCumple(Cliente cliente, double precioBase) {
		double descuento, precioConDescuento;

		if (!esCumple(cliente)) {
			return precioBase;
		}

		descuento = datosMaestroServicio.obtenerConfiguracion()
				.getDescuentoCumple();
		precioConDescuento = precioBase * (1 - descuento / 100);

		return Math.max(0, Math.round(precioConDescuento * 100.0) / 100.0);
	}

}
