package modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloCliente {

	private long id;
	private String nombre, email, telefono;
	private LocalDate cumple;
	private int numCortes;

}
