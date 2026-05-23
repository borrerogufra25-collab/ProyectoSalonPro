package com.salesianostriana.dam.salonpro.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RepartidorLogin implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Saca
		boolean esAdmin = authentication.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority()
						.equals("ROLE_ADMIN"));
		// Manda
		if (esAdmin) {
			response.sendRedirect("/inicioAdmin");
		} else {
			response.sendRedirect("/inicioUsuario");
		}
	}
}
