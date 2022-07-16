package com.app.estadistica.requests;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Respuestas {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	private Integer idProyecto;

	@NotNull(message = "Username de pregunta be null")
	@Size(max = 20)
	private String username;

	@NotNull(message = "numero de pregunta be null")
	private Integer numeroPregunta;

	private Integer formulario;

	@NotEmpty(message = "Respuestas cannot be empty")
	private List<String> respuestas;

}
