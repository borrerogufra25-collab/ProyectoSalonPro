package com.salesianostriana.dam.salonpro.controladores;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrincipalControlador {

	private final ClienteServicio clienteServicio;

	@GetMapping("/")
	public String principal() {
		return "principal";
	}

	@GetMapping("/inicioAdmin")
	public String inicioAdmin() {
		return "inicioAdmin";
	}

	@GetMapping("/inicioUsuario")
	public String inicioUsuario(Principal principal, Model model) {
		Optional<Cliente> cliente = clienteServicio.findByEmail(principal.getName());
		model.addAttribute("cliente", cliente.orElse(null));
		return "inicioUsuario";
	}

}
