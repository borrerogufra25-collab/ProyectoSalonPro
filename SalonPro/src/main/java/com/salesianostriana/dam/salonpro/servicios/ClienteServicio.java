package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServicio extends BaseServiciosImpl<Cliente, Long, ClienteRepositorio> {

	private final DatosMaestroServicio datosMaestroServicio;
	private final CuponServicio cuponServicio;
	private final PasswordEncoder passwordEncoder;

	public Optional<Cliente> findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public List<Cliente> listarClientesUsuarios() {
		return findAll().stream()
				.filter(c -> c.getRole() != UserRole.ADMIN)
				.toList();
	}

	public Cliente registrarUsuario(Cliente cliente) {
		cliente.setRole(UserRole.USER);
		cliente.setContrasenia(passwordEncoder.encode(cliente.getContrasenia()));
		return save(cliente);
	}

	public Cliente editarCliente(Cliente cliente) {
		return edit(cliente);
	}

	public void borrarClienteSiExiste(Long id) {
		findById(id).ifPresent(this::delete);
	}

	public CambioContraseniaResultado cambiarContraseniaAdmin(String email, String nuevaContrasenia,
			String confirmarContrasenia) {

		if (nuevaContrasenia == null || nuevaContrasenia.isBlank()) {
			return CambioContraseniaResultado.VACIA;
		}

		if (!nuevaContrasenia.equals(confirmarContrasenia)) {
			return CambioContraseniaResultado.NO_COINCIDE;
		}

		Cliente admin = findByEmail(email)
				.filter(c -> c.getRole() == UserRole.ADMIN)
				.orElseThrow(() -> new NoSuchElementException("Administrador no encontrado"));

		admin.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
		edit(admin);

		return CambioContraseniaResultado.EXITO;
	}

	public boolean esCumple(Cliente cliente) {
		if (cliente.getCumple() == null)
			return false;

		LocalDate hoy = LocalDate.now();
		LocalDate cumple = cliente.getCumple();

		return cumple.getMonth() == hoy.getMonth() && cumple.getDayOfMonth() == hoy.getDayOfMonth();
	}

	public void aumentarPelados(Cliente cliente) {

		DatosMaestro dm = datosMaestroServicio.obtenerConfiguracion();

		cliente.setNumCortes(cliente.getNumCortes() + 1);

		if (cliente.getNumCortes() % dm.getCortesPorPunto() == 0) {
			cliente.setPuntos(cliente.getPuntos() + 1);
		}

		if (cliente.getPuntos() >= dm.getPuntosParaCupon()) {
			cuponServicio.generarCupon(cliente, dm.getDescuentoCupon());
			cliente.setPuntos(0);
		}

		save(cliente);
	}

	public double aplicarDescuentoCumple(Cliente cliente, double precioBase) {
		double descuento, precioConDescuento;

		if (!esCumple(cliente)) {
			return precioBase;
		}

		descuento = datosMaestroServicio.obtenerConfiguracion()
				.getDescuentoCumple();
		precioConDescuento = precioBase - (precioBase * descuento / 100);

		return Math.max(0, Math.round(precioConDescuento * 100.0) / 100.0);
	}

	public enum CambioContraseniaResultado {
		EXITO("passwordExito"),
		VACIA("passwordVacia"),
		NO_COINCIDE("passwordNoCoincide");

		private final String parametroUrl;

		CambioContraseniaResultado(String parametroUrl) {
			this.parametroUrl = parametroUrl;
		}

		public String getParametroUrl() {
			return parametroUrl;
		}
	}

}
