package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cita {

	@Id
	@GeneratedValue
	private long codigo;

	private LocalDateTime fecha;
	private double precioTotal;

	@ManyToOne
	private Cliente cliente;

	// Preguntar a Luismi ??
	@OneToMany
	private Servicio servicio;
}
