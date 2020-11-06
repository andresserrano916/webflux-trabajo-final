package com.proyecto.webflux.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyecto.webflux.model.Estudiante;
import com.proyecto.webflux.service.IEstudianteService;
import com.proyecto.webflux.validator.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class EstudianteHandler {

	private IEstudianteService estudianteService;
	private RequestValidator validadorGeneral;
	
	@Autowired
	public EstudianteHandler(IEstudianteService estudianteService, RequestValidator validadorGeneral) {
		this.estudianteService = estudianteService;
		this.validadorGeneral = validadorGeneral;
	}
	
	public Mono<ServerResponse> buscarTodo(ServerRequest req){
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(estudianteService.buscarTodo(), Estudiante.class);		
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
	
	public Mono<ServerResponse> buscarTodoOrdenEdadDesc(ServerRequest req){
		return ServerResponse
			.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(estudianteService.buscarTodoOrdenEdadDesc(), Estudiante.class);		
	}
	
	public Mono<ServerResponse> guardar(ServerRequest req){
		Mono<Estudiante> monoEstudiante = req.bodyToMono(Estudiante.class);
		
		return monoEstudiante
				.flatMap(this.validadorGeneral::validate)
				.flatMap(estudianteService::guardar)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req){
		Mono<Estudiante> monoEstudiante = req.bodyToMono(Estudiante.class);
		String id = req.pathVariable("id");
		
		return estudianteService.buscarPorId(id)
				.zipWith(monoEstudiante, (monoDb, monoReq) -> {
					monoDb.setNombres(monoReq.getNombres());
					monoDb.setApellidos(monoReq.getApellidos());
					monoDb.setEdad(monoReq.getEdad());
					monoDb.setDni(monoReq.getDni());
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
