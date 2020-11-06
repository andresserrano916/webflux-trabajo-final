package com.proyecto.webflux.service;


import com.proyecto.webflux.model.Usuario;
import com.proyecto.webflux.security.User;

import reactor.core.publisher.Mono;

public interface IUsuarioService extends ICRUD<Usuario, String>{

	Mono<User> buscarPorUsuario(String usuario);
}
