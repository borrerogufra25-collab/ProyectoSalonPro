package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Cupon;
import com.salesianostriana.dam.salonpro.repositorios.CuponRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuponServicio extends BaseServiciosImpl<Cupon, Long, CuponRepositorio> {

	public Cupon generarCupon(Cliente cliente, double descuento) {
		Cupon cupon = Cupon.builder()
				.cliente(cliente)
				.descuento(descuento)
				.usado(false)
				.fechaCreacion(LocalDateTime.now())
				.build();

		return save(cupon);
	}

	public Optional<Cupon> buscarCuponDisponible(Cliente cliente) {
		if (cliente == null || cliente.getId() == null) {
			return Optional.empty();
		}

		return repository.findFirstByClienteIdAndUsadoFalseOrderByFechaCreacionAsc(cliente.getId());
	}

	public Optional<Cupon> buscarCuponUsadoEnCita(Cita cita) {
		if (cita == null || cita.getCodigo() == null) {
			return Optional.empty();
		}

		return repository.findFirstByCitaUsoCodigo(cita.getCodigo());
	}

	public List<Cupon> listarCuponesDisponibles(Cliente cliente) {
		if (cliente == null || cliente.getId() == null) {
			return List.of();
		}

		return repository.findByClienteIdAndUsadoFalseOrderByFechaCreacionAsc(cliente.getId());
	}

	public double calcularPrecioConCupon(Cupon cupon, double precioBase) {
		if (cupon == null || cupon.isUsado()) {
			return precioBase;
		}

		double precioConDescuento = precioBase * (1 - cupon.getDescuento() / 100);
		return Math.max(0, Math.round(precioConDescuento * 100.0) / 100.0);
	}

	public void marcarComoUsado(Cupon cupon, Cita cita) {
		cupon.setUsado(true);
		cupon.setFechaUso(LocalDateTime.now());
		cupon.setCitaUso(cita);
		save(cupon);
	}

	public void liberarCuponesDeCita(Cita cita) {
		if (cita == null || cita.getCodigo() == null) {
			return;
		}

		repository.findByCitaUsoCodigo(cita.getCodigo())
				.forEach(cupon -> {
					cupon.setUsado(false);
					cupon.setFechaUso(null);
					cupon.setCitaUso(null);
					save(cupon);
				});
	}
}
