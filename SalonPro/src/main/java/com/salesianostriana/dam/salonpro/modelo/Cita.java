package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
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
public class Cita {

	@Id
	@GeneratedValue
	private Long codigo;

	@FutureOrPresent(message = "La fecha no puede ser pasada")
	private LocalDateTime fecha;

	@Min(0)
	private double precioTotal;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(name = "fk_cita_cliente"))
	private Cliente cliente;

	@OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CitaServicio> citaServicios;
}
