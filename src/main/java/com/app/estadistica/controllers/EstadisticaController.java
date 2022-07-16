package com.app.estadistica.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.app.estadistica.models.Estadisticas;
import com.app.estadistica.models.EstadisticasUsuarios;
import com.app.estadistica.repository.EstadisticasRepository;
import com.app.estadistica.repository.EstadisticasUsuarioRepository;
import com.app.estadistica.repository.ResultadosRepository;
import com.app.estadistica.services.IEstadisticaService;
import com.app.estadistica.services.IResultadosServices;
import com.app.estadistica.services.UserExcelExporter;

@RestController
public class EstadisticaController {

	@Autowired
	EstadisticasUsuarioRepository euRepository;

	@Autowired
	EstadisticasRepository eRepository;

	@Autowired
	ResultadosRepository rRepository;

	@Autowired
	IEstadisticaService eService;

	@Autowired
	IResultadosServices rServices;

//  ****************************	USUARIOS	***********************************  //

	// MICROSERVICIO USUARIOS -> CREAR
	@PostMapping("/estadisticas/usuarios/crear/")
	public Boolean crearUsuarioNotificaciones(@RequestParam("username") String username) throws IOException {
		try {
			EstadisticasUsuarios e = new EstadisticasUsuarios(username, new ArrayList<String>());
			euRepository.save(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la creacion, estadistica: " + e.getMessage());
		}
	}

	// MICROSERVICIO USUARIOS -> EDITAR
	@PutMapping("/estadisticas/usuarios/editar/{username}")
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

	// VER ESTADISTICAS
	@GetMapping("/estadisticas/usuarios/ver/estadisticas/{username}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<String> verEstadisticasUsuario(@PathVariable("username") String username) {
		EstadisticasUsuarios esta = euRepository.findByUsername(username);
		return esta.getHistorialParticipacion();
	}

	// MICROSERVICIO USUARIOS -> ELIMINAR
	@DeleteMapping("/estadisticas/usuarios/borrarEstadisticas/")
	public Boolean borrarEstadisticasUsuario(@RequestParam("username") String username) throws IOException {
		try {
			EstadisticasUsuarios e = euRepository.findByUsername(username);
			euRepository.delete(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error la eliminacion, estadistica: " + e.getMessage());
		}
	}

	// MICROSERVICIO USUARIOS -> ELIMINAR TODOS
	@DeleteMapping("/estadisticas/eliminar/all/usuarios/")
	public Boolean eliminarAllUsuario() throws IOException {
		try {
			euRepository.deleteAll();
			return true;
		} catch (Exception e) {
			throw new IOException("Error: " + e.getMessage());
		}
	}

//  ****************************	PROYECTOS	***********************************  //

	// MICROSERVICIO PROYECTOS -> CREAR
	@PostMapping("/estadisticas/crear/")
	public Boolean crearEstadistica(@RequestParam("idProyecto") Integer idProyecto) throws IOException {
		try {
			eService.crearEstadisticas(idProyecto);
			return true;
		} catch (Exception e) {
			throw new IOException("Error crear estadistica: " + e.getMessage());
		}
	}

	// AUMENTAR VISUALIZACION
	@PutMapping("/estadisticas/visualizaciones/aumentar/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public Boolean aumentarVisualizaciones(@PathVariable("idProyecto") Integer idProyecto) throws IOException {
		try {
			eService.aumentarVisualizacion(idProyecto);
			return true;
		} catch (Exception e) {
			throw new IOException("Error aumentar visualizaciones: " + e.getMessage());
		}
	}

	// MICROSERVICIO RESPUESTAS -> CALCULAR ESTADISTICA
	// MICROSERVICIO ESTADISTICA DASHBOARD -> CALCULAR ESTADISTICA
	@PutMapping("/estadisticas/{idProyecto}/{formulario}/")
	public Boolean obtenerEstadistica(@PathVariable("idProyecto") Integer idProyecto,
			@PathVariable("formulario") Integer formulario) throws IOException {
		try {
			if (eRepository.existsByIdProyecto(idProyecto)) {
				eService.obtenerEstadistica(idProyecto, formulario);
				return true;
			}
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El Proyecto no existe");
		} catch (Exception e) {
			throw new IOException("Error obtener estadistica " + e.getMessage());
		}
	}

	// MICROSERVICIO ESTADISTICA DASHBOARD -> OBTENER ESTADISTICA
	// VER ESTADISTICA
	@GetMapping("/estadisticas/proyecto/{idProyecto}/formulario/{formulario}")
	public Estadisticas verEstadistica(@PathVariable("idProyecto") Integer idProyecto,
			@PathVariable("formulario") Integer formulario) throws IOException {
		try {
			obtenerEstadistica(idProyecto, formulario);
			return eRepository.findByIdProyecto(idProyecto);
		} catch (Exception e) {
			throw new IOException("Error ver estadistica: " + e.getMessage());
		}
	}

	// VER VISUALIZACIONES
	@GetMapping("/estadisticas/visualizaciones/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public Integer verVisualizaciones(@PathVariable("idProyecto") Integer idProyecto) {
		if (eRepository.existsByIdProyecto(idProyecto)) {
			return eService.verVisualizaciones(idProyecto);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no existe");
	}

	// GENERAR EXCEL
	@GetMapping("/estadisticas/export/excel/{idProyecto}")
	@ResponseStatus(code = HttpStatus.OK)
	public void exportToExcel(@PathVariable("idProyecto") Integer idProyecto, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		Estadisticas listUsers = eRepository.findByIdProyecto(idProyecto);

		UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

		excelExporter.export(response);
	}

	// MICROSERVICIO PROYECTOS -> BORRAR TODA LA ESTADISTICA
	@DeleteMapping("/estadistica/proyecto/{idProyecto}")
	public Boolean borrarEstadisticas(@PathVariable("idProyecto") Integer idProyecto) throws IOException {
		try {
			Estadisticas e = eRepository.findByIdProyecto(idProyecto);
			if (rRepository.existsByIdProyecto(idProyecto))
				rRepository.deleteByIdProyecto(idProyecto);
			eRepository.delete(e);
			return true;
		} catch (Exception e) {
			throw new IOException("Error borrar estadistica " + e.getMessage());
		}
	}

//  ****************************	RESULTADOS	***********************************  //

	// MICROSERVICIO PREGUNTAS -> CREAR RESULTADOS
	@PostMapping("/resultados/crear/")
	public Boolean crearResultados(@RequestParam("idProyecto") Integer idProyecto,
			@RequestParam("formulario") Integer formulario, @RequestParam("numeroPregunta") Integer numeroPregunta,
			@RequestParam("pregunta") String pregunta, @RequestParam("tipoConsulta") Integer tipoConsulta,
			@RequestParam("opciones") List<String> opciones,
			@RequestParam("mensajeImpacto") List<String> mensajeImpacto) throws IOException {
		try {
			rServices.crearResultados(idProyecto, formulario, numeroPregunta, pregunta, tipoConsulta, opciones,
					mensajeImpacto);
			return true;
		} catch (Exception e) {
			throw new IOException("Error aumentar visualizaciones: " + e.getMessage());
		}
	}

}
