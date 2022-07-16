package com.app.estadistica.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.estadistica.requests.Respuestas;

@FeignClient(name = "app-respuestas")
public interface RespuestasFeignClient {

	@GetMapping("/respuestas/proyecto/{idProyecto}/{formulario}/exists")
	public Boolean respuestasProyectoExisten(@PathVariable("idProyecto") Integer idProyecto,
			@PathVariable("formulario") Integer formulario);

	@GetMapping("/respuestas/ver/todas/pregunta/proyecto/{idProyecto}")
	public List<Respuestas> verRespuestasPreguntaProyecto(@PathVariable("idProyecto") Integer idProyecto,
			@RequestParam("numeroPregunta") Integer numeroPregunta, @RequestParam("formulario") Integer formulario);
}
