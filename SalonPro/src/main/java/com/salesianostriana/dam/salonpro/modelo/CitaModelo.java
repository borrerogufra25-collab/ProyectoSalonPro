package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaModelo {

	@Id
	@GeneratedValue
	private long codigo;

	private LocalDateTime fecha;
	private double precioTotal;
}
