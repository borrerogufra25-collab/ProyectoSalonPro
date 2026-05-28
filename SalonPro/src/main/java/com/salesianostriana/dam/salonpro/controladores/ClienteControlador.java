package com.salesianostriana.dam.salonpro.controladores;

import java.beans.Encoder;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClienteControlador {

	private final ClienteServicio clienteServicio;
	private final PasswordEncoder passwordEncoder;

	// Listar

	@GetMapping("/inicioAdmin/clientes")
	public String listarClientes(Model model) {

		model.addAttribute("listaClientes", clienteServicio.findAll());
		return "clientes/listarClientes";
	}

	// Crear

	@GetMapping("/inicioAdmin/clientes/nuevo")
	public String nuevoCliente(Model model) {

		model.addAttribute("nuevoCliente", new Cliente());

		return "clientes/formularioCliente";
	}

	@PostMapping("/inicioAdmin/clientes/nuevo/submit")
	public String submitCliente(@ModelAttribute("nuevoCliente") Cliente cliente) {
		cliente.setRole(UserRole.USER);
		cliente.setContrasenia(passwordEncoder.encode(cliente.getContrasenia()));
		clienteServicio.save(cliente);
		return "redirect:/inicioAdmin/clientes";
	}

	// Crear Login

	@GetMapping("/nuevoCliente")
	public String nuevoClienteLogin(Model model) {

		model.addAttribute("nuevoCliente", new Cliente());

		return "formularioLogin";
	}

	@PostMapping("/nuevoCliente/submit")
	public String submitClienteLogin(@ModelAttribute("nuevoCliente") Cliente cliente) {
		cliente.setRole(UserRole.USER);
		cliente.setContrasenia(passwordEncoder.encode(cliente.getContrasenia()));
		clienteServicio.save(cliente);
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
	public String submitFormularioEdicion(@ModelAttribute("cliente") Cliente c) {
		clienteServicio.edit(c);
		return "redirect:/inicioAdmin/clientes";

	}

	// Borrar

	@GetMapping("/inicioAdmin/clientes/borrar/{id}")
	public String borrar(@PathVariable("id") long id) {

		Optional<Cliente> cBorrar = clienteServicio.findById(id);
		if (cBorrar.isPresent()) {
			clienteServicio.delete(cBorrar.get());
			return "redirect:/inicioAdmin/clientes";
		}
		return "redirect:/inicioAdmin/clientes";
	}

}
