package com.app.estadistica.models;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.app.estadistica.request.Preguntas;


@Document(collection = "preguntasrespuestas")
public class PreguntasRespuestas {

	@Id
	private String id;

	@NotBlank(message = "Name cannot be null")
	@Size(max = 50)
	@Indexed(unique = true)
	private String nombre;

	private List<Preguntas> preguntas;

	private List<List<List<String>>> respuestas;

	private List<String> usuarios;

	private List<List<List<String>>> respuestasUsuario;

	public PreguntasRespuestas() {
	}

	public PreguntasRespuestas(String nombre, List<Preguntas> preguntas, List<List<List<String>>> respuestas,
			List<String> usuarios, List<List<List<String>>> respuestasUsuario) {
		super();
		this.nombre = nombre;
		this.preguntas = preguntas;
		this.respuestas = respuestas;
		this.usuarios = usuarios;
		this.respuestasUsuario = respuestasUsuario;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Preguntas> getPreguntas() {
		return preguntas;
	}

	public void setPreguntas(List<Preguntas> preguntas) {
		this.preguntas = preguntas;
	}

	public List<List<List<String>>> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<List<List<String>>> respuestas) {
		this.respuestas = respuestas;
	}

	public List<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}

	public List<List<List<String>>> getRespuestasUsuario() {
		return respuestasUsuario;
	}

	public void setRespuestasUsuario(List<List<List<String>>> respuestasUsuario) {
		this.respuestasUsuario = respuestasUsuario;
	}

}