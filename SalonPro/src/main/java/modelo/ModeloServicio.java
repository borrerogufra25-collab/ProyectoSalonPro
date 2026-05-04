package modelo;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloServicio {

	private long id;
	private String nombre, img;
	private double precio;
	private Duration duracion;
	private ModeloCita mCita;
	private ModeloCliente mCliente;

	public ModeloServicio(ModeloCita mCita, ModeloCliente mCliente) {

		this.mCita = mCita;
		this.mCliente = mCliente;

	}

}
