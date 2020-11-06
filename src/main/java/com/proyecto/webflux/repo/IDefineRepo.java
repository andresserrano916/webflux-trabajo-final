package com.proyecto.webflux.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IDefineRepo<T, ID> extends ReactiveMongoRepository<T, ID>{

}
