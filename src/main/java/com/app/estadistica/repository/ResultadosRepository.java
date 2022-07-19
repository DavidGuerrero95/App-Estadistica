package com.app.estadistica.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.app.estadistica.models.Resultados;

public interface ResultadosRepository extends MongoRepository<Resultados, String> {

	@RestResource(path = "exist-resultado-formulario-numeroPregunta\"")
	public Boolean existsByIdProyectoAndFormularioAndNumeroPregunta(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario, @Param("numeroPregunta") Integer numeroPregunta);

	@RestResource(path = "find-resultado-formulario-numeroPregunta")
	public Resultados findByIdProyectoAndFormularioAndNumeroPregunta(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario, @Param("numeroPregunta") Integer numeroPregunta);

	@RestResource(path = "find-resultado-formulario")
	public List<Resultados> findByIdProyectoAndFormulario(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario);

	@RestResource(path = "find-resultado-id")
	public Resultados findByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "exist-resultado")
	public Boolean existsByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "exist-resultado-formulario")
	public Boolean existsByIdProyectoAndFormulario(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario);

	@RestResource(path = "delete-id")
	public void deleteByIdProyecto(@Param("idProyecto") Integer idProyecto);

	@RestResource(path = "delete-id-formulario")
	public void deleteByIdProyectoAndFormulario(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario);

	@RestResource(path = "delete-resultado-formulario-numeroPregunta")
	public void deleteByIdProyectoAndFormularioAndNumeroPregunta(@Param("idProyecto") Integer idProyecto,
			@Param("formulario") Integer formulario, @Param("numeroPregunta") Integer numeroPregunta);

}
