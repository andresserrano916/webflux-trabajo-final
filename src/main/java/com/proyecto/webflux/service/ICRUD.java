package com.proyecto.webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T, ID> {

	public Flux<T> buscarTodo();
	
	public Mono<T> buscarPorId(ID id);
	
	public Mono<T> guardar(T documento);
	
	public Mono<T> actualizar(T documento);
	
	public Mono<Void> eliminarPorId(ID id);
}
