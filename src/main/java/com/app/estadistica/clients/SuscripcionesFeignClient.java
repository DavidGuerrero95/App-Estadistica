package com.app.estadistica.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.estadistica.models.Suscripciones;

@FeignClient(name = "app-subscripciones")
public interface SuscripcionesFeignClient {

	@GetMapping("/suscripciones/obtener/nombre/{nombre}")
	public Suscripciones obtenerSuscripcionesNombre(@PathVariable("nombre") String nombre);

}
