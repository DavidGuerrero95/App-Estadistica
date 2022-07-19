package com.app.estadistica.services;

import java.io.IOException;

import org.springframework.web.server.ResponseStatusException;

import com.app.estadistica.models.Estadisticas;

public interface IEstadisticaService {

	public void crearEstadisticas(Integer idProyecto);

	public void aumentarVisualizacion(Integer idProyecto);

	public Integer verVisualizaciones(Integer idProyecto);

	public void obtenerEstadisticaProyecto(Integer idProyecto) throws IOException, ResponseStatusException;

	public Estadisticas verEstadistica(Integer idProyecto, Integer formulario);

}
