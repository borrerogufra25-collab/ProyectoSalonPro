package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDate;
import java.util.List;

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
public class Cliente {

	@Id
	@GeneratedValue
	private long id;

	private String nombre, email, telefono;
	private LocalDate cumple;
	private int numCortes;

	@OneToMany
	private List<Cita> listaCitas;

}
