package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "servicio", cascade = CascadeType.REMOVE)
	private List<CitaServicio> citaServicios;

	// Prueba

	public Servicio(String nombre, double precio, Duration duracion) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.duracion = duracion;
	}

}
