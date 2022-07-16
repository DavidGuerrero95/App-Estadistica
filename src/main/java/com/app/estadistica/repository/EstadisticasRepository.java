package com.app.estadistica.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.estadistica.models.Estadisticas;

public interface EstadisticasRepository extends MongoRepository<Estadisticas, String> {

	@RestResource(path = "find-proyecto")
	public Estadisticas findByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "exist-proyecto")
	public Boolean existsByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "delete-proyecto")
	public void deleteByIdProyecto(@Param("idProyecto") Integer idProyecto);

}
