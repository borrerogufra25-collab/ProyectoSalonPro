package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrincipalControlador {

	@GetMapping("/")
	public String principal() {
		return "principal";
	}

	@GetMapping("/inicioAdmin")
	public String inicioAdmin() {
		return "inicioAdmin";
	}

	@GetMapping("/inicioUsuario/{id}")
	public String inicioUsuario(@PathVariable("id") long id) {
		return "inicioUsuario";
	}

}
