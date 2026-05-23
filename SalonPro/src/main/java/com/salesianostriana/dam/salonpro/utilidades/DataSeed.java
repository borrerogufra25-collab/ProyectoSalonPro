/* Para ir rápido:
 * Email: usuario@usuario.com
 * Contraseña: user
 * 
 * Email: admin@admin.com
 * Contraseña: admin
 */

package com.salesianostriana.dam.salonpro.utilidades;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.repositorios.ServicioRepositorio;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeed {

	private final ClienteRepositorio clienteRepositorio;
	private final ServicioRepositorio servicioRepositorio;
	private final PasswordEncoder passwordEncoder;

	@PostConstruct
	public void run() {

		// Clientes

		List<Cliente> clientesOriginales = List.of(Cliente.builder()
				.nombre("Pepito")
				.telefono("696322304")
				.email("futa@pera.com")
				.contrasenia(passwordEncoder.encode("1234"))
				.role(UserRole.USER)
				.cumple(LocalDate.of(1998, 5, 11))
				.build(),

				Cliente.builder()
						.nombre("Michael Scott")
						.telefono("600998877")
						.email("michael@dundermifflin.com")
						.contrasenia(passwordEncoder.encode("1234"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1964, 3, 15))
						.build(),

				Cliente.builder()
						.nombre("Walter White")
						.telefono("600112233")
						.email("walter@white.com")
						.contrasenia(passwordEncoder.encode("1234"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1958, 9, 7))
						.build(),

				Cliente.builder()
						.nombre("Jesse Pinkman")
						.telefono("622334455")
						.email("jesse@pinkman.com")
						.contrasenia(passwordEncoder.encode("1234"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1984, 9, 24))
						.build(),

				Cliente.builder()
						.nombre("Saul Goodman")
						.telefono("699887766")
						.email("saul@goodman.com")
						.contrasenia(passwordEncoder.encode("1234"))
						.role(UserRole.USER)
						.cumple(LocalDate.of(1960, 11, 12))
						.build());

		clienteRepositorio.saveAll(clientesOriginales);

		// Para probar

		Cliente usuarioPrueba = Cliente.builder()
				.nombre("Usuario de Prueba")
				.telefono("111222333")
				.email("usuario@usuario.com")
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
						.build());

		servicioRepositorio.saveAll(listaServicios);
	}
}