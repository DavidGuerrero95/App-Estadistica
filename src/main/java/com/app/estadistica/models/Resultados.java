package com.app.estadistica.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "resultados")
@Data
@NoArgsConstructor
public class Resultados {

	@Id
	@JsonIgnore
	private String id;

	@NotNull(message = "id proyecto cannot be null")
	@Indexed(unique = false)
	private Integer idProyecto;

	@NotNull(message = "formulario be null")
	@Indexed(unique = false)
	private Integer formulario;

	@NotNull(message = "numero de pregunta be null")
	@Indexed(unique = false)
	private Integer numeroPregunta;

	@NotNull(message = "pregunta be null")
	@Indexed(unique = false)
	private String pregunta;

	@NotNull(message = "tipoConsulta be null")
	@Indexed(unique = false)
	private Integer tipoConsulta;

	@NotNull(message = "opciones be null")
	@Indexed(unique = false)
	private List<String> opciones;

	@NotNull(message = "mensajeImpacto be null")
	@Indexed(unique = false)
	private List<String> mensajeImpacto;

	private Integer numeroPersonas;
	private List<Double> promedioPonderado;
	private List<String> impacto;
	private List<Double> promedio;
	private String mayorEscogida;
	private String menorEscogida;
	private List<Integer> personasOpcion;
	private List<String> respuestas;

}
