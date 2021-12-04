package com.app.estadistica.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.app.estadistica.clients.PreguntasRespuestasFeignClient;
import com.app.estadistica.clients.SuscripcionesFeignClient;
import com.app.estadistica.models.Estadisticas;
import com.app.estadistica.models.EstadisticasUsuarios;
import com.app.estadistica.models.PreguntasRespuestas;
import com.app.estadistica.models.Suscripciones;
import com.app.estadistica.repository.EstadisticasRepository;
import com.app.estadistica.repository.EstadisticasUsuarioRepository;
import com.app.estadistica.repository.PreguntasRespuestasRepository;
import com.app.estadistica.repository.SuscripcionesRepository;
import com.app.estadistica.request.Preguntas;
import com.app.estadistica.request.Resultados;
import com.app.estadistica.services.IEstadisticaService;
import com.app.estadistica.services.UserExcelExporter;

@RestController
public class EstadisticaController {

	private final Logger logger = LoggerFactory.getLogger(EstadisticaController.class);

	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	EstadisticasUsuarioRepository euRepository;

	@Autowired
	EstadisticasRepository eRepository;

	@Autowired
	IEstadisticaService eService;

	@Autowired
	PreguntasRespuestasFeignClient prFeignClient;

	@Autowired
	PreguntasRespuestasRepository prRepository;

	@Autowired
	SuscripcionesRepository sRepository;

	@Autowired
	SuscripcionesFeignClient sClient;

