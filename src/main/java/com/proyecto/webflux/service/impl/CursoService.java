package com.proyecto.webflux.service.impl;

import org.springframework.stereotype.Service;

import com.proyecto.webflux.model.Curso;
import com.proyecto.webflux.repo.ICursoRepo;
import com.proyecto.webflux.repo.IDefineRepo;
import com.proyecto.webflux.service.ICursoService;

@Service
public class CursoService extends CRUD<Curso, String> implements ICursoService {

	private ICursoRepo cursoRepo;
	
	public CursoService(ICursoRepo cursoRepo) {
		this.cursoRepo = cursoRepo;
	}
	
	@Override
	public IDefineRepo<Curso, String> obtenerRepo() {
		return this.cursoRepo;
	}

}
