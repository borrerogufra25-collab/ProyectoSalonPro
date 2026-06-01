package com.salesianostriana.dam.salonpro.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ServicioControlador {

	private final ServiciosServicio servicioServicio;

	// Listar
	@GetMapping("/inicioAdmin/servicios")
	public String listarServicios(Model model) {
		List<Servicio> servicios = servicioServicio.findAll();

		model.addAttribute("Servicios", servicios);
		model.addAttribute("serviciosConCitas", servicioServicio.obtenerMapaServiciosConCitas(servicios));
		return "servicios/listarServicios";
	}

	// Crear
	@GetMapping("/inicioAdmin/servicios/nuevo")
	public String nuevoServicio(Model model) {
		model.addAttribute("nuevoServicio", new Servicio());
		return "servicios/formularioServicios";
	}

	@PostMapping("/inicioAdmin/servicios/nuevo/submit")
	public String submitServicio(@Valid @ModelAttribute("nuevoServicio") Servicio servicio,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "servicios/formularioServicios";
		}
		servicioServicio.save(servicio);
		return "redirect:/inicioAdmin/servicios";
	}

	// Editar
	@GetMapping("/inicioAdmin/servicios/editar/{id}")
	public String formularioEdicion(@PathVariable("id") long id, Model model) {
		Optional<Servicio> sEditar = servicioServicio.findById(id);

		if (sEditar.isPresent()) {
			model.addAttribute("nuevoServicio", sEditar.get());
			return "servicios/formularioServicios";
		} else {
			return "redirect:/inicioAdmin/servicios";
		}
	}

	@PostMapping("/inicioAdmin/servicios/editar/submit")
	public String submitFormularioEdicion(@Valid @ModelAttribute("nuevoServicio") Servicio s,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "servicios/formularioServicios";
		}
		servicioServicio.edit(s);
		return "redirect:/inicioAdmin/servicios";
	}

	// Borrar
	@GetMapping("/inicioAdmin/servicios/borrar/{id}")
	public String borrar(@PathVariable("id") long id) {
		servicioServicio.borrarServicioYCitasAsociadasSiExiste(id);
		return "redirect:/inicioAdmin/servicios";
	}
}
