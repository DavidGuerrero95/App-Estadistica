package com.app.estadistica.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.estadistica.models.PreguntasRespuestas;

public interface PreguntasRespuestasRepository extends MongoRepository<PreguntasRespuestas, String>{

	@RestResource(path = "buscar-name")
	public PreguntasRespuestas findByNombre(@Param("name") String nombre);
	
	@RestResource(path = "existNombre")
	public Boolean existsByNombre(@Param("nombre") String nombre);
	
}
