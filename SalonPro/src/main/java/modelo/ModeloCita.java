package modelo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloCita {

	private long codigo;
	private LocalDateTime fecha;
	private double precioTotal;
}
