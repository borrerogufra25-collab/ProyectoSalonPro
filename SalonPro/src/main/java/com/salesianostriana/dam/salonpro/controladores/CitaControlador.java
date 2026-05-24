package com.salesianostriana.dam.salonpro.controladores;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.CitaServicio;
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

	@GetMapping("/inicioAdmin/citas")
	public String listar(Model model) {
		model.addAttribute("listaCitas", citaService.findAll());
		return "citas/listarCitas";
	}

	@GetMapping("/inicioAdmin/citas/nueva")
	public String nueva(Model model) {
		model.addAttribute("cita", new Cita());
		model.addAttribute("listaClientes", clienteServicio.findAll());
		model.addAttribute("listaServicios", serviciosServicio.findAll());
		return "citas/formularioCita";
	}

	@PostMapping("/inicioAdmin/citas/nueva/submit")
	public String submitNueva(@ModelAttribute("cita") Cita cita, @RequestParam("observaciones") String observaciones) {

		// TODO: no se que mierda pasa

		return "redirect:/inicioAdmin/citas";
	}

	@GetMapping("/inicioAdmin/citas/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {

		// TODO: no se que mierda pasa

	}

	@PostMapping("/inicioAdmin/citas/editar/submit")
	public String submitEditar(@ModelAttribute("cita") Cita cita, @RequestParam("observaciones") String obs) {

		// TODO: no se que mierda pasa

		return "redirect:/inicioAdmin/citas";
	}

	@GetMapping("/inicioAdmin/citas/borrar/{id}")
	public String borrar(@PathVariable Long id) {
		citaService.deleteByID(id);
		return "redirect:/inicioAdmin/citas";
	}
}