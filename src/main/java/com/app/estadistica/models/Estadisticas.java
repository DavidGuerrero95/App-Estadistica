package com.app.estadistica.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "estadisticas")
@Data
@NoArgsConstructor
public class Estadisticas {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	@Indexed(unique = true)
	private Integer idProyecto;
	
	private Integer visualizaciones;
	private Integer likes;
	private Integer dislikes;
	private Integer numeroSuscritas;
	private Integer numeroCuestionario;
	private Integer numeroComentarios;

	private List<Resultados> resultados;

	public Estadisticas(Integer idProyecto, Integer visualizaciones, Integer likes, Integer dislikes,
			Integer numeroSuscritas, Integer numeroCuestionario, Integer numeroComentarios,
			List<Resultados> resultados) {
		super();
		this.idProyecto = idProyecto;
		this.visualizaciones = visualizaciones;
		this.likes = likes;
		this.dislikes = dislikes;
		this.numeroSuscritas = numeroSuscritas;
		this.numeroCuestionario = numeroCuestionario;
		this.numeroComentarios = numeroComentarios;
		this.resultados = resultados;
	}

}