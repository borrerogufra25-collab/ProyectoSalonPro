package com.salesianostriana.dam.salonpro.serviciosBase;

import java.util.List;
import java.util.Optional;

import com.salesianostriana.dam.salonpro.modelo.Cliente;

public interface BaseServicios<T, ID> {

	List<T> findAll();

	Optional<T> findById(ID id);

	T save(T t);

	T edit(T t);

	void delete(T t);

	void deleteByID(T t);

	List<Cliente> obtenerCumple();

	boolean esCumple(Cliente cliente);

}
