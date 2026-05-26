/* Para ir rápido:
 * Email: usuario@usuario.com
 * Contraseña: user
 * * Email: admin@admin.com
 * Contraseña: admin
 */

package com.salesianostriana.dam.salonpro.utilidades;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.CitaServicio;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeed {

	private final ClienteRepositorio clienteRepositorio;
	private final ServicioRepositorio servicioRepositorio;
	private final CitaRepositorio citaRepositorio;
	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void run() {

		// Clientes

		List<Cliente> clientesOriginales = List.of(Cliente.builder()
				.numCortes(3)
				.nombre("Pepito")
				.telefono("696322304")
				.email("futa@pera.com")
				.contrasenia(passwordEncoder.encode("user"))
				.role(UserRole.USER)
				.cumple(LocalDate.of(1998, 5, 11))
				.build(),

				Cliente.builder()
						.numCortes(4)
						.nombre("Michael Scott")
						.telefono("600998877")
						.email("michael@dundermifflin.com")
						.contrasenia(passwordEncoder.encode("user"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1964, 3, 15))
						.build(),

				Cliente.builder()
						.numCortes(1)
						.nombre("Walter White")
						.telefono("600112233")
						.email("walter@white.com")
						.contrasenia(passwordEncoder.encode("user"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1958, 9, 7))
						.build(),

				Cliente.builder()
						.numCortes(5)
						.nombre("Jesse Pinkman")
						.telefono("622334455")
						.email("jesse@pinkman.com")
						.contrasenia(passwordEncoder.encode("user"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1984, 9, 24))
						.build(),

				Cliente.builder()
						.numCortes(17)
						.nombre("Saul Goodman")
						.telefono("699887766")
						.email("saul@goodman.com")
						.contrasenia(passwordEncoder.encode("user"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1960, 11, 12))
						.build());

		clienteRepositorio.saveAll(clientesOriginales);

		// Para probar

		Cliente usuarioPrueba = Cliente.builder()
				.nombre("Usuario de Prueba")
				.telefono("111222333")
				.email("user@user.com")
				.contrasenia(passwordEncoder.encode("user"))
				.role(UserRole.USER)
				.build();

		Cliente adminPrueba = Cliente.builder()
				.nombre("Administrador")
				.telefono("999888777")
				.email("admin@admin.com")
				.contrasenia(passwordEncoder.encode("admin"))
				.role(UserRole.ADMIN)
				.build();

		clienteRepositorio.save(usuarioPrueba);
		clienteRepositorio.save(adminPrueba);

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
						.precio(35.00)
						.duracion(Duration.ofMinutes(60))
						.build(),

				Servicio.builder()
						.nombre("Peinado niña pequeña (de 0 a 10 años)")
						.precio(22.00)
						.duracion(Duration.ofMinutes(30))
						.build(),

				Servicio.builder()
						.nombre("Matiz + difuminado")
						.precio(48.00)
						.duracion(Duration.ofMinutes(30))
						.build(),

				Servicio.builder()
						.nombre("No-breaker aplicación")
						.precio(8.99)
						.duracion(Duration.ofMinutes(5))
						.build(),

				Servicio.builder()
						.nombre("Secado express")
						.precio(20.00)
						.duracion(Duration.ofMinutes(20))
						.build(),

				Servicio.builder()
						.nombre("Lavado")
						.precio(0.0)
						.duracion(Duration.ofMinutes(20))
						.build(),

				Servicio.builder()
						.nombre("Tratamiento Reconstructor No-Breaker")
						.precio(40.00)
						.duracion(Duration.ofMinutes(30))
						.build(),

				Servicio.builder()
						.nombre("Diseño de cejas")
						.precio(13.00)
						.duracion(Duration.ofMinutes(15))
						.build(),

				Servicio.builder()
						.nombre("Laminado de cejas")
						.precio(25.00)
						.duracion(Duration.ofMinutes(40))
						.build(),

				Servicio.builder()
						.nombre("Manicura Princess (hasta 12 años)")
						.precio(14.00)
						.duracion(Duration.ofMinutes(30))
						.build(),

				Servicio.builder()
						.nombre("Prueba de peinado novia")
						.precio(85.00)
						.duracion(Duration.ofHours(1)
								.plusMinutes(15))
						.build());

		List<Servicio> serviciosGuardados = servicioRepositorio.saveAll(listaServicios);

		// Citas

		Cita cita1 = Cita.builder()
				.fecha(LocalDateTime.now())
				.cliente(usuarioPrueba)
				.precioTotal(serviciosGuardados.get(0)
						.getPrecio())
				.build();

		CitaServicio cs1 = CitaServicio.builder()
				.cita(cita1)
				.servicio(serviciosGuardados.get(0))
				.observaciones("Quiere un mohicano inverso")
				.build();

		// Añadir el servicio a la cita
		cita1.setCitaServicios(List.of(cs1));

		Cita cita2 = Cita.builder()
				.fecha(LocalDateTime.now()
						.withHour(17)
						.withMinute(30))
				.cliente(clientesOriginales.get(0))
				.precioTotal(serviciosGuardados.get(1)
						.getPrecio()
						+ serviciosGuardados.get(2)
								.getPrecio())
				.build();

		CitaServicio cs2 = CitaServicio.builder()
				.cita(cita2)
				.servicio(serviciosGuardados.get(1))
				.observaciones("Usar champú para calvos")
				.build();

		CitaServicio cs3 = CitaServicio.builder()
				.cita(cita2)
				.servicio(serviciosGuardados.get(2))
				.observaciones("Tinte color fuxia")
				.build();

		cita2.setCitaServicios(List.of(cs2, cs3));

		citaRepositorio.saveAll(List.of(cita1, cita2));
	}
}