package com.app.estadistica.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.estadistica.clients.RespuestasFeignClient;
import com.app.estadistica.models.Resultados;
import com.app.estadistica.repository.ResultadosRepository;
import com.app.estadistica.requests.Respuestas;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResultadosServices implements IResultadosServices {

	@SuppressWarnings("rawtypes")
	@Autowired
	private CircuitBreakerFactory cbFactory;

	@Autowired
	ResultadosRepository rRepository;

	@Autowired
	RespuestasFeignClient rClient;

	@Override
	public void crearResultados(Integer idProyecto, Integer formulario, Integer numeroPregunta, String pregunta,
			Integer tipoConsulta, List<String> opciones, List<String> mensajeImpacto) {
		Resultados r = new Resultados();
		r.setIdProyecto(idProyecto);
		r.setFormulario(formulario);
		r.setNumeroPregunta(numeroPregunta);
		r.setPregunta(pregunta);
		r.setTipoConsulta(tipoConsulta);
		r.setOpciones(opciones);
		r.setMensajeImpacto(mensajeImpacto);
		r.setNumeroPersonas(0);
		switch (r.getTipoConsulta()) {
		case 1:
			List<Double> pP = new ArrayList<Double>();
			List<String> i = new ArrayList<String>();
			r.getOpciones().forEach(x -> {
				pP.add(0.0);
				i.add(mensajeImpacto.get(mensajeImpacto.size() - 1));
			});
			r.setPromedioPonderado(pP);
			r.setImpacto(i);
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 2:
			List<Double> p = new ArrayList<Double>();
			r.getOpciones().forEach(x -> {
				p.add(0.0);
			});
			r.setPromedio(p);
			r.setMayorEscogida("mayor");
			r.setMenorEscogida("menor");
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 3:
			List<Integer> personas = new ArrayList<Integer>();
			List<Double> prom = new ArrayList<Double>();
			r.getOpciones().forEach(x -> {
				personas.add(0);
				prom.add(0.0);
			});
			r.setPersonasOpcion(personas);
			r.setRespuestas(new ArrayList<String>());
			r.setPromedio(prom);
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			break;
		case 4:
			List<Double> pP4 = new ArrayList<Double>();
			List<String> i4 = new ArrayList<String>();
			r.getOpciones().forEach(x -> {
				pP4.add(0.0);
				i4.add(mensajeImpacto.get(mensajeImpacto.size() - 1));
			});
			r.setPromedioPonderado(pP4);
			r.setImpacto(i4);
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 5:
			List<String> resp = new ArrayList<String>();
			r.setRespuestas(resp);
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			break;
		case 6:
			List<Double> pr6 = new ArrayList<Double>();
			for (int j = 0; j < 6; j++) {
				pr6.add((double) 0);
			}
			r.setPromedioPonderado(pr6);
			r.setMayorEscogida("Sin respuestas");
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 7:
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		default:
			break;
		}
		rRepository.save(r);
	}

	@Override
	public void obtenerEstadisticaResultados(Integer idProyecto, Integer formulario, Integer numeroPregunta) {
		Resultados resultado = rRepository.findByIdProyectoAndFormularioAndNumeroPregunta(idProyecto, formulario,
				numeroPregunta);
		resultado.setNumeroPersonas(resultado.getNumeroPersonas() + 1);
		List<Respuestas> respuestas = cbFactory.create("respuestas").run(
				() -> rClient.verRespuestasPreguntaProyecto(idProyecto, numeroPregunta, formulario),
				e -> encontrarRespuestasPreguntaProyecto(e));
		List<List<Double>> listaProm = new ArrayList<List<Double>>();
		List<Double> promedioPonderado = new ArrayList<Double>();
		List<String> impacto = new ArrayList<String>();
		List<Double> promedio = new ArrayList<Double>();
		switch (resultado.getTipoConsulta()) {
		case 1:

			Double prom = (double) (100 / resultado.getOpciones().size());
			respuestas.forEach(x -> {
				List<Double> l = new ArrayList<Double>(Collections.nCopies(resultado.getOpciones().size(), 0.0));
				x.getRespuestas().forEach(y -> {
					l.set(resultado.getOpciones().indexOf(y), 100.0 - (prom * x.getRespuestas().indexOf(y)));
				});
				listaProm.add(l);
			});
			for (int i = 0; i < resultado.getOpciones().size(); i++) {
				Double val = 0.0;
				for (int j = 0; j < listaProm.size(); j++) {
					val += listaProm.get(j).get(i);
				}
				val /= resultado.getNumeroPersonas();
				promedioPonderado.add(val);
			}
			for (int j = 0; j < promedioPonderado.size(); j++) {
				if (promedioPonderado.get(j) >= 80) {
					impacto.add(resultado.getMensajeImpacto().get(0));
				} else if (promedioPonderado.get(j) >= 60) {
					impacto.add(resultado.getMensajeImpacto().get(1));
				} else if (promedioPonderado.get(j) >= 40) {
					impacto.add(resultado.getMensajeImpacto().get(2));
				} else if (promedioPonderado.get(j) >= 20) {
					impacto.add(resultado.getMensajeImpacto().get(3));
				} else {
					impacto.add(resultado.getMensajeImpacto().get(4));
				}
			}
			resultado.setPromedioPonderado(promedioPonderado);
			resultado.setImpacto(impacto);
			break;
		case 2:
			for (int i = 0; i < respuestas.size(); i++) {
				promedio.add(0.0);
				for (int j = 0; j < respuestas.get(i).getRespuestas().size(); j++) {
					promedio.set(j, promedio.get(j) + Double.parseDouble(respuestas.get(i).getRespuestas().get(j)));
				}
				Double operacion = promedio.get(i) / resultado.getNumeroPersonas();
				promedio.set(i, operacion);
			}
			resultado.setPromedio(promedio);
			resultado.setMayorEscogida(resultado.getOpciones()
					.get(promedio.indexOf(promedio.stream().max(Comparator.naturalOrder()).get())));
			resultado.setMenorEscogida(resultado.getOpciones()
					.get(promedio.indexOf(promedio.stream().min(Comparator.naturalOrder()).get())));
			break;
		case 3:
			List<Integer> promedioOpcion3 = new ArrayList<Integer>(
					Collections.nCopies(resultado.getOpciones().size(), 0));
			List<String> respUsuario = new ArrayList<String>();
			List<Integer> personasOpcion = new ArrayList<Integer>();

			for (int i = 0; i < respuestas.size(); i++) {
				for (int j = 0; j < respuestas.get(i).getRespuestas().size(); j++) {
					if (resultado.getOpciones().contains(respuestas.get(i).getRespuestas().get(j))) {
						int index = resultado.getOpciones().indexOf(respuestas.get(i).getRespuestas().get(j));
						promedioOpcion3.set(index, promedioOpcion3.get(index) + 1);
					} else
						respUsuario.add(respuestas.get(i).getRespuestas().get(j));
				}
			}

			for (int j = 0; j < personasOpcion.size(); j++) {
				double op1 = personasOpcion.get(j);
				promedio.add((op1 / resultado.getNumeroPersonas()) * 100);
			}

			for (int i = 0; i < promedio.size(); i++) {
				BigDecimal bD = new BigDecimal(promedio.get(i)).setScale(2, RoundingMode.HALF_UP);
				Double d2 = bD.doubleValue();
				promedio.set(i, d2);
			}
			resultado.setPersonasOpcion(personasOpcion);
			resultado.setRespuestas(respUsuario);
			resultado.setPromedio(promedio);
			break;
		case 4:
			respuestas.forEach(x -> {
				List<Double> r = new ArrayList<Double>();
				for (String d : x.getRespuestas()) {
					r.add(Double.valueOf(d));
				}
				listaProm.add(r);
			});
			for (int i = 0; i < resultado.getOpciones().size(); i++) {
				Double val = 0.0;
				for (int j = 0; j < listaProm.size(); j++) {
					val += listaProm.get(j).get(i);
				}
				val /= resultado.getNumeroPersonas();
				promedioPonderado.add(val);
			}
			for (int j = 0; j < promedioPonderado.size(); j++) {
				if (promedioPonderado.get(j) >= 80) {
					impacto.add(resultado.getMensajeImpacto().get(0));
				} else if (promedioPonderado.get(j) >= 60) {
					impacto.add(resultado.getMensajeImpacto().get(1));
				} else if (promedioPonderado.get(j) >= 40) {
					impacto.add(resultado.getMensajeImpacto().get(2));
				} else if (promedioPonderado.get(j) >= 20) {
					impacto.add(resultado.getMensajeImpacto().get(3));
				} else {
					impacto.add(resultado.getMensajeImpacto().get(4));
				}
			}
			resultado.setPromedioPonderado(promedioPonderado);
			resultado.setImpacto(impacto);
			break;
		case 5:
			List<String> r = new ArrayList<String>();
			for (int i = 0; i < r.size(); i++) {
				respuestas.get(i).getRespuestas().forEach(resp -> r.add(resp));
			}
			resultado.setRespuestas(r);
			break;
		case 6: // KANO
			List<Double> resultadoPersonas = tipoSeisPrimero(respuestas);
			resultado.setPromedioPonderado(resultadoPersonas);
			resultado.setMayorEscogida(tipoSeisSegundo(resultadoPersonas, resultado));
			break;
		case 7: // LIKERT
			break;
		default:
			break;
		}
		rRepository.save(resultado);
	}

	private List<Double> tipoSeisPrimero(List<Respuestas> r) {
		List<Double> respuesta = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		for (int a = 0; a < r.size(); a++) {
			List<String> list = r.get(a).getRespuestas();
			for (int i = 0; i < list.size(); i++) {
				switch (list.get(0)) {
				case "1.0":
					switch (list.get(1)) {
					case "1.0":
						respuesta.set(5, respuesta.get(5) + 1);
						break;
					default:
						respuesta.set(4, respuesta.get(4) + 1);
						break;
					}
					break;
				case "5.0":
					switch (list.get(1)) {
					case "1.0":
						respuesta.set(1, respuesta.get(1) + 1);
						break;
					case "5.0":
						respuesta.set(5, respuesta.get(5) + 1);
						break;
					default:
						respuesta.set(0, respuesta.get(0) + 1);
						break;
					}
					break;
				default:
					switch (list.get(1)) {
					case "1.0":
						respuesta.set(2, respuesta.get(2) + 1);
						break;
					case "5.0":
						respuesta.set(4, respuesta.get(4) + 1);
						break;
					default:
						respuesta.set(3, respuesta.get(3) + 1);
						break;
					}
					break;
				}
			}
		}
		return respuesta;
	}

	private String tipoSeisSegundo(List<Double> list, Resultados r) {
		Double valor = Collections.max(list);
		if (valor == 0.0)
			return "Cuestionable";
		int pos = list.indexOf(valor);
		for (int i = (pos + 1); i < list.size(); i++) {
			if (valor == list.get(i)) {
				pos = list.indexOf(list.get(i));
				i = pos;
			}
		}
		switch (pos) {
		case 0:
			return r.getMensajeImpacto().get(0);
		case 1:
			return r.getMensajeImpacto().get(1);
		case 2:
			return r.getMensajeImpacto().get(2);
		case 3:
			return r.getMensajeImpacto().get(3);
		case 4:
			return r.getMensajeImpacto().get(4);
		default:
			return r.getMensajeImpacto().get(5);
		}
	}

//  ****************************	FUNCIONES TOLERANCIA A FALLOS	***********************************  //

	private List<Respuestas> encontrarRespuestasPreguntaProyecto(Throwable e) {
		log.error(e.getMessage());
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Servicio Respuestas no disponible");
	}

}
