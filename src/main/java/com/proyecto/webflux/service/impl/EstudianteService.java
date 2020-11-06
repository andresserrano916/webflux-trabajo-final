package com.proyecto.webflux.service.impl;

import org.springframework.stereotype.Service;

import com.proyecto.webflux.model.Estudiante;
import com.proyecto.webflux.repo.IDefineRepo;
import com.proyecto.webflux.repo.IEstudianteRepo;
import com.proyecto.webflux.service.IEstudianteService;

import reactor.core.publisher.Flux;

@Service
public class EstudianteService extends CRUD<Estudiante, String> implements IEstudianteService {

	private IEstudianteRepo estudianteRepo;
	
	public EstudianteService(IEstudianteRepo estudianteRepo) {
		this.estudianteRepo = estudianteRepo;
	}
	
	@Override
	public IDefineRepo<Estudiante, String> obtenerRepo() {
		return this.estudianteRepo;
	}

	@Override
	public Flux<Estudiante> buscarTodoOrdenEdadDesc() {
		return this.estudianteRepo.findAllByOrderByEdadDesc();
	}

}
