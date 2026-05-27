package com.salesianostriana.dam.salonpro.controladores;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.servicios.CarritoServicio;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CarritoControlador {

	private final CarritoServicio carritoServicio;
	private final ServiciosServicio serviciosServicio;

	// Listar

	@GetMapping("/inicioUsuario/servicios")
	public String listarServicios(Model model) {

		model.addAttribute("productos", serviciosServicio.findAll());
		model.addAttribute("carrito", carritoServicio.getProductsInCart());

		return "usuario/pedirServicios";
	}

	// Meter

	@GetMapping("/inicioUsuario/servicios/{id}")
	public String agregarServicio(@PathVariable Long id) {

		Servicio s = serviciosServicio.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Servicio no encontrado con ID: " + id));

		carritoServicio.addProducto(s);

		return "redirect:/inicioUsuario/servicios";
	}

	// Borrar

	@GetMapping("/carrito/borrar/{id}")
	public String borrarServicio(@PathVariable Long id) {

		carritoServicio.removeProductoPorId(id);

		return "redirect:/inicioUsuario/servicios";
	}

	// Borrar todo

	@GetMapping("/carrito/vaciar")
	public String vaciarCarrito() {

		carritoServicio.vaciarCarrito();

		return "redirect:/inicioUsuario/servicios";
	}

	// Para que no se borre

	@ModelAttribute("total_carrito")
	public Double totalCarrito() {
		return carritoServicio.calcularTotal();
	}

	// Tramitar

	@GetMapping("/carrito/tramitar")
	public String tramitar(Model model) {

		if (carritoServicio.getProductsInCart()
				.isEmpty()) {
			return "redirect:/inicioUsuario/servicios";
		}

		model.addAttribute("carrito", carritoServicio.getProductsInCart());
		model.addAttribute("total", carritoServicio.calcularTotal());

		return "ticket";
	}

}
