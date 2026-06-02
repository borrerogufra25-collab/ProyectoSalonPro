package com.salesianostriana.dam.salonpro.controladores;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.servicios.CitaService;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.CuponServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrincipalControlador {

	private final ClienteServicio clienteServicio;
	private final CitaService citaService;
	private final CuponServicio cuponServicio;

	@GetMapping("/")
	public String principal() {
		return "principal";
	}

	@GetMapping("/inicioAdmin")
	public String inicioAdmin(Model model) {
		model.addAttribute("citasProximas", citaService.listarCincoCitasMasCercanas());
		return "inicioAdmin";
	}

	@GetMapping("/inicioAdmin/estadisticas")
	public String estadisticas(Model model) {
		model.addAttribute("serviciosPopulares", citaService.listarServiciosPopulares());
		model.addAttribute("clientesConMasVisitas", citaService.listarClientesConMasVisitas());
		return "estadisticas/estadisticas";
	}

	@GetMapping("/inicioUsuario")
	public String inicioUsuario(Principal principal, Model model) {
		Optional<Cliente> cliente = clienteServicio.findByEmail(principal.getName());
		model.addAttribute("cliente", cliente.orElse(null));
		model.addAttribute("cuponesDisponibles", cliente.map(cuponServicio::listarCuponesDisponibles)
				.orElseGet(List::of));
		return "inicioUsuario";
	}

}
