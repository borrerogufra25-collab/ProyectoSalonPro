package com.salesianostriana.dam.salonpro.controladores;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.servicios.CitaService;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CitaControlador {

	private final CitaService citaService;
	private final ClienteServicio clienteServicio;
	private final ServiciosServicio serviciosServicio;

	// Listar

	@GetMapping("/inicioAdmin/citas")
	public String listar(Model model) {
		model.addAttribute("listaCitas", citaService.findAll());
		return "citas/listarCitas";
	}

	@GetMapping("/inicioUsuario/citas")
	public String listarCitasCliente(Principal principal, Model model) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		List<Cita> citas = citaService.listarCitasDeCliente(cliente.getId());

		model.addAttribute("cliente", cliente);
		model.addAttribute("citas", citas);

		return "usuario/susCitas";
	}

	// Editar (user)

	@GetMapping("/inicioUsuario/citas/editar/{id}")
	public String editarCitaCliente(@PathVariable Long id, Principal principal, Model model) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		Cita cita = citaService.obtenerCitaDeCliente(id, cliente.getId());

		model.addAttribute("cliente", cliente);
		model.addAttribute("cita", cita);
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		model.addAttribute("serviciosCantidad", citaService.obtenerCantidadesServicios(cita));
		model.addAttribute("observaciones", citaService.obtenerObservaciones(cita));

		return "usuario/formularioCita";
	}

	@PostMapping("/inicioUsuario/citas/editar/submit")
	public String submitEditarCitaCliente(@RequestParam("citaId") Long citaId,
			@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha,
			@RequestParam("observaciones") String observaciones, @RequestParam Map<String, String> params,
			Principal principal) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		try {
			citaService.editarCitaDeCliente(citaId, cliente.getId(), params, fecha, observaciones);
		} catch (ConflictoFechaException | IllegalArgumentException e) {
			return "redirect:/inicioUsuario/citas/editar/" + citaId + "?error=" + codificar(e.getMessage());
		}

		return "redirect:/inicioUsuario/citas";
	}

	// Crear (admin)

	@GetMapping("/inicioAdmin/citas/nueva")
	public String nuevaCita(Model model) {
		model.addAttribute("cita", new Cita());
		model.addAttribute("listaClientes", clienteServicio.findAll());
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		model.addAttribute("serviciosCantidad", Map.of());
		model.addAttribute("observaciones", "");
		return "citas/formularioCita";
	}

	@PostMapping("/inicioAdmin/citas/nueva/submit")
	public String submitNueva(@ModelAttribute("cita") Cita cita, @RequestParam("clienteId") Long clienteId,
			@RequestParam("observaciones") String observaciones, @RequestParam Map<String, String> params) {

		try {
			citaService.registrarCitaAdmin(cita, clienteId, params, observaciones);
			return "redirect:/inicioAdmin/citas";

		} catch (ConflictoFechaException | IllegalArgumentException e) {
			return "redirect:/inicioAdmin/citas/nueva?error=" + codificar(e.getMessage());
		}
	}

	// Editar (admin)

	@GetMapping("/inicioAdmin/citas/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {

		Optional<Cita> cita = citaService.findById(id);

		if (cita.isPresent()) {
			model.addAttribute("cita", cita.get());
			model.addAttribute("listaClientes", clienteServicio.findAll());
			model.addAttribute("listaServicios", serviciosServicio.findAll());
			model.addAttribute("serviciosCantidad", citaService.obtenerCantidadesServicios(cita.get()));
			model.addAttribute("observaciones", citaService.obtenerObservaciones(cita.get()));
			return "citas/formularioCita";
		}

		return "redirect:/inicioAdmin/citas";
	}

	@PostMapping("/inicioAdmin/citas/editar/submit")
	public String submitEditar(@ModelAttribute("cita") Cita cita, @RequestParam("clienteId") Long clienteId,
			@RequestParam("observaciones") String obs, @RequestParam Map<String, String> params) {

		try {
			citaService.editarCitaAdmin(cita.getCodigo(), clienteId, params, cita.getFecha(), obs);
		} catch (ConflictoFechaException | IllegalArgumentException e) {
			return "redirect:/inicioAdmin/citas/editar/" + cita.getCodigo() + "?error=" + codificar(e.getMessage());
		}

		return "redirect:/inicioAdmin/citas";
	}

	// Borrar

	@GetMapping("/inicioUsuario/citas/borrar/{id}")
	public String borrarCitaCliente(@PathVariable Long id, Principal principal) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		citaService.borrarCitaDeCliente(id, cliente.getId());

		return "redirect:/inicioUsuario/citas";
	}

	@GetMapping("/inicioAdmin/citas/borrar/{id}")
	public String borrar(@PathVariable("id") long id) {

		citaService.borrarCitaAdmin(id);

		return "redirect:/inicioAdmin/citas";
	}

	// Utilidades

	private String codificar(String mensaje) {
		return URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
	}
}
