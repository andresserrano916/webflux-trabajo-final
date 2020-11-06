package com.proyecto.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

import com.proyecto.webflux.handler.CursoHandler;
import com.proyecto.webflux.handler.EstudianteHandler;
import com.proyecto.webflux.handler.MatriculaHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> rutasEstudiantes(EstudianteHandler estudianteHandler){
		return nest(path("/v2/estudiantes"), route(GET(""), estudianteHandler::buscarTodo)
				.andRoute(GET("/edad-orden-desc"), estudianteHandler::buscarTodoOrdenEdadDesc)
				.andRoute(GET("/{id}"), estudianteHandler::buscarPorId)
				.andRoute(POST(""), estudianteHandler::guardar)
				.andRoute(PUT("/{id}"), estudianteHandler::modificar)
				.andRoute(DELETE("/{id}"), estudianteHandler::eliminarPorId));
				
	}
	
	@Bean
	public RouterFunction<ServerResponse> rutasCursos(CursoHandler cursoHandler){
		return nest(path("/v2/cursos"), route(GET(""), cursoHandler::buscarTodo)
				.andRoute(GET("/{id}"), cursoHandler::buscarPorId)
				.andRoute(POST(""), cursoHandler::guardar)
				.andRoute(PUT("/{id}"), cursoHandler::modificar)
				.andRoute(DELETE("/{id}"), cursoHandler::eliminarPorId));
	}
	
	@Bean
	public RouterFunction<ServerResponse> rutasMatriculas(MatriculaHandler matriculaHandler){
		return nest(path("/v2/matriculas"), route(GET(""), matriculaHandler::buscarTodo)
				.andRoute(GET("/{id}"), matriculaHandler::buscarPorId)
				.andRoute(POST(""), matriculaHandler::guardar)
				.andRoute(PUT("/{id}"), matriculaHandler::modificar)
				.andRoute(DELETE("/{id}"), matriculaHandler::eliminarPorId));
	}
}
