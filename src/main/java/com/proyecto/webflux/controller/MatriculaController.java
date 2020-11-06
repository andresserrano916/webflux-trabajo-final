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

import com.proyecto.webflux.model.Matricula;
import com.proyecto.webflux.service.IMatriculaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {
	
	private IMatriculaService matriculaService;
	
	public MatriculaController(IMatriculaService matriculaService) {
		this.matriculaService = matriculaService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Matricula>>> listar(){
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(this.matriculaService.buscarTodo()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Matricula>> buscarPorId(@PathVariable("id") String id){
		return this.matriculaService.buscarPorId(id)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Matricula>> guardar(@Valid @RequestBody Matricula matricula, ServerHttpRequest request){
		return this.matriculaService.guardar(matricula)
			.map(matriculaDB -> ResponseEntity.created(URI.create(request
																	.getURI()
																	.toString()
																	.concat("/")
																	.concat(matriculaDB.getId())
					))
					.contentType(MediaType.APPLICATION_JSON)
					.body(matriculaDB));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Matricula>> actualizar(@Valid @RequestBody Matricula matricula, @PathVariable("id") String id){
		return this.matriculaService.buscarPorId(id)
				.flatMap(matriculaDB -> {
					matriculaDB.setCursos(matricula.getCursos());
					matriculaDB.setEstado(matricula.isEstado());
					matriculaDB.setEstudiante(matricula.getEstudiante());
					matriculaDB.setFecha(matricula.getFecha());
					return this.matriculaService.actualizar(matriculaDB);
				}).flatMap(matriculaUpdated -> Mono.just(ResponseEntity.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(matriculaUpdated)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminarPorId(@PathVariable("id") String id){
		return this.matriculaService.buscarPorId(id)
				.flatMap(p -> {
					return this.matriculaService.eliminarPorId(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
}
