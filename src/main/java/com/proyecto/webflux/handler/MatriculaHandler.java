package com.proyecto.webflux.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyecto.webflux.model.Matricula;
import com.proyecto.webflux.service.IMatriculaService;
import com.proyecto.webflux.validator.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class MatriculaHandler {

	private IMatriculaService estudianteService;
	private RequestValidator validadorGeneral;
	
	@Autowired
	public MatriculaHandler(IMatriculaService estudianteService, RequestValidator validadorGeneral) {
		this.estudianteService = estudianteService;
		this.validadorGeneral = validadorGeneral;
	}
	
	public Mono<ServerResponse> buscarTodo(ServerRequest req){
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(estudianteService.buscarTodo(), Matricula.class);		
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
		Mono<Matricula> monoMatricula = req.bodyToMono(Matricula.class);
		
		return monoMatricula
				.flatMap(this.validadorGeneral::validate)
				.flatMap(estudianteService::guardar)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Matricula> monoMatricula = req.bodyToMono(Matricula.class);
		String id = req.pathVariable("id");
		
		return estudianteService.buscarPorId(id)
				.zipWith(monoMatricula, (monoDb, monoReq) -> {
					monoDb.setEstado(monoReq.isEstado());
					monoDb.setEstudiante(monoReq.getEstudiante());
					monoDb.setCursos(monoReq.getCursos());
					monoDb.setFecha(monoReq.getFecha());
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
