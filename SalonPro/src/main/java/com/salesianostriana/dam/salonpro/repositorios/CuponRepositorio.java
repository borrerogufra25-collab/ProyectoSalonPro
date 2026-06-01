package com.salesianostriana.dam.salonpro.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.salonpro.modelo.Cupon;

@Repository
public interface CuponRepositorio extends JpaRepository<Cupon, Long> {

	Optional<Cupon> findFirstByClienteIdAndUsadoFalseOrderByFechaCreacionAsc(Long clienteId);

	Optional<Cupon> findFirstByCitaUsoCodigo(Long citaId);

	List<Cupon> findByCitaUsoCodigo(Long citaId);

	List<Cupon> findByClienteIdAndUsadoFalseOrderByFechaCreacionAsc(Long clienteId);
}
