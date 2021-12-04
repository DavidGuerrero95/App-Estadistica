package com.app.estadistica.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.estadistica.models.PreguntasRespuestas;

@FeignClient(name = "app-preguntasrespuestas")
public interface PreguntasRespuestasFeignClient {

	@GetMapping("/preguntasrespuestas/pregunta/respuesta/obtener/{nombre}")
	public PreguntasRespuestas getProyectosByNombre(@PathVariable("nombre") String nombre);

}
