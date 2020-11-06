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

import com.proyecto.webflux.model.Estudiante;
import com.proyecto.webflux.service.IEstudianteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {
	
	private IEstudianteService estudianteService;
	
	public EstudianteController(IEstudianteService estudianteService) {
		this.estudianteService = estudianteService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Estudiante>>> listar(){
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.estudianteService.buscarTodo()));
	}
	
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> buscarPorId(@PathVariable("id") String id){
		return this.estudianteService.buscarPorId(id)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/edad-orden-desc")
	public Mono<ResponseEntity<Flux<Estudiante>>> buscarTodoOrdenEdadDesc(){
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.estudianteService.buscarTodoOrdenEdadDesc()));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Estudiante>> guardar(@Valid @RequestBody Estudiante estudiante, ServerHttpRequest request){
		return this.estudianteService.guardar(estudiante)
			.map(estudianteDB -> ResponseEntity.created(URI.create(request
																	.getURI()
																	.toString()
																	.concat("/")
																	.concat(estudianteDB.getId())
					))
					.contentType(MediaType.APPLICATION_JSON)
					.body(estudianteDB));
	}
	

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> actualizar(@Valid @RequestBody Estudiante estudiante, @PathVariable("id") String id){
		return this.estudianteService.buscarPorId(id)
				.flatMap(estudianteDB -> {
					estudianteDB.setDni(estudiante.getDni());
					estudianteDB.setNombres(estudiante.getNombres());
					estudianteDB.setApellidos(estudiante.getApellidos());
					estudianteDB.setEdad(estudiante.getEdad());
					return this.estudianteService.actualizar(estudianteDB);
				}).flatMap(estudianteUpdated -> Mono.just(ResponseEntity.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(estudianteUpdated)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminarPorId(@PathVariable("id") String id){
		return this.estudianteService.buscarPorId(id)
				.flatMap(p -> {
					return this.estudianteService.eliminarPorId(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
}
