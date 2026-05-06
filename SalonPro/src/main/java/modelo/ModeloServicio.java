package modelo;

import java.time.Duration;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloServicio {

//Mirar este que poner

	private long id;

	private String nombre, img;
	private double precio;
	private Duration duracion;
<<<<<<< Updated upstream:SalonPro/src/main/java/modelo/ModeloServicio.java
	private ModeloCita mCita;
	private ModeloCliente mCliente;

	public ModeloServicio(ModeloCita mCita, ModeloCliente mCliente) {

		this.mCita = mCita;
		this.mCliente = mCliente;

	}
=======

	private CitaModelo mCita;
	private ClienteModelo mCliente;
>>>>>>> Stashed changes:SalonPro/src/main/java/com/salesianostriana/dam/salonpro/modelo/ServicioModelo.java

}
