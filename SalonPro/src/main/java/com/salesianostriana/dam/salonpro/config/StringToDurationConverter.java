package com.salesianostriana.dam.salonpro.config;

import java.time.Duration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDurationConverter implements Converter<String, Duration> {

	@Override
	public Duration convert(String source) {
		if (source == null || source.trim()
				.isEmpty()) {
			return null;
		}
		try {

			long minutos = Long.parseLong(source.trim());
			return Duration.ofMinutes(minutos);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
