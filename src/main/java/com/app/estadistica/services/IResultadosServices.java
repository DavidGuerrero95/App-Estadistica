package com.app.estadistica.services;

import java.util.List;

import com.app.estadistica.models.Resultados;

public interface IResultadosServices {

	void crearResultados(Integer idProyecto, Integer formulario, Integer numeroPregunta, String pregunta,
			Integer tipoConsulta, List<String> opciones, List<String> mensajeImpacto);

	List<Resultados> obtenerResultados(Integer idProyecto, Integer formulario);

}
