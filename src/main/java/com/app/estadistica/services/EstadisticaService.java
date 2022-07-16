package com.app.estadistica.services;

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
	public void obtenerEstadistica(Integer idProyecto, Integer formulario) {
		if (cbFactory.create("estadistica").run(() -> rClient.respuestasProyectoExisten(idProyecto, formulario),
				er -> existsRespuestas(idProyecto, formulario, er))) {
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
			List<Resultados> listaResultados = rServices.obtenerResultados(idProyecto, formulario);
			e.setResultados(listaResultados);
			eRepository.save(e);
		}

	}

//  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //

	private Boolean existsRespuestas(Integer idProyecto, Integer formulario, Throwable er) {
		log.info(er.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Respuestas no esta disponible");
	}

	private Suscripciones obtenerSuscripciones(Integer idProyecto, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Suscripciones no esta disponible");
	}

	private Integer obtenerComentarios(Integer idProyecto, Throwable e) {
		log.info(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Suscripciones no esta disponible");
	}

}
