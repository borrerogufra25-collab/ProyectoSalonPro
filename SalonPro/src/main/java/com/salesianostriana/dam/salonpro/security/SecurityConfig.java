package com.salesianostriana.dam.salonpro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final RepartidorLogin repartidorLogin;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authz) -> authz.requestMatchers("/inicioAdmin/**")
				.hasRole("ADMIN")
				.requestMatchers("/inicioUsuario/**")
				.hasRole("USER")
				.requestMatchers("/", "/img/**", "/registro", "/inicioSesion", "/css/**", "/js/**", "/h2-console/**",
						"/favicon.ico")
				.permitAll()
				.anyRequest()
				.authenticated())
				.formLogin(form -> form.loginPage("/inicioSesion")
						.loginProcessingUrl("/login")
						.successHandler(repartidorLogin)
						.permitAll())
				.logout(logout -> logout.logoutSuccessUrl("/inicioSesion?logout")
						.permitAll())
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(f -> f.disable()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}