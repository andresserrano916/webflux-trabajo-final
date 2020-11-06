package com.proyecto.webflux.service.impl;

import org.springframework.stereotype.Service;

import com.proyecto.webflux.model.Matricula;
import com.proyecto.webflux.repo.IDefineRepo;
import com.proyecto.webflux.repo.IMatriculaRepo;
import com.proyecto.webflux.service.IMatriculaService;

@Service
public class MatriculaService extends CRUD<Matricula, String> implements IMatriculaService {

	private IMatriculaRepo matriculaRepo;
	
	public MatriculaService(IMatriculaRepo matriculaRepo) {
		this.matriculaRepo = matriculaRepo;
	}
	
	@Override
	public IDefineRepo<Matricula, String> obtenerRepo() {
		return this.matriculaRepo;
	}

}
