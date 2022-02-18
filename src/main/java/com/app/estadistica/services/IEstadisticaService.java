package com.app.estadistica.services;

import java.util.List;

import com.app.estadistica.models.PreguntasRespuestas;
import com.app.estadistica.request.Resultados;

public interface IEstadisticaService {

	public List<List<String>> tipoUnoCuatro(int i, PreguntasRespuestas p, Resultados resultados);
	
	public List<List<String>> tipoDos(int i, PreguntasRespuestas preguntasRespuestas, Resultados resultados);
	
	public List<List<String>> tipoTres(int i, PreguntasRespuestas preguntasRespuestas, Resultados resultados);

	public List<Resultados> obtenerPreguntasCreacion(PreguntasRespuestas p);
	
	public List<Double> tipoSeisPrimero(List<List<String>> list);

	public String tipoSeisSegundo(List<Double> list);


}
