package com.salesianostriana.dam.salonpro.excepciones;

import com.salesianostriana.dam.salonpro.modelo.Cita;

public class ConflictoFechaException extends RuntimeException {

	private final Cita errorCita;

	public ConflictoFechaException(Cita errorCita) {
		super();
		this.errorCita = errorCita;
	}

	public ConflictoFechaException(String mensaje) {
		super(mensaje);
		this.errorCita = null;
	}

	public Cita getErrorCita() {
		return errorCita;
	}

}
