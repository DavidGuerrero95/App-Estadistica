package com.app.estadistica.services;

import java.util.List;

public interface IResultadosServices {

	void crearResultados(Integer idProyecto, Integer formulario, Integer numeroPregunta, String pregunta,
			Integer tipoConsulta, List<String> opciones, List<String> mensajeImpacto);

	void obtenerEstadisticaResultados(Integer idProyecto, Integer formulario, Integer numeroPregunta);

}
