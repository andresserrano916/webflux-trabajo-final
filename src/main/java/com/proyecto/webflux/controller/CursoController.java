package com.proyecto.webflux.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.webflux.model.Curso;
import com.proyecto.webflux.service.ICursoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cursos")
public class CursoController {
	
	private ICursoService cursoService;
	
	public CursoController(ICursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Curso>>> listar(){
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.cursoService.buscarTodo()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Curso>> buscarPorId(@PathVariable("id") String id){
		return this.cursoService.buscarPorId(id)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Curso>> guardar(@Valid @RequestBody Curso curso, ServerHttpRequest request){
		return this.cursoService.guardar(curso)
			.map(cursoDB -> ResponseEntity.created(URI.create(request
																	.getURI()
																	.toString()
																	.concat("/")
																	.concat(cursoDB.getId())
					))
					.contentType(MediaType.APPLICATION_JSON)
					.body(cursoDB));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Curso>> actualizar(@Valid @RequestBody Curso curso, @PathVariable("id") String id){
		return this.cursoService.buscarPorId(id)
				.flatMap(cursoDB -> {
					cursoDB.setEstado(curso.isEstado());
					cursoDB.setNombre(curso.getNombre());
					cursoDB.setSiglas(curso.getSiglas());
					return this.cursoService.actualizar(cursoDB);
				}).flatMap(cursoUpdated -> Mono.just(ResponseEntity.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(cursoUpdated)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminarPorId(@PathVariable("id") String id){
		return this.cursoService.buscarPorId(id)
				.flatMap(p -> {
					return this.cursoService.eliminarPorId(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
}
