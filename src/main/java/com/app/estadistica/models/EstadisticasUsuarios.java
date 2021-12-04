package com.app.estadistica.models;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estadisticasUsuarios")
public class EstadisticasUsuarios {

	@Id
	private String id;

	@NotBlank(message = "Name cannot be null")
	@Size(max = 50)
	private String username;

	private List<String> historialParticipacion;

	public EstadisticasUsuarios() {
	}

	public EstadisticasUsuarios(String username, List<String> historialParticipacion) {
		super();
		this.username = username;
		this.historialParticipacion = historialParticipacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getHistorialParticipacion() {
		return historialParticipacion;
	}

	public void setHistorialParticipacion(List<String> historialParticipacion) {
		this.historialParticipacion = historialParticipacion;
	}

}