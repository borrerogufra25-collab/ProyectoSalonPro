package com.salesianostriana.dam.salonpro.servicios;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	private final ServiciosServicio serviciosServicio;

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

	public Cita obtenerCitaDeCliente(Long citaId, Long clienteId) {
		return findById(citaId)
				.filter(c -> c.getCliente() != null && c.getCliente()
						.getId()
						.equals(clienteId))
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));
	}

	// Validaciones Tiempo

	private boolean seSolapan(LocalDateTime inicioA, LocalDateTime finA, LocalDateTime inicioB, LocalDateTime finB) {
		return inicioA.isBefore(finB) && finA.isAfter(inicioB);
	}

	private List<Servicio> expandServicios(Map<Servicio, Integer> servicios) {
		return servicios.entrySet()
				.stream()
				.flatMap(entry -> IntStream.range(0, entry.getValue())
						.mapToObj(i -> entry.getKey()))
				.collect(Collectors.toList());
	}

	private long calcularDuracionTotal(Map<Servicio, Integer> servicios) {
		return expandServicios(servicios).stream()
				.mapToLong(s -> s.getDuracion()
						.toMinutes())
				.sum();
	}

	private void validarHorario(LocalDateTime inicio, Map<Servicio, Integer> servicios) {

		LocalTime apertura = LocalTime.of(9, 0);
		LocalTime cierre = LocalTime.of(20, 0);

		long minutosTotales = calcularDuracionTotal(servicios);
		LocalDateTime fin = inicio.plusMinutes(minutosTotales);

		if (inicio.toLocalTime()
				.isBefore(apertura)) {
			throw new ConflictoFechaException("No puedes reservar antes de las " + apertura);
		}

		if (fin.toLocalTime()
				.isAfter(cierre)) {
			throw new ConflictoFechaException("El servicio seleccionado termina después de las " + cierre);
		}
	}

	private void validarSolapamientos(LocalDateTime inicio, Map<Servicio, Integer> servicios) {
		validarSolapamientos(inicio, servicios, null);
	}

	private void validarSolapamientos(LocalDateTime inicio, Map<Servicio, Integer> servicios, Long citaIdIgnorada) {

		long duracionNueva = calcularDuracionTotal(servicios);
		LocalDateTime finNueva = inicio.plusMinutes(duracionNueva);

		boolean conflicto = this.findAll()
				.stream()
				.filter(c -> citaIdIgnorada == null || !c.getCodigo()
						.equals(citaIdIgnorada))
				.anyMatch(c -> {

					long duracionExistente = c.getCitaServicios()
							.stream()
							.mapToLong(cs -> cs.getServicio()
									.getDuracion()
									.toMinutes())
							.sum();

					LocalDateTime finExistente = c.getFecha()
							.plusMinutes(duracionExistente);

					return seSolapan(inicio, finNueva, c.getFecha(), finExistente);
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

		return cuponServicio.calcularPrecioConCupon(cupon, precioConCumple);
	}

	private double calcularPrecioFinalActualizacion(Cita cita, Cliente cliente, Map<Servicio, Integer> servicios) {

		double precioBase = servicios.entrySet()
				.stream()
				.mapToDouble(e -> e.getKey()
						.getPrecio() * e.getValue())
				.sum();

		double precioConCumple = clienteServicio.aplicarDescuentoCumple(cliente, precioBase);

		Cupon cupon = cuponServicio.buscarCuponUsadoEnCita(cita)
				.filter(c -> c.getCliente() != null && c.getCliente()
						.getId()
						.equals(cliente.getId()))
				.orElse(null);

		return cuponServicio.calcularPrecioConCupon(cupon, precioConCumple);
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

	@Transactional
	public void registrarCita(Cita cita, Long clienteId, Map<Servicio, Integer> servicios, String observaciones) {

		if (servicios == null || servicios.isEmpty()) {
			throw new IllegalArgumentException("No se han seleccionado servicios para la cita");
		}

		Cliente cliente = clienteServicio.findById(clienteId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

		cita.setCliente(cliente);

		validarHorario(cita.getFecha(), servicios);
		validarSolapamientos(cita.getFecha(), servicios);

		cita.setPrecioTotal(calcularPrecioFinal(cliente, servicios));
		cita.setCitaServicios(generarDetalles(cita, servicios, observaciones));

		this.save(cita);

		cuponServicio.buscarCuponDisponible(cliente)
				.ifPresent(c -> cuponServicio.marcarComoUsado(c, cita));

		clienteServicio.aumentarPelados(cliente);
	}

	// Actualizar cita

	@Transactional
	public void actualizarCita(Long citaId, Long clienteId, Map<Servicio, Integer> servicios, LocalDateTime fecha,
			String observaciones) {

		if (servicios == null || servicios.isEmpty()) {
			throw new IllegalArgumentException("No se han seleccionado servicios para la cita");
		}

		Cita cita = this.findById(citaId)
				.orElseThrow(() -> new EntityNotFoundException("Cita no encontrada"));

		Cliente cliente = clienteServicio.findById(clienteId)
				.orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

		if (fecha.isBefore(LocalDateTime.now())) {
			throw new ConflictoFechaException("La fecha seleccionada no puede ser pasada");
		}

		validarHorario(fecha, servicios);
		validarSolapamientos(fecha, servicios, citaId);

		cita.setFecha(fecha);
		cita.setCliente(cliente);
		cita.setPrecioTotal(calcularPrecioFinalActualizacion(cita, cliente, servicios));

		cita.getCitaServicios()
				.clear();
		cita.getCitaServicios()
				.addAll(generarDetalles(cita, servicios, observaciones));

		this.edit(cita);
	}

	// Editar cita (user)

	@Transactional
	public void editarCitaDeCliente(Long citaId, Long clienteId, Map<String, String> params, LocalDateTime fecha,
			String observaciones) {

		Cita cita = this.findById(citaId)
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		if (cita.getCliente() == null || !cita.getCliente()
				.getId()
				.equals(clienteId)) {
			throw new IllegalArgumentException("No tienes permiso para editar esta cita");
		}

		Map<Servicio, Integer> servicios = obtenerServiciosDesdeFormulario(params);

		actualizarCita(citaId, clienteId, servicios, fecha, observaciones);
	}

	// Editar cita (Admin)

	@Transactional
	public void editarCitaAdmin(Long citaId, Long clienteId, Map<String, String> params, LocalDateTime fecha,
			String observaciones) {

		this.findById(citaId)
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		clienteServicio.findById(clienteId)
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		Map<Servicio, Integer> servicios = obtenerServiciosDesdeFormulario(params);

		actualizarCita(citaId, clienteId, servicios, fecha, observaciones);
	}

	@Transactional
	public void registrarCitaAdmin(Cita cita, Long clienteId, Map<String, String> params, String observaciones) {
		clienteServicio.findById(clienteId)
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado"));

		Map<Servicio, Integer> servicios = obtenerServiciosDesdeFormulario(params);

		registrarCita(cita, clienteId, servicios, observaciones);
	}

	// Ayudas

	public Map<Long, Integer> obtenerCantidadesServicios(Cita cita) {
		if (cita.getCitaServicios() == null)
			return new HashMap<>();

		return cita.getCitaServicios()
				.stream()
				.collect(Collectors.groupingBy(cs -> cs.getServicio()
						.getId(), Collectors.summingInt(cs -> 1)));
	}

	public String obtenerObservaciones(Cita cita) {
		if (cita.getCitaServicios() == null || cita.getCitaServicios()
				.isEmpty())
			return "";
		return cita.getCitaServicios()
				.get(0)
				.getObservaciones();
	}

	public Map<Servicio, Integer> obtenerServiciosDesdeFormulario(Map<String, String> params) {

		Map<Servicio, Integer> servicios = new LinkedHashMap<>();

		params.forEach((clave, valor) -> {
			if (clave.startsWith("cantidadServicio_")) {
				int cantidad = parseCantidad(valor);
				if (cantidad > 0) {
					Long servicioId = Long.parseLong(clave.replace("cantidadServicio_", ""));
					Servicio servicio = serviciosServicio.findById(servicioId)
							.orElseThrow(() -> new NoSuchElementException("Servicio no encontrado"));
					servicios.put(servicio, cantidad);
				}
			}
		});

		return servicios;
	}

	private int parseCantidad(String valor) {
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	// Borrar citas

	@Transactional
	public void borrarCitaDeCliente(Long citaId, Long clienteId) {

		Cita cita = this.findById(citaId)
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		if (cita.getCliente() == null || !cita.getCliente()
				.getId()
				.equals(clienteId)) {
			throw new IllegalArgumentException("No tienes permiso para borrar esta cita");
		}

		cita.getCliente()
				.getListaCitas()
				.remove(cita);
		cuponServicio.liberarCuponDeCita(cita);
		this.delete(cita);
	}

	@Transactional
	public void borrarCitaAdmin(Long citaId) {

		Cita cita = this.findById(citaId)
				.orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));

		if (cita.getCliente() != null) {
			cita.getCliente()
					.getListaCitas()
					.remove(cita);
		}

		cuponServicio.liberarCuponDeCita(cita);
		this.delete(cita);
	}
}
