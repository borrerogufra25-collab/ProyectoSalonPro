package com.salesianostriana.dam.salonpro.controladores;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.servicios.CarritoServicio;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.CitaService;
import com.salesianostriana.dam.salonpro.servicios.CuponServicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CarritoControlador {

	private final CarritoServicio carritoServicio;
	private final ServiciosServicio serviciosServicio;
	private final ClienteServicio clienteServicio;
	private final CitaService citaService;
	private final CuponServicio cuponServicio;

	// Listar

	@GetMapping("/inicioUsuario/servicios")
	public String listarServicios(Model model, Principal principal) {
		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(
						() -> new NoSuchElementException("Cliente no encontrado con email: " + principal.getName()));

		model.addAttribute("productos", serviciosServicio.findAll());
		model.addAttribute("carrito", carritoServicio.getProductsInCart());
		model.addAttribute("cliente", cliente);
		model.addAttribute("cuponesDisponibles", cuponServicio.listarCuponesDisponibles(cliente));

		return "usuario/pedirServicios";
	}

	// Meter

	@GetMapping("/inicioUsuario/servicios/{id}")
	public String agregarServicio(@PathVariable Long id) {

		Servicio s = serviciosServicio.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Servicio no encontrado con ID: " + id));

		carritoServicio.addProducto(s);

		return "redirect:/inicioUsuario/servicios";
	}

	// Borrar

	@GetMapping("/carrito/borrar/{id}")
	public String borrarServicio(@PathVariable Long id) {

		carritoServicio.removeProductoPorId(id);

		return "redirect:/inicioUsuario/servicios";
	}

	// Borrar todo

	@GetMapping("/carrito/vaciar")
	public String vaciarCarrito() {

		carritoServicio.vaciarCarrito();

		return "redirect:/inicioUsuario/servicios";
	}

	// Para que no se borre

	@ModelAttribute("total_carrito")
	public Double totalCarrito() {
		return carritoServicio.calcularTotal();
	}

	// Tramitar

	@GetMapping("/carrito/tramitar")
	public String mostrarTicket() {
		return "redirect:/inicioUsuario/servicios";
	}

	@PostMapping("/carrito/tramitar")
	public String tramitar(
			@RequestParam("fechaHora") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
			@RequestParam("observaciones") String observaciones, Principal principal, Model model) {

		if (carritoServicio.getProductsInCart()
				.isEmpty()) {
			return "redirect:/inicioUsuario/servicios?error=carritoVacio";
		}

		if (fechaHora.isBefore(LocalDateTime.now())) {
			return "redirect:/inicioUsuario/servicios?error=fechaPasada";
		}

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(
						() -> new NoSuchElementException("Cliente no encontrado con email: " + principal.getName()));

		// Esto para copiar el carrito tal y como está ahora

		Map<Servicio, Integer> carrito = new HashMap<>(carritoServicio.getProductsInCart());
		double totalBase = carritoServicio.calcularTotal();

		Cita cita = new Cita();
		cita.setFecha(fechaHora);

		try {
			citaService.registrarCita(cita, cliente.getId(), carrito, observaciones);
		} catch (ConflictoFechaException e) {
			return "redirect:/inicioUsuario/servicios?error=conflicto";
		}

		carritoServicio.vaciarCarrito();

		model.addAttribute("cliente", cliente);
		model.addAttribute("carrito", carrito);
		model.addAttribute("totalBase", totalBase);
		model.addAttribute("total", cita.getPrecioTotal());
		model.addAttribute("descuentoAplicado", totalBase - cita.getPrecioTotal());
		model.addAttribute("fecha", fechaHora);
		model.addAttribute("observaciones", observaciones);

		return "usuario/ticket";
	}

}
