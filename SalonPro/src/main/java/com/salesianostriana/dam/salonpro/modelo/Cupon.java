package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Cupon {

	@Id
	@GeneratedValue
	private Long id;

	@Min(0)
	@DecimalMax("100.0")
	private double descuento;

	private boolean usado;

	private LocalDateTime fechaCreacion;

	private LocalDateTime fechaUso;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", foreignKey = @ForeignKey(name = "fk_cupon_cliente"))
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "codigo_cita", foreignKey = @ForeignKey(name = "fk_cupon_cita"))
	private Cita citaUso;
}
