package com.app.estadistica.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.estadistica.models.EstadisticasUsuarios;

public interface EstadisticasUsuarioRepository extends MongoRepository<EstadisticasUsuarios, String>{

	@RestResource(path = "find-user")
	public EstadisticasUsuarios findByUsername(@Param("username") String username);
	
	@RestResource(path = "exist-user")
	public Boolean existsByUsername(@Param("username") String username);
	
}
