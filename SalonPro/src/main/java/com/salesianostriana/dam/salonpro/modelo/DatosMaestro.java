package com.salesianostriana.dam.salonpro.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	private double descuentoCumple;

}
