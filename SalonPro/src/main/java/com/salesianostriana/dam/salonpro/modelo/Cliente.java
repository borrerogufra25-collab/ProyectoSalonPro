package com.salesianostriana.dam.salonpro.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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
public class Cliente {

	@Id
	@GeneratedValue
	private Long id;

	@Size(min = 1, max = 50, message = "Nombre demasiado largo")
	private String nombre;

	@NotNull(message = "Introduzca un correo electrónico")
	@Email(message = "Formato del correo incorrecto")
	private String email;

	@Pattern(regexp = "^\\d{9}$")
	private String telefono;

	@NotEmpty
	private String contrasenia;

	@PositiveOrZero
	private int numCortes;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PastOrPresent(message = "La fecha debe ser anterior o igual a la fecha actual")
	private LocalDate cumple;

	@Enumerated(EnumType.STRING)
	private UserRole role;

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
