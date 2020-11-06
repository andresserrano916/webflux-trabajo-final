package com.proyecto.webflux.service.impl;

import com.proyecto.webflux.repo.IDefineRepo;
import com.proyecto.webflux.service.ICRUD;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUD<T, ID> implements ICRUD<T, ID> {
	
	public abstract IDefineRepo<T, ID> obtenerRepo();

	@Override
	public Flux<T> buscarTodo() {
		return this.obtenerRepo().findAll();
	}

	@Override
	public Mono<T> buscarPorId(ID id) {
		return this.obtenerRepo().findById(id);
	}

	@Override
	public Mono<T> guardar(T documento) {
		return this.obtenerRepo().save(documento);
	}

	@Override
	public Mono<T> actualizar(T documento){
		return this.obtenerRepo().save(documento);
	}
	
	@Override
	public Mono<Void> eliminarPorId(ID id) {
		return this.obtenerRepo().deleteById(id);
	}

}
