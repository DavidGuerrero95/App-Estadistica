package com.app.estadistica.models;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "estadisticasUsuarios")
@Data
@NoArgsConstructor
public class EstadisticasUsuarios {

	@Id
	@JsonIgnore
	private String id;

	@NotBlank(message = "Name cannot be null")
	@Size(max = 50)
	private String username;

	private List<String> historialParticipacion;

	public EstadisticasUsuarios(String username, List<String> historialParticipacion) {
		super();
		this.username = username;
		this.historialParticipacion = historialParticipacion;
	}

}