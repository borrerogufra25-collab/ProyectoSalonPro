package com.salesianostriana.dam.salonpro.controladores;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClienteControlador {

	private final ClienteServicio clienteServicio;

	// Listar
	@GetMapping("/inicioAdmin/clientes")
	public String listarClientes(Model model) {
		model.addAttribute("listaClientes", clienteServicio.listarClientesUsuarios());
		return "clientes/listarClientes";
	}

	// Crear Admin
	@GetMapping("/inicioAdmin/clientes/nuevo")
	public String nuevoCliente(Model model) {
		model.addAttribute("nuevoCliente", new Cliente());
		return "clientes/formularioCliente";
	}

	@PostMapping("/inicioAdmin/clientes/nuevo/submit")
	public String submitCliente(@Valid @ModelAttribute("nuevoCliente") Cliente cliente, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "clientes/formularioCliente";
		}

		clienteServicio.registrarUsuario(cliente);
		return "redirect:/inicioAdmin/clientes";
	}

	// Crear Login
	@GetMapping("/nuevoCliente")
	public String nuevoClienteLogin(Model model) {
		model.addAttribute("nuevoCliente", new Cliente());
		return "formularioLogin";
	}

	@PostMapping("/nuevoCliente/submit")
	public String submitClienteLogin(@Valid @ModelAttribute("nuevoCliente") Cliente cliente,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "formularioLogin";
		}

		clienteServicio.registrarUsuario(cliente);
		return "redirect:/inicioSesion";
	}

	// Editar
	@GetMapping("/inicioAdmin/clientes/editar/{id}")
	public String formularioEdicion(@PathVariable("id") long id, Model model) {
		Optional<Cliente> cEditar = clienteServicio.findById(id);

		if (cEditar.isPresent()) {

			model.addAttribute("nuevoCliente", cEditar.get());
			return "clientes/formularioCliente";
		} else {
			return "redirect:/inicioAdmin/clientes";
		}
	}

	@PostMapping("/inicioAdmin/clientes/editar/submit")
	public String submitFormularioEdicion(@Valid @ModelAttribute("nuevoCliente") Cliente c,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "clientes/formularioCliente";
		}

		clienteServicio.editarCliente(c);
		return "redirect:/inicioAdmin/clientes";
	}

	// Borrar
	@GetMapping("/inicioAdmin/clientes/borrar/{id}")
	public String borrar(@PathVariable("id") long id) {
		clienteServicio.borrarClienteSiExiste(id);
		return "redirect:/inicioAdmin/clientes";
	}
}
