package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.excepciones.ConflictoFechaException;
import com.salesianostriana.dam.salonpro.modelo.Cita;
import com.salesianostriana.dam.salonpro.modelo.CitaServicio;
import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.modelo.Cupon;
import com.salesianostriana.dam.salonpro.modelo.Servicio;
import com.salesianostriana.dam.salonpro.repositorios.CitaRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaService extends BaseServiciosImpl<Cita, Long, CitaRepositorio> {

	private final ClienteServicio clienteServicio;
	private final CuponServicio cuponServicio;

	// Consultas

	public List<Cita> listarCitasDeCliente(Long clienteId) {
		return repository.findByClienteId(clienteId);
	}

	public List<Cita> listarCincoCitasMasCercanas() {
		return repository.findTop5ByFechaGreaterThanEqualOrderByFechaAsc(LocalDateTime.now());
	}

	public List<Object[]> listarServiciosPopulares() {
		return repository.findServiciosPopulares();
	}

	public List<Object[]> listarClientesConMasVisitas() {
		return repository.findClientesConMasVisitas();
	}

	// Tiempos (validaciones y calcular)

	private boolean seSolapan(LocalDateTime inicioA, LocalDateTime finA, LocalDateTime inicioB, LocalDateTime finB) {

		boolean solapan = inicioA.isBefore(finB) && finA.isAfter(inicioB);
		return solapan;
	}

	private List<Servicio> expandServicios(Map<Servicio, Integer> servicios) {

		List<Servicio> listaExpandida = servicios.entrySet()
				.stream()
				.flatMap(entry -> IntStream.range(0, entry.getValue())
						.mapToObj(i -> entry.getKey()))
				.collect(Collectors.toList());

		return listaExpandida;
	}

	private long calcularDuracionTotal(Map<Servicio, Integer> servicios) {

		long minutosTotales = expandServicios(servicios).stream()
				.mapToLong(s -> s.getDuracion()
						.toMinutes())
				.sum();

		return minutosTotales;
	}

	private void validarHorario(LocalDateTime inicio, Map<Servicio, Integer> servicios) {

		LocalTime apertura = LocalTime.of(9, 0);
		LocalTime cierre = LocalTime.of(20, 0);

		long minutosTotales = calcularDuracionTotal(servicios);
		LocalDateTime fin = inicio.plusMinutes(minutosTotales);

		boolean empiezaAntes = inicio.toLocalTime()
				.isBefore(apertura);
		boolean terminaDespues = fin.toLocalTime()
				.isAfter(cierre);

		if (empiezaAntes) {
			throw new ConflictoFechaException("No puedes reservar antes de las " + apertura);
		}

		if (terminaDespues) {
			throw new ConflictoFechaException("El servicio seleccionado termina después de las " + cierre);
		}
	}

	private void validarSolapamientos(LocalDateTime inicio, Map<Servicio, Integer> servicios) {

		long duracionNueva = calcularDuracionTotal(servicios);
		LocalDateTime finNueva = inicio.plusMinutes(duracionNueva);

		List<Cita> todas = this.findAll();

		boolean conflicto = todas.stream()
				.anyMatch(citaExistente -> {

					long duracionExistente = citaExistente.getCitaServicios()
							.stream()
							.mapToLong(cs -> cs.getServicio()
									.getDuracion()
									.toMinutes())
							.sum();

					LocalDateTime finExistente = citaExistente.getFecha()
							.plusMinutes(duracionExistente);

					return seSolapan(inicio, finNueva, citaExistente.getFecha(), finExistente);
				});

		if (conflicto) {
			throw new ConflictoFechaException("La cita se solapa con otra existente");
		}
	}

	// Calculeo

	private double calcularPrecioFinal(Cliente cliente, Map<Servicio, Integer> servicios) {

		double precioBase = servicios.entrySet()
				.stream()
				.mapToDouble(e -> e.getKey()
						.getPrecio() * e.getValue())
				.sum();

		double precioConCumple = clienteServicio.aplicarDescuentoCumple(cliente, precioBase);

		Cupon cupon = cuponServicio.buscarCuponDisponible(cliente)
				.orElse(null);

		double precioFinal = cuponServicio.calcularPrecioConCupon(cupon, precioConCumple);
		return precioFinal;
	}

	private List<CitaServicio> generarDetalles(Cita cita, Map<Servicio, Integer> servicios, String obs) {

		List<CitaServicio> detalles = new ArrayList<>();

		servicios.forEach((servicio, cantidad) -> IntStream.range(0, cantidad)
				.forEach(i -> detalles.add(CitaServicio.builder()
						.cita(cita)
						.servicio(servicio)
						.observaciones(obs)
						.build())));

		return detalles;
	}

	// Crear cita

	// @Transactional = Garantiza que si falla algo se vuelva a cargar y no guarde
	// nada
	@Transactional
	public void registrarCita(Cita cita, Long clienteId, Map<Servicio, Integer> servicios, String observaciones) {

		Cliente cliente;
		double precioFinal;
		List<CitaServicio> detalles;

		boolean sinServicios = (servicios == null || servicios.isEmpty());
		if (sinServicios) {
			throw new IllegalArgumentException("No se han seleccionado servicios para la cita");
		}

		cliente = clienteServicio.findById(clienteId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

		cita.setCliente(cliente);

		validarHorario(cita.getFecha(), servicios);
		validarSolapamientos(cita.getFecha(), servicios);

		precioFinal = calcularPrecioFinal(cliente, servicios);
		cita.setPrecioTotal(precioFinal);

		detalles = generarDetalles(cita, servicios, observaciones);
		cita.setCitaServicios(detalles);

		this.save(cita);

		cuponServicio.buscarCuponDisponible(cliente)
				.ifPresent(c -> cuponServicio.marcarComoUsado(c, cita));

		clienteServicio.aumentarPelados(cliente);
	}
}
