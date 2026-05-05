package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioModelo {

	private long id;
	private String nombre, img;
	private double precio;
	private Duration duracion;
	private CitaModelo mCita;
	private ClienteModelo mCliente;

}
