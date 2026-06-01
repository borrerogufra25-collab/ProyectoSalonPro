package com.salesianostriana.dam.salonpro.controladores;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.salesianostriana.dam.salonpro.modelo.Servicio;
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

	@GetMapping("/inicioUsuario/citas/editar/{id}")
	public String editarCitaCliente(@PathVariable Long id, Principal principal, Model model) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		Cita cita = citaService.findById(id)
				.filter(c -> c.getCliente() != null && c.getCliente()
						.getId()
						.equals(cliente.getId()))
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		model.addAttribute("cliente", cliente);
		model.addAttribute("cita", cita);
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		model.addAttribute("serviciosCantidad", obtenerCantidadesServicios(cita));
		model.addAttribute("observaciones", obtenerObservaciones(cita));

		return "usuario/formularioCita";
	}

	@PostMapping("/inicioUsuario/citas/editar/submit")
	public String submitEditarCitaCliente(@RequestParam("citaId") Long citaId,
			@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha,
			@RequestParam("observaciones") String observaciones, @RequestParam Map<String, String> params,
			Principal principal) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		citaService.findById(citaId)
				.filter(c -> c.getCliente() != null && c.getCliente()
						.getId()
						.equals(cliente.getId()))
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		try {
			citaService.actualizarCita(citaId, cliente.getId(), obtenerServiciosDesdeFormulario(params), fecha,
					observaciones);
		} catch (ConflictoFechaException | IllegalArgumentException e) {
			return "redirect:/inicioUsuario/citas/editar/" + citaId + "?error=" + codificar(e.getMessage());
		}

		return "redirect:/inicioUsuario/citas";
	}

	@GetMapping("/inicioUsuario/citas/borrar/{id}")
	public String borrarCitaCliente(@PathVariable Long id, Principal principal) {

		Cliente cliente = clienteServicio.findByEmail(principal.getName())
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		citaService.findById(id)
				.filter(cita -> cita.getCliente() != null && cita.getCliente()
						.getId()
						.equals(cliente.getId()))
				.ifPresent(cita -> {
					cliente.getListaCitas()
							.remove(cita);
					citaService.delete(cita);
				});

		return "redirect:/inicioUsuario/citas";
	}

	// Crear

	@GetMapping("/inicioAdmin/citas/nueva")
	public String nuevaCita(Model model) {
		model.addAttribute("cita", new Cita());
		model.addAttribute("listaClientes", clienteServicio.findAll());
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		return "citas/formularioCita";
	}

	@PostMapping("/inicioAdmin/citas/nueva/submit")
	public String submitNueva(@ModelAttribute("cita") Cita cita, @RequestParam("servicioId") Long servicioId,
			@RequestParam("clienteId") Long clienteId, @RequestParam("observaciones") String observaciones) {
		try {
			Servicio servicio = serviciosServicio.findById(servicioId)
					.orElseThrow(() -> new NoSuchElementException("Servicio no encontrado"));
			Map<Servicio, Integer> servicios = new HashMap<>();
			servicios.put(servicio, 1);

			citaService.registrarCita(cita, clienteId, servicios, observaciones);
			return "redirect:/inicioAdmin/citas";
		} catch (ConflictoFechaException e) {
			return "redirect:/inicioAdmin/citas/nueva?error=conflicto";
		} catch (Exception e) {
			return "redirect:/inicioAdmin/citas/nueva?error=desconocido";
		}
	}

	// Editar

	@GetMapping("/inicioAdmin/citas/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Optional<Cita> cita = citaService.findById(id);
		if (cita.isPresent()) {
			model.addAttribute("cita", cita.get());
			model.addAttribute("listaClientes", clienteServicio.findAll());
			model.addAttribute("listaServicios", serviciosServicio.findAll());
			model.addAttribute("serviciosCantidad", obtenerCantidadesServicios(cita.get()));
			model.addAttribute("observaciones", obtenerObservaciones(cita.get()));
			return "citas/formularioCita";
		}
		return "redirect:/inicioAdmin/citas";
	}

	@PostMapping("/inicioAdmin/citas/editar/submit")
	public String submitEditar(@ModelAttribute("cita") Cita cita, @RequestParam("clienteId") Long clienteId,
			@RequestParam("observaciones") String obs, @RequestParam Map<String, String> params) {

		try {
			citaService.actualizarCita(cita.getCodigo(), clienteId, obtenerServiciosDesdeFormulario(params),
					cita.getFecha(), obs);
		} catch (ConflictoFechaException | IllegalArgumentException e) {
			return "redirect:/inicioAdmin/citas/editar/" + cita.getCodigo() + "?error=" + codificar(e.getMessage());
		}

		return "redirect:/inicioAdmin/citas";
	}

	// Borrar

	@GetMapping("/inicioAdmin/citas/borrar/{id}")
	public String borrar(@PathVariable("id") long id) {
		citaService.findById(id)
				.ifPresent(cita -> {
					if (cita.getCliente() != null) {
						cita.getCliente()
								.getListaCitas()
								.remove(cita);
					}
					citaService.delete(cita);
				});
		return "redirect:/inicioAdmin/citas";
	}

	private Map<Long, Integer> obtenerCantidadesServicios(Cita cita) {
		if (cita.getCitaServicios() == null) {
			return new HashMap<>();
		}

		return cita.getCitaServicios()
				.stream()
				.collect(Collectors.groupingBy(cs -> cs.getServicio()
						.getId(), Collectors.summingInt(cs -> 1)));
	}

	private String obtenerObservaciones(Cita cita) {
		if (cita.getCitaServicios() == null || cita.getCitaServicios()
				.isEmpty()) {
			return "";
		}

		return cita.getCitaServicios()
				.get(0)
				.getObservaciones();
	}

	private Map<Servicio, Integer> obtenerServiciosDesdeFormulario(Map<String, String> params) {
		Map<Servicio, Integer> servicios = new LinkedHashMap<>();

		params.forEach((clave, valor) -> {
			if (clave.startsWith("cantidadServicio_")) {
				int cantidad = parseCantidad(valor);
				if (cantidad > 0) {
					Long servicioId = Long.parseLong(clave.replace("cantidadServicio_", ""));
					Servicio servicio = serviciosServicio.findById(servicioId)
							.orElseThrow(() -> new NoSuchElementException("Servicio no encontrado"));
					servicios.put(servicio, cantidad);
				}
			}
		});

		return servicios;
	}

	private int parseCantidad(String valor) {
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private String codificar(String mensaje) {
		return URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
	}
}
