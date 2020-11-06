package com.proyecto.webflux.repo;

import com.proyecto.webflux.model.Estudiante;

import reactor.core.publisher.Flux;

public interface IEstudianteRepo extends IDefineRepo<Estudiante, String>{

	public Flux<Estudiante> findAllByOrderByEdadDesc();
}
