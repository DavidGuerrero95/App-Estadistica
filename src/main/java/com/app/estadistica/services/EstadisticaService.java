package com.app.estadistica.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.estadistica.clients.RespuestasFeignClient;
import com.app.estadistica.clients.SuscripcionesFeignClient;
import com.app.estadistica.models.Estadisticas;
import com.app.estadistica.models.Resultados;
import com.app.estadistica.repository.EstadisticasRepository;
import com.app.estadistica.repository.ResultadosRepository;
import com.app.estadistica.requests.Suscripciones;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstadisticaService implements IEstadisticaService {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	EstadisticasRepository eRepository;

	@Autowired
	ResultadosRepository rRepository;

	@Autowired
	SuscripcionesFeignClient sClient;

	@Autowired
	RespuestasFeignClient rClient;

	@Autowired
	IResultadosServices rServices;

	@Override
	public void crearEstadisticas(Integer idProyecto) {
		Estadisticas e = new Estadisticas();
		e.setIdProyecto(idProyecto);
		e.setVisualizaciones(0);
		e.setLikes(0);
		e.setDislikes(0);
		e.setNumeroSuscritas(0);
		e.setNumeroCuestionario(0);
		e.setNumeroComentarios(0);
		e.setResultados(new ArrayList<Resultados>());
		eRepository.save(e);
	}

	@Override
	public void aumentarVisualizacion(Integer idProyecto) {
		Estadisticas estadisticas = eRepository.findByIdProyecto(idProyecto);
		Integer visualizacion = estadisticas.getVisualizaciones();
		visualizacion++;
		estadisticas.setVisualizaciones(visualizacion);
		eRepository.save(estadisticas);
	}

	@Override
	public Integer verVisualizaciones(Integer idProyecto) {
		Estadisticas estadisticas = eRepository.findByIdProyecto(idProyecto);
		return estadisticas.getVisualizaciones();
	}

	@Override
	public void obtenerEstadisticaProyecto(Integer idProyecto) throws IOException, ResponseStatusException {
		if (eRepository.existsByIdProyecto(idProyecto)) {
			Estadisticas e = eRepository.findByIdProyecto(idProyecto);
			Suscripciones s = cbFactory.create("estadistica").run(() -> sClient.obtenerSuscripcionesNombre(idProyecto),
					er -> obtenerSuscripciones(idProyecto, er));
			Integer c = cbFactory.create("estadistica").run(() -> sClient.obtenerComentariosNombre(idProyecto),
					er -> obtenerComentarios(idProyecto, er));
			e.setNumeroSuscritas(s.getSuscripciones().size());
			e.setLikes(s.getLike().size());
			e.setDislikes(s.getDislike().size());
			e.setNumeroComentarios(c);
			e.setNumeroCuestionario(s.getCuestionarios().size());
			eRepository.save(e);
		} else {

		}

	}

	@Override
	public Estadisticas verEstadistica(Integer idProyecto, Integer formulario) {
		Estadisticas e = eRepository.findByIdProyecto(idProyecto);
		List<Resultados> r = rRepository.findByIdProyectoAndFormulario(idProyecto, formulario);
		e.setResultados(r);
		eRepository.save(e);
		return e;
	}

//  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //

	private Suscripciones obtenerSuscripciones(Integer idProyecto, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Suscripciones no esta disponible");
	}

	private Integer obtenerComentarios(Integer idProyecto, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Suscripciones no esta disponible");
	}

}
