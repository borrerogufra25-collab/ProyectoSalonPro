package com.salesianostriana.dam.salonpro.excepciones;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.servicios.ClienteServicio;
import com.salesianostriana.dam.salonpro.servicios.ServiciosServicio;

@ControllerAdvice
public class GlobalExceptionControllerAdvice {

	private final ClienteServicio clienteServicio;
	private final ServiciosServicio serviciosServicio;

	public GlobalExceptionControllerAdvice(ClienteServicio clienteServicio, ServiciosServicio serviciosServicio) {
		super();
		this.clienteServicio = clienteServicio;
		this.serviciosServicio = serviciosServicio;
	}

	@ExceptionHandler(ConflictoFechaException.class)
	public String handleConflictoAgenda(ConflictoFechaException ex, Model model) {

		Cita errorCita = ex.getErrorCita();
		model.addAttribute("cita", errorCita);
		DataBinder binder = new DataBinder(errorCita, "cita");
		BindingResult bindingResult = binder.getBindingResult();
		bindingResult.rejectValue("cliente", "error.cita", ex.getMessage());
		model.addAttribute("org.springframework.validation.BindingResult.cita", bindingResult);
		model.addAttribute("cliente", clienteServicio.findAll());
		model.addAttribute("servicio", serviciosServicio.findAll());

		return "citas/formularioCita";

	}

}
