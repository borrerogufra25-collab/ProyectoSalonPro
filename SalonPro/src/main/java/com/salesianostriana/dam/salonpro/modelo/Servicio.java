package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Servicio {

	@Id
	@GeneratedValue
	private long id;

	private String nombre, img;
	private double precio;
	private Duration duracion;

	// Preguntar a Luismi ??
	@OneToMany
	private Cita cita;

	// Puede que haya que crear una clase más para relacionarlas (?)

}
