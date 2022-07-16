package com.app.estadistica.requests;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comentarios {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	@Indexed(unique = false)
	private Integer idProyecto;

	private String username;
	private Boolean anonimo;
	private String fecha;
	private String tiempo;
	private String comentario;

}
