package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

	@GetMapping("/inicioUsuario")
	public String inicioUsuario(Authentication auth, Model model) {
		String emailUsuario = auth.getName();
		return "inicioUsuario";
	}

}
