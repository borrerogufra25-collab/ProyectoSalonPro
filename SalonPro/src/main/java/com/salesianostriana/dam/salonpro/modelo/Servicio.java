package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Servicio {

	@Id
	@GeneratedValue
	private Long id;

	private String nombre, img;
	private double precio;

	@DateTimeFormat(iso = DateTimeFormat.ISO.NONE)
	private Duration duracion;

	// Prueba

	public Servicio(String nombre, double precio, Duration duracion) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.duracion = duracion;
	}

}
