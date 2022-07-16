package com.app.estadistica.services;

public interface IEstadisticaService {

	public void crearEstadisticas(Integer idProyecto);

	public void aumentarVisualizacion(Integer idProyecto);

	public Integer verVisualizaciones(Integer idProyecto);

	public void obtenerEstadistica(Integer idProyecto, Integer formulario);

}
