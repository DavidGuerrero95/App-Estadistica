package com.app.estadistica.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.estadistica.requests.Suscripciones;

@FeignClient(name = "app-subscripciones")
public interface SuscripcionesFeignClient {

	@GetMapping("/suscripciones/{idProyecto}/")
	public Suscripciones obtenerSuscripcionesNombre(@PathVariable("idProyecto") Integer idProyecto);

	@GetMapping("/suscripciones/proyecto/comentarios/{idProyecto}/")
	public Integer obtenerComentariosNombre(@PathVariable("idProyecto") Integer idProyecto);

}
