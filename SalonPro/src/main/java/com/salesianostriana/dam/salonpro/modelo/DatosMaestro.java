package com.salesianostriana.dam.salonpro.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DatosMaestro {

	@Id
	@GeneratedValue
	private Long id;

	@Min(0)
	@DecimalMax("100.0")
	private double descuentoCumple;

	@Min(1)
	private int cortesPorPunto;

	@Min(1)
	private int puntosParaCupon;

	@Min(0)
	@DecimalMax("100.0")
	private double descuentoCupon;

}
