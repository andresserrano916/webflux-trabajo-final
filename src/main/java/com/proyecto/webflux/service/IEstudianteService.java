package com.proyecto.webflux.service;

import com.proyecto.webflux.model.Estudiante;

import reactor.core.publisher.Flux;

public interface IEstudianteService extends ICRUD<Estudiante, String>{

	public Flux<Estudiante> buscarTodoOrdenEdadDesc();
}
