package com.salesianostriana.dam.salonpro.excepciones;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	 	@ExceptionHandler(Exception.class)
	public String manejarExcepcionGeneral(Exception ex, Model model) {

		model.addAttribute("error", "Ha ocurrido un error inesperado.");
		model.addAttribute("detalle", ex.getMessage());

		return "error/general";
	}
}
