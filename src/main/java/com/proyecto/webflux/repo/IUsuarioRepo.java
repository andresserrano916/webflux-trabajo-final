package com.proyecto.webflux.repo;

import com.proyecto.webflux.model.Usuario;

import reactor.core.publisher.Mono;

public interface IUsuarioRepo extends IDefineRepo<Usuario, String>{

	Mono<Usuario> findOneByUsuario(String usuario);
}
