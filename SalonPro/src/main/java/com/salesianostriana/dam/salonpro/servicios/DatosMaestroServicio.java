package com.salesianostriana.dam.salonpro.servicios;

import org.springframework.stereotype.Service;

import com.salesianostriana.dam.salonpro.modelo.DatosMaestro;
import com.salesianostriana.dam.salonpro.repositorios.DatosMaestroRepositorio;
import com.salesianostriana.dam.salonpro.serviciosBase.BaseServiciosImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatosMaestroServicio extends BaseServiciosImpl<DatosMaestro, Long, DatosMaestroRepositorio> {

}
