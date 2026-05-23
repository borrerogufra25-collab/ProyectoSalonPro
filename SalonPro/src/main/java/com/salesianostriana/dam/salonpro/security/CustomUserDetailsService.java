package com.salesianostriana.dam.salonpro.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.Cliente;
import com.salesianostriana.dam.salonpro.repositorios.ClienteRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final ClienteRepositorio clienteRepositorio;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Cliente cliente = clienteRepositorio.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con email: " + email));

		return User.builder()
				.username(cliente.getEmail())
				.password(cliente.getContrasenia())
				.roles(cliente.getRole()
						.name())
				.build();
	}
}