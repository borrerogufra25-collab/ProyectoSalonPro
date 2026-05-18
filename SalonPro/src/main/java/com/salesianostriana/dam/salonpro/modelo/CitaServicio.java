package com.salesianostriana.dam.salonpro.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CitaServicio {

	@Id
	@GeneratedValue
	private Long id;

	private String observaciones;

	@ManyToOne
	@JoinColumn(name = "id_cita")
	private Cita cita;

	@ManyToOne
	@JoinColumn(name = "id_servicio")
	private Servicio servicio;

}
