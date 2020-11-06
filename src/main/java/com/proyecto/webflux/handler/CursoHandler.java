package com.proyecto.webflux.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyecto.webflux.model.Curso;
import com.proyecto.webflux.service.ICursoService;
import com.proyecto.webflux.validator.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

	private ICursoService estudianteService;
	private RequestValidator validadorGeneral;
	
	@Autowired
	public CursoHandler(ICursoService estudianteService, RequestValidator validadorGeneral) {
		this.estudianteService = estudianteService;
		this.validadorGeneral = validadorGeneral;
	}
	
	public Mono<ServerResponse> buscarTodo(ServerRequest req){
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(estudianteService.buscarTodo(), Curso.class);		
	}
	
	public Mono<ServerResponse> buscarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		
		return estudianteService.buscarPorId(id)
				.flatMap(p -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				).switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	public Mono<ServerResponse> guardar(ServerRequest req){
		Mono<Curso> monoCurso = req.bodyToMono(Curso.class);
		
		return monoCurso
				.flatMap(this.validadorGeneral::validate)
				.flatMap(estudianteService::guardar)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Curso> monoCurso = req.bodyToMono(Curso.class);
		String id = req.pathVariable("id");
		
		return estudianteService.buscarPorId(id)
				.zipWith(monoCurso, (monoDb, monoReq) -> {
					monoDb.setEstado(monoReq.isEstado());
					monoDb.setNombre(monoReq.getNombre());
					monoDb.setSiglas(monoReq.getSiglas());
					return monoDb;
				})
				.flatMap(this.validadorGeneral::validate)
				.flatMap(estudianteService::actualizar)
				.flatMap(p -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				).switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		
		return estudianteService.buscarPorId(id)
				.flatMap(p -> estudianteService.eliminarPorId(p.getId())
							.then(ServerResponse.noContent().build())
				).switchIfEmpty(ServerResponse.notFound().build());
	}
}
