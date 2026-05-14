package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

@Service
public class ClienteServicio extends BaseServiciosImpl<Cliente, Long, ClienteRepositorio> {

	private ClienteRepositorio repositorio;

	@Override
	public List<Cliente> obtenerCumple() {
		LocalDate hoy = LocalDate.now();
		return repositorio.findByCumple(hoy.getMonthValue(), hoy.getDayOfMonth());
	}

	@Override
	public boolean esCumple(Cliente cliente) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * @Override public boolean esCumple(Cliente cliente) { if (cliente.get == null)
	 * return false;
	 * 
	 * LocalDate hoy = LocalDate.now(); LocalDate cumple = cliente.getClass();
	 * 
	 * return cumple.getMonth() == hoy.getMonth() && cumple.getDayOfMonth() ==
	 * hoy.getDayOfMonth(); }
	 */

}
