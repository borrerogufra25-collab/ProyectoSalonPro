package com.salesianostriana.dam.salonpro.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;

@Controller
public class ClienteControlador {

	@GetMapping("/nuevoCliente")
	public String nuevoCliente(Model model) {

		Cliente cliente = new Cliente();
		model.addAttribute("clienteForm", cliente);

		return "cliente/formularioCliente";
	}

	@PostMapping("/addCliente")
	public String submitCliente(@ModelAttribute("clienteForm") Cliente cliente, Model model) {
		model.addAttribute("cliente", cliente);
		return "redirect:/nuevoCliente";
	}

}
