package com.salesianostriana.dam.salonpro.modelo;

import java.time.Duration;
import java.util.List;

import org.hibernate.validator.constraints.time.DurationMax;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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

	@Size(min = 1, max = 50, message = "Nombre demasiado largo")
	private String nombre;

	@PositiveOrZero(message = "El precio tiene que ser mayor o igual a 0")
	private double precio;

	@NotNull(message = "La duración es obligatoria y debe ser un número entero")
	@DurationMax(hours = 8, message = "La duración máxima no puede superar las 8 horas")
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
