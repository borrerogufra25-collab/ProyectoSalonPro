package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.servicios.CitaServicio;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CitaControlador {

	private final CitaServicio citaServicio;
	private final ClienteServicio clienteServicio;
	private final ServiciosServicio serviciosServicio;

	// Listar

	@GetMapping("/inicioAdmin/citas")
	public String listar(Model model) {
		model.addAttribute("listaCitas", citaServicio.findAll());
		return "citas/listarCitas";
	}

	// Crear

	@GetMapping("/inicioAdmin/citas/nueva")
	public String nueva(Model model) {
		model.addAttribute("cita", new Cita());
		model.addAttribute("listaClientes", clienteServicio.findAll());
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		return "citas/formularioCita";
	}

	@PostMapping("/inicioAdmin/citas/nueva/submit")
	public String submitNueva(@ModelAttribute("cita") Cita cita) {
		citaServicio.save(cita);
		return "redirect:/inicioAdmin/citas";
	}

	// Editar

	@GetMapping("/inicioAdmin/citas/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		Cita cita = citaServicio.findById(id)
				.orElseThrow();
		model.addAttribute("cita", cita);
		model.addAttribute("listaClientes", clienteServicio.findAll());
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		return "citas/formularioCita";
	}

	@PostMapping("/inicioAdmin/citas/editar/submit")
	public String submitEditar(@ModelAttribute("cita") Cita cita) {
		citaServicio.edit(cita);
		return "redirect:/inicioAdmin/citas";
	}

	// TODO: no funciona
	// Borrar

	@GetMapping("/inicioAdmin/citas/borrar/{id}")
	public String borrar(@PathVariable Long id) {
		citaServicio.deleteByID(id);
		return "redirect:/inicioAdmin/citas";
	}
}
