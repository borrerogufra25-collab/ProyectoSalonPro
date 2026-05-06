package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioModelo {

//Mirar este que poner

	private long id;

	private String nombre, img;
	private double precio;
	private Duration duracion;

	private CitaModelo mCita;
	private ClienteModelo mCliente;

	public ServicioModelo(CitaModelo mCita, ClienteModelo mCliente) {

		this.mCita = mCita;
		this.mCliente = mCliente;

	}

}
