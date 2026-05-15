package com.salesianostriana.dam.salonpro.controladores;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClienteControlador {

	private final ClienteServicio clienteServicio;

	@GetMapping("/nuevoCliente")
	public String nuevoCliente(Model model) {

		Cliente cliente = new Cliente();
		model.addAttribute("clienteForm", cliente);

		return "clientes/formularioCliente";
	}

	@PostMapping("/addCliente")
	public String submitCliente(@ModelAttribute("clienteForm") Cliente cliente, Model model) {
		model.addAttribute("cliente", cliente);
		return "redirect:/inicioAdmin";
	}

	@GetMapping("/clientes/listarClientes")
	public String listarClientes(Model model) {

		model.addAttribute("listaClientes", clienteServicio.findAll());
		return "clientes/listarClientes";
	}

}
