package com.app.estadistica.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.estadistica.models.Suscripciones;
import com.app.estadistica.request.Comentarios;

@FeignClient(name = "app-subscripciones")
public interface SuscripcionesFeignClient {

	@GetMapping("/suscripciones/obtener/nombre/{nombre}")
	public Suscripciones obtenerSuscripcionesNombre(@PathVariable("nombre") String nombre);
	
	@GetMapping("/suscripciones/obtener/comentarios/nombre/{nombre}")
	public List<Comentarios> obtenerComentariosNombre(@PathVariable("nombre") String nombre);
	
}