	@DeleteMapping("/estadistica/usuarios/borrarEstadisticas/")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean borrarEstadisticasUsuario(@RequestParam("username") String username) throws IOException {
		try {
			EstadisticasUsuarios e = euRepository.findByUsername(username);
			euRepository.delete(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la eliminacion, estadistica: " + e.getMessage());
		}

	}

	@PostMapping("/estadistica/usuarios/crear/")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean crearUsuarioNotificaciones(@RequestParam("username") String username) throws IOException {
		try {
			EstadisticasUsuarios e = new EstadisticasUsuarios(username, new ArrayList<String>());
			euRepository.save(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la creacion, estadistica: " + e.getMessage());
		}

	}

	@PutMapping("/estadistica/usuarios/editar/{username}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean editUser(@PathVariable("username") String username, @RequestParam("newUsername") String newUsername)
			throws IOException {
		try {
			EstadisticasUsuarios e = euRepository.findByUsername(newUsername);
			e.setUsername(newUsername);
			euRepository.save(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la edicion, estadistica: " + e.getMessage());
		}

	}

	@GetMapping("/estadistica/usuarios/ver/estadisticas/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<String> verEstadisticasUsuario(@PathVariable("username") String username) {
		EstadisticasUsuarios esta = euRepository.findByUsername(username);
		return esta.getHistorialParticipacion();
	}

	@GetMapping("/estadistica/ver/estadisticas/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Estadisticas verEstadistica(@PathVariable("nombre") String nombre) throws IOException {
		try {
			obtenerEstadistica(nombre);
			return eRepository.findByNombre(nombre);
		} catch (Exception e) {
			throw new IOException("Error ver estadistica: " + e.getMessage());
		}

	}

	@GetMapping("/estadistica/likes/ver/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Integer verLikes(@PathVariable("nombre") String nombre) {
		Estadisticas estadisticas = eRepository.findByNombre(nombre);
		return estadisticas.getLikes();
	}

	@GetMapping("/estadistica/dislikes/ver/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Integer verDislikes(@PathVariable("nombre") String nombre) {
		Estadisticas estadisticas = eRepository.findByNombre(nombre);
		return estadisticas.getDislikes();
	}

	@PostMapping("/estadistica/crear/")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Boolean crearEstadistica(@RequestParam("nombre") String nombre) throws IOException {
		try {
			Estadisticas e = new Estadisticas();
			PreguntasRespuestas p = cbFactory.create("estadistica").run(
					() -> prFeignClient.getProyectosByNombre(nombre), er -> obtenerPreguntasRespuestas(nombre, er));
			e.setNombre(nombre);
			e.setVisualizaciones(0);
			e.setLikes(0);
			e.setDislikes(0);
			e.setNumeroSuscritas(0);
			e.setNumeroCuestionario(0);
			e.setNumeroComentarios(0);
			List<Resultados> lResultados = eService.obtenerPreguntasCreacion(p);
			e.setResultados(lResultados);
			eRepository.save(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error crear estadistica: " + e.getMessage());
		}

	}

	@PutMapping("/estadistica/visualizaciones/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean aumentarVisualizaciones(@PathVariable("nombre") String nombre) throws IOException {
		try {
			Estadisticas estadisticas = eRepository.findByNombre(nombre);
			Integer visualizacion = estadisticas.getVisualizaciones();
			visualizacion++;
			estadisticas.setVisualizaciones(visualizacion);
			eRepository.save(estadisticas);
			return true;
		} catch (Exception e) {
			throw new IOException("Error aumentar visualizaciones: " + e.getMessage());
		}

	}

	@GetMapping("/estadistica/visualizacion/ver/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Integer verVisualizaciones(@PathVariable("nombre") String nombre) {
		Estadisticas estadisticas = eRepository.findByNombre(nombre);
		return estadisticas.getVisualizaciones();
	}

	@PutMapping("/estadistica/obtenerEstadistica/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean obtenerEstadistica(@PathVariable("nombre") String nombre) throws IOException {
		try {
			PreguntasRespuestas p = cbFactory.create("estadistica").run(
					() -> prFeignClient.getProyectosByNombre(nombre), er -> obtenerPreguntasRespuestas(nombre, er));
			Estadisticas e = eRepository.findByNombre(nombre);
			Suscripciones s = cbFactory.create("estadistica").run(() -> sClient.obtenerSuscripcionesNombre(nombre),
					er -> obtenerSuscripciones(nombre, er));
			e.setNumeroSuscritas(s.getSuscripciones().size());
			e.setLikes(s.getLike().size());
			e.setDislikes(s.getDislike().size());
			e.setNumeroComentarios(s.getComentarios().size());
			e.setNumeroCuestionario(s.getCuestionarios().size());
			List<Preguntas> preguntas = p.getPreguntas();
			List<Integer> tipoPregunta = new ArrayList<Integer>();
			List<String> enunciado = new ArrayList<String>();
			List<Resultados> listaResultados = new ArrayList<Resultados>();
			preguntas.forEach(i -> {
				tipoPregunta.add(i.getTipoConsulta());
				enunciado.add(i.getPregunta());
			});
			for (int i = 0; i < tipoPregunta.size(); i++) {
				Resultados resultados = new Resultados();
				resultados.setTipoConsulta(tipoPregunta.get(i));
				resultados.setNumeroPersonas(s.getCuestionarios().size());
				List<Double> promedioPonderado = new ArrayList<Double>();
				List<Double> promedio = new ArrayList<Double>();
				List<String> respuestas = new ArrayList<String>();
				List<Integer> personasOpcion = new ArrayList<Integer>();
				resultados.setEnunciado(enunciado.get(i));
				resultados.setNumeroPersonas(p.getRespuestas().get(i).size());
				resultados.setOpciones(p.getPreguntas().get(i).getOpciones());
				switch (tipoPregunta.get(i)) {
				case 1:
					List<List<String>> tipoUno = eService.tipoUnoCuatro(i, p, resultados);
					for (String d : tipoUno.get(0)) {
						promedioPonderado.add(Double.parseDouble(d));
					}
					resultados.setPromedioPonderado(promedioPonderado);
					resultados.setImpacto(tipoUno.get(1));
					resultados.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setMayorEscogida("-1");
					resultados.setMenorEscogida("-1");
					resultados.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
					resultados.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
					listaResultados.add(resultados);
					break;
				case 2:
					List<List<String>> tipoDos = eService.tipoDos(i, p, resultados);

					for (String d : tipoDos.get(0)) {
						promedio.add(Double.parseDouble(d));
					}
					resultados.setMenorEscogida(tipoDos.get(1).get(0));
					resultados.setMayorEscogida(tipoDos.get(1).get(1));
					resultados.setPromedio(promedio);
					resultados.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
					resultados.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
					resultados.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
					listaResultados.add(resultados);
					break;
				case 3:
					List<List<String>> tipoTres = eService.tipoTres(i, p, resultados);
					for (String d : tipoTres.get(0)) {
						personasOpcion.add(Integer.valueOf(d));
					}
					for (String d : tipoTres.get(2)) {
						promedio.add(Double.parseDouble(d));
					}

					resultados.setPersonasOpcion(personasOpcion);
					resultados.setRespuestas(tipoTres.get(1));
					resultados.setPromedio(promedio);
					resultados.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
					resultados.setMayorEscogida("-1");
					resultados.setMenorEscogida("-1");
					listaResultados.add(resultados);
					break;
				case 4:
					List<List<String>> tipoCuatro = eService.tipoUnoCuatro(i, p, resultados);
					for (String d : tipoCuatro.get(0)) {
						promedioPonderado.add(Double.parseDouble(d));
					}
					resultados.setPromedioPonderado(promedioPonderado);
					resultados.setImpacto(tipoCuatro.get(1));
					resultados.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setMayorEscogida("-1");
					resultados.setMenorEscogida("-1");
					resultados.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
					resultados.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
					listaResultados.add(resultados);
					break;
				case 5:
					for (int j = 0; j < p.getRespuestas().get(i).size(); j++) {
						respuestas.add(p.getRespuestas().get(i).get(j).get(0));
					}
					resultados.setRespuestas(respuestas);
					resultados.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
					resultados.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
					resultados.setMayorEscogida("-1");
					resultados.setMenorEscogida("-1");
					resultados.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
					listaResultados.add(resultados);
					break;
				default:
					break;

				}
			}
			e.setResultados(listaResultados);
			eRepository.save(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error obtener estadistica " + e.getMessage());
		}

	}

	@DeleteMapping("/estadistica/borrarEstadisticas/{nombre}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean borrarEstadisticas(@PathVariable("nombre") String nombre) throws IOException {
		try {
			Estadisticas e = eRepository.findByNombre(nombre);
			eRepository.delete(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error borrar estadistica " + e.getMessage());
		}

	}

	@GetMapping("/estadistica/export/excel/{nombre}")
	public void exportToExcel(@PathVariable String nombre, HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		Estadisticas listUsers = eRepository.findByNombre(nombre);

		UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

		excelExporter.export(response);
	}

	@PostMapping("/estadistica/preguntasrespuestas/crear/")
	public Boolean crearPreguntasRespuestas(@RequestBody PreguntasRespuestas pr) throws IOException {
		try {
			prRepository.save(pr);
			return true;
		} catch (Exception e) {
			throw new IOException("Error crear preguntasrespuestas estadistica " + e.getMessage());
		}

	}

	@PutMapping("/estadistica/preguntasrespuestas/editar/")
	public Boolean editarPreguntasRespuestas(@RequestBody PreguntasRespuestas pr) throws IOException {
		try {
			prRepository.save(pr);
			return true;
		} catch (Exception e) {
			throw new IOException("Error editar preguntasrespuestas estadistica " + e.getMessage());
		}

	}

	@PostMapping("/estadistica/suscripciones/crear/")
	public Boolean crearSuscripciones(@RequestBody Suscripciones pr) throws IOException {
		try {
			sRepository.save(pr);
			return true;
		} catch (Exception e) {
			throw new IOException("Error crear suscripciones, estadistica " + e.getMessage());
		}

	}

	@PutMapping("/estadistica/suscripciones/editar/")
	public Boolean editarSuscripciones(@RequestBody Suscripciones pr) throws IOException {
		try {
			sRepository.save(pr);
			return true;
		} catch (Exception e) {
			throw new IOException("Error editar suscripciones estadistica " + e.getMessage());
		}

	}

	private PreguntasRespuestas obtenerPreguntasRespuestas(String nombre, Throwable e) {
		logger.info(e.getMessage());
		return prRepository.findByNombre(nombre);
	}

	private Suscripciones obtenerSuscripciones(String nombre, Throwable e) {
		logger.info(e.getMessage());
		return sRepository.findByNombre(nombre);
	}

}
