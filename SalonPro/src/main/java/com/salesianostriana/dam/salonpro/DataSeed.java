package com.salesianostriana.dam.salonpro;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeed {

	private final ClienteRepositorio clienteRepositorio;
	private final ServicioRepositorio servicioRepositorio;

	@PostConstruct
	public void run() {

		// Clientes

		List<Cliente> listaClientes = List.of(Cliente.builder()

				.nombre("Pepito")
				.telefono("696322304")
				.email("futa@pera.com")
				.cumple(LocalDate.of(1998, 5, 11))
				.build(),

				Cliente.builder()
						.nombre("Michael Scott")
						.telefono("600998877")
						.email("michael.scott@dundermifflin.com")
						.cumple(LocalDate.of(1964, 3, 15))
						.build(),

				Cliente.builder()
						.nombre("Dwight Schrute")
						.telefono("611223344")
						.email("dwight.schrute@dundermifflin.com")
						.cumple(LocalDate.of(1970, 1, 20))
						.build(),

				Cliente.builder()
						.nombre("Jim Halpert")
						.telefono("622334455")
						.email("jim.halpert@dundermifflin.com")
						.cumple(LocalDate.of(1978, 10, 1))
						.build(),

				Cliente.builder()
						.nombre("Walter White")
						.telefono("600112233")
						.email("heisenberg@abq.com")
						.cumple(LocalDate.of(1958, 9, 7))
						.build(),

				Cliente.builder()
						.nombre("Jesse Pinkman")
						.telefono("622334455")
						.email("capncook@abq.com")
						.cumple(LocalDate.of(1984, 9, 24))
						.build(),

				Cliente.builder()
						.nombre("Saul Goodman")
						.telefono("699887766")
						.email("bettercallsaul@law.com")
						.cumple(LocalDate.of(1960, 11, 12))
						.build());

		clienteRepositorio.saveAll(listaClientes);

		// Servicios

		List<Servicio> listaServicios = List.of(Servicio.builder()
				.nombre("Corte de Pelo Caballero")
				.precio(15.50)
				.duracion(Duration.ofMinutes(30))
				.build(),

				Servicio.builder()
						.nombre("Lavado y Peinado Señora")
						.precio(25.00)
						.duracion(Duration.ofMinutes(45))
						.build(),

				Servicio.builder()
						.nombre("Tinte Completo")
						.precio(35.90)
						.duracion(Duration.ofMinutes(90))
						.build(),

				Servicio.builder()
						.nombre("Arreglo de Barba Premium")
						.precio(12.00)
						.duracion(Duration.ofMinutes(20))
						.build(),

				Servicio.builder()
						.nombre("Tratamiento de Keratina")
						.precio(65.00)
						.duracion(Duration.ofMinutes(120))
						.build(),

				Servicio.builder()
						.nombre("Corte de Pelo Niño")
						.precio(10.00)
						.duracion(Duration.ofMinutes(25))
						.build());

		servicioRepositorio.saveAll(listaServicios);
	}
}