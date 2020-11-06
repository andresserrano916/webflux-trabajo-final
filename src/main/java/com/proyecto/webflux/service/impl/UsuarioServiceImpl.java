package com.proyecto.webflux.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.webflux.model.Usuario;
import com.proyecto.webflux.repo.IDefineRepo;
import com.proyecto.webflux.repo.IRolRepo;
import com.proyecto.webflux.repo.IUsuarioRepo;
import com.proyecto.webflux.security.User;
import com.proyecto.webflux.service.IUsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Clase S8
@Service
public class UsuarioServiceImpl extends CRUD<Usuario, String>  implements IUsuarioService{

	@Autowired
	private IUsuarioRepo repo;
	
	@Autowired
	private IRolRepo rolRepo;

	@Override
	public IDefineRepo<Usuario, String> obtenerRepo() {
		return repo;
	}

	@Override
	public Mono<User> buscarPorUsuario(String usuario) {
		Mono<Usuario> monoUsuario = repo.findOneByUsuario(usuario);
		
		List<String> roles = new ArrayList<>();
		
		return monoUsuario.flatMap(u -> {
			return Flux.fromIterable(u.getRoles())
					.flatMap(rol -> {
						return rolRepo.findById(rol.getId())
								.map(r -> {
									roles.add(r.getNombre());
									return r;
								});
					}).collectList().flatMap(list -> {
						u.setRoles(list);
						return Mono.just(u);
					});
		}).flatMap(u -> {
			return Mono.just(new User(u.getUsuario(), u.getClave(), u.getEstado(), roles));
		});
		
	}
	
}
