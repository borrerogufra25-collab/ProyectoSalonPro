package com.salesianostriana.dam.salonpro.controladores;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.CitaServicio;
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
			citaService.registrarCita(cita, clienteId, servicioId, observaciones);
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
			return "citas/formularioCita";
		}
		return "redirect:/inicioAdmin/citas";
	}

	@PostMapping("/inicioAdmin/citas/editar/submit")
	public String submitEditar(@ModelAttribute("cita") Cita cita, @RequestParam("servicioId") Long servicioId,
			@RequestParam("clienteId") Long clienteId, @RequestParam("observaciones") String obs) {

		Cita citaDb = citaService.findById(cita.getCodigo())
				.orElse(null);

		if (citaDb != null) {
			Servicio nuevoServicio = serviciosServicio.findById(servicioId)
					.orElse(null);
			Cliente nuevoCliente = clienteServicio.findById(clienteId)
					.orElse(null);

			if (nuevoServicio != null && nuevoCliente != null) {
				citaDb.setFecha(cita.getFecha());
				citaDb.setCliente(nuevoCliente);
				citaDb.setPrecioTotal(nuevoServicio.getPrecio());

				if (!citaDb.getCitaServicios()
						.isEmpty()) {
					CitaServicio cs = citaDb.getCitaServicios()
							.get(0);
					cs.setServicio(nuevoServicio);
					cs.setObservaciones(obs);
				}

				citaService.edit(citaDb);
			}
		}
		return "redirect:/inicioAdmin/citas";
	}

	// Borrar

	@GetMapping("/inicioAdmin/citas/borrar/{id}")
	public String borrar(@PathVariable Long id) {
		citaService.deleteByID(id);
		return "redirect:/inicioAdmin/citas";
	}
}