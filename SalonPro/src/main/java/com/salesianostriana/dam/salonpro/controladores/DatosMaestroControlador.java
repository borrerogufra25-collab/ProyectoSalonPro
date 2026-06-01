package com.salesianostriana.dam.salonpro.controladores;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.modelo.UserRole;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.DatosMaestroServicio;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DatosMaestroControlador {

	private final DatosMaestroServicio datosMaestroServicio;
	private final ClienteServicio clienteServicio;
	private final PasswordEncoder passwordEncoder;

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

	@PostMapping("/inicioAdmin/configuracion/admin-password")
	public String cambiarContraseniaAdmin(@RequestParam("nuevaContrasenia") String nuevaContrasenia,
			@RequestParam("confirmarContrasenia") String confirmarContrasenia, Principal principal) {

		if (nuevaContrasenia == null || nuevaContrasenia.isBlank()) {
			return "redirect:/inicioAdmin/configuracion?passwordVacia";
		}

		if (!nuevaContrasenia.equals(confirmarContrasenia)) {
			return "redirect:/inicioAdmin/configuracion?passwordNoCoincide";
		}

		if (principal == null) {
			return "redirect:/inicioAdmin/configuracion?passwordError";
		}

		Optional<Cliente> admin = clienteServicio.findByEmail(principal.getName());
		if (admin.isEmpty() || admin.get().getRole() != UserRole.ADMIN) {
			return "redirect:/inicioAdmin/configuracion?passwordError";
		}

		Cliente cliente = admin.get();
		cliente.setContrasenia(passwordEncoder.encode(nuevaContrasenia));
		clienteServicio.edit(cliente);

		return "redirect:/inicioAdmin/configuracion?passwordExito";
	}

}
