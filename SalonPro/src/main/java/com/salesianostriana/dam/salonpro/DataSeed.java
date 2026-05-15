package com.salesianostriana.dam.salonpro;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeed {

	private final ClienteRepositorio clienteRepositorio;

	@PostConstruct
	public void run() {
		Cliente c = Cliente.builder()
				.nombre("Pepito")
				.telefono("696322304")
				.email("futa@pera.com")
				.cumple(LocalDate.of(1998, 05, 11))
				.build();

		clienteRepositorio.save(c);

		Cliente c2 = Cliente.builder()
				.nombre("María")
				.telefono("600111222")
				.email("maria@ejemplo.com")
				.cumple(LocalDate.of(1995, 8, 25))
				.build();

		clienteRepositorio.save(c2);
	}

}
