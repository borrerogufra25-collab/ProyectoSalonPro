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
import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.DatosMaestroRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeed {

	private final ClienteRepositorio clienteRepositorio;
	private final ServicioRepositorio servicioRepositorio;
	private final CitaRepositorio citaRepositorio;
	private final DatosMaestroRepositorio datosMaestroRepositorio;
	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void run() {

		if (datosMaestroRepositorio.count() == 0) {
			datosMaestroRepositorio.save(DatosMaestro.builder()
					.descuentoCumple(10.0)
					.build());
		}

		// Clientes

		List<Cliente> clientesOriginales = List.of(Cliente.builder()
				.numCortes(3)
				.nombre("Pepito")
				.telefono("696322304")
				.email("futa@pera.com")
				.contrasenia(passwordEncoder.encode("user"))
				.role(UserRole.USER)
				.cumple(LocalDate.of(1998, 5, 11))
				.numCortes(1)
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

		// Para probar roles

		Cliente usuarioPrueba = Cliente.builder()
				.nombre("Usuario de Prueba")
				.telefono("999999999")
				.email("user@user.com")
				.contrasenia(passwordEncoder.encode("user"))
				.role(UserRole.USER)
				.numCortes(1)
				.cumple(LocalDate.now())
				.build();

		Cliente adminPrueba = Cliente.builder()
				.nombre("Administrador")
				.telefono("999999999")
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
				.fecha(LocalDateTime.now()
						.plusHours(2))
				.cliente(usuarioPrueba)
				.precioTotal(serviciosGuardados.get(0)
						.getPrecio())
				.build();

		CitaServicio cs1 = CitaServicio.builder()
				.cita(cita1)
				.servicio(serviciosGuardados.get(0))
				.observaciones("Quiere un mohicano inverso")
				.build();

		cita1.setCitaServicios(List.of(cs1));

		Cita cita2 = Cita.builder()
				.fecha(LocalDateTime.now()
						.plusHours(5))
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

		Cita cita3 = Cita.builder()
				.fecha(LocalDateTime.now()
						.plusDays(1)
						.withHour(10)
						.withMinute(0))
				.cliente(clientesOriginales.get(1))
				.precioTotal(serviciosGuardados.get(3)
						.getPrecio())
				.build();

		CitaServicio cs4 = CitaServicio.builder()
				.cita(cita3)
				.servicio(serviciosGuardados.get(3))
				.observaciones("Peinado elegante para reunión importante")
				.build();

		cita3.setCitaServicios(List.of(cs4));

		Cita cita4 = Cita.builder()
				.fecha(LocalDateTime.now()
						.plusDays(2)
						.withHour(12)
						.withMinute(30))
				.cliente(clientesOriginales.get(2))
				.precioTotal(serviciosGuardados.get(4)
						.getPrecio()
						+ serviciosGuardados.get(5)
								.getPrecio())
				.build();

		CitaServicio cs5 = CitaServicio.builder()
				.cita(cita4)
				.servicio(serviciosGuardados.get(4))
				.observaciones("Difuminado suave, estilo discreto")
				.build();

		CitaServicio cs6 = CitaServicio.builder()
				.cita(cita4)
				.servicio(serviciosGuardados.get(5))
				.observaciones("Aplicar No-breaker con cuidado")
				.build();

		cita4.setCitaServicios(List.of(cs5, cs6));

		Cita cita5 = Cita.builder()
				.fecha(LocalDateTime.now()
						.plusDays(3)
						.withHour(16)
						.withMinute(0))
				.cliente(clientesOriginales.get(3))
				.precioTotal(serviciosGuardados.get(10)
						.getPrecio())
				.build();

		CitaServicio cs7 = CitaServicio.builder()
				.cita(cita5)
				.servicio(serviciosGuardados.get(10))
				.observaciones("Quiere cejas muy marcadas, estilo urbano")
				.build();

		cita5.setCitaServicios(List.of(cs7));

		Cita cita6 = Cita.builder()
				.fecha(LocalDateTime.now()
						.plusDays(4)
						.withHour(18)
						.withMinute(15))
				.cliente(clientesOriginales.get(4))
				.precioTotal(serviciosGuardados.get(12)
						.getPrecio())
				.build();

		CitaServicio cs8 = CitaServicio.builder()
				.cita(cita6)
				.servicio(serviciosGuardados.get(12))
				.observaciones("Peinado de prueba para evento legal importante")
				.build();

		cita6.setCitaServicios(List.of(cs8));

		citaRepositorio.saveAll(List.of(cita1, cita2, cita3, cita4, cita5, cita6));
	}
}
