package modelo;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloCliente {

	@Id
	@GeneratedValue
	private long id;

	private String nombre, email, telefono;
	private LocalDate cumple;
	private int numCortes;

}
