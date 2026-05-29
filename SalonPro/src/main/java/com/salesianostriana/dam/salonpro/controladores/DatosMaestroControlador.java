package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.servicios.DatosMaestroServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DatosMaestroControlador {

	private final DatosMaestroServicio datosMaestroServicio;

	@GetMapping("/inicioAdmin/configuracion")
	public String configuracion(Model model) {
		model.addAttribute("datosMaestro", datosMaestroServicio.obtenerConfiguracion());
		return "datosMaestro/formularioDatosMaestro";
	}

	@PostMapping("/inicioAdmin/configuracion/submit")
	public String submitConfiguracion(@Valid @ModelAttribute("datosMaestro") DatosMaestro datosMaestro,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "datosMaestro/formularioDatosMaestro";
		}

		datosMaestroServicio.edit(datosMaestro);
		return "redirect:/inicioAdmin/configuracion?exito";
	}

}
