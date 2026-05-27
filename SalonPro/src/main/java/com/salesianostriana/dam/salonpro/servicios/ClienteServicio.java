package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

@Service
public class ClienteServicio extends BaseServiciosImpl<Cliente, Long, ClienteRepositorio> {

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

}
