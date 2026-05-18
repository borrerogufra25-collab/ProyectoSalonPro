package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Cliente {

	@Id
	@GeneratedValue
	private Long id;

	private String nombre, email, telefono;
	private int numCortes;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate cumple;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Cita> listaCitas;

	public Cliente(String nombre, String email, String telefono, LocalDate cumple) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.cumple = cumple;
	}

}
