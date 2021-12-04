package com.app.estadistica.request;

import java.util.List;

public class Resultados {

	private String enunciado;
	private Integer tipoConsulta;
	private List<String> opciones;
	private Integer numeroPersonas;
	private List<Double> promedioPonderado;
	private List<String> impacto;
	private List<Double> promedio;
	private String mayorEscogida;
	private String menorEscogida;
	private List<Integer> personasOpcion;
	private List<String> respuestas;

	public Resultados() {
	}

	public Resultados(String enunciado, Integer tipoConsulta, List<String> opciones, Integer numeroPersonas,
			List<Double> promedioPonderado, List<String> impacto, List<Double> promedio, String mayorEscogida,
			String menorEscogida, List<Integer> personasOpcion, List<String> respuestas) {
		super();
		this.enunciado = enunciado;
		this.tipoConsulta = tipoConsulta;
		this.opciones = opciones;
		this.numeroPersonas = numeroPersonas;
		this.promedioPonderado = promedioPonderado;
		this.impacto = impacto;
		this.promedio = promedio;
		this.mayorEscogida = mayorEscogida;
		this.menorEscogida = menorEscogida;
		this.personasOpcion = personasOpcion;
		this.respuestas = respuestas;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Integer getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(Integer tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public List<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<String> opciones) {
		this.opciones = opciones;
	}

	public Integer getNumeroPersonas() {
		return numeroPersonas;
	}

	public void setNumeroPersonas(Integer numeroPersonas) {
		this.numeroPersonas = numeroPersonas;
	}

	public List<Double> getPromedioPonderado() {
		return promedioPonderado;
	}

	public void setPromedioPonderado(List<Double> promedioPonderado) {
		this.promedioPonderado = promedioPonderado;
	}

	public List<String> getImpacto() {
		return impacto;
	}

	public void setImpacto(List<String> impacto) {
		this.impacto = impacto;
	}

	public List<Double> getPromedio() {
		return promedio;
	}

	public void setPromedio(List<Double> promedio) {
		this.promedio = promedio;
	}

	public String getMayorEscogida() {
		return mayorEscogida;
	}

	public void setMayorEscogida(String mayorEscogida) {
		this.mayorEscogida = mayorEscogida;
	}

	public String getMenorEscogida() {
		return menorEscogida;
	}

	public void setMenorEscogida(String menorEscogida) {
		this.menorEscogida = menorEscogida;
	}

	public List<Integer> getPersonasOpcion() {
		return personasOpcion;
	}

	public void setPersonasOpcion(List<Integer> personasOpcion) {
		this.personasOpcion = personasOpcion;
	}

	public List<String> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<String> respuestas) {
		this.respuestas = respuestas;
	}

}
