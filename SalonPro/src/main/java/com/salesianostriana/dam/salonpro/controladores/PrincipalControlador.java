package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrincipalControlador {

	@GetMapping("/")
	public String principal() {
		return "principal";
	}

}
