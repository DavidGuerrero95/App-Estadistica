package com.app.estadistica.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.estadistica.models.Estadisticas;

public interface EstadisticasRepository extends MongoRepository<Estadisticas, String>{

	@RestResource(path = "find-name")
	public Estadisticas findByNombre(@Param("nombre") String nombre);
	
	@RestResource(path = "existNombre")
	public Boolean existsByNombre(@Param("nombre") String nombre);
	
}
