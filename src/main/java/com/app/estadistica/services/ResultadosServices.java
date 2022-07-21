package com.app.estadistica.services;

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

	private final boolean errors = true;

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
		case 1: // UBICAR POR PREFERENCIA
			List<Double> pP = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
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
		case 2: // 100 DOLARES
			List<Double> p = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
			r.setPromedio(p);
			r.setMayorEscogida("mayor");
			r.setMenorEscogida("menor");
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 3: // SELECCION MULTIPLE
			List<Integer> personas = new ArrayList<Integer>(Collections.nCopies(r.getOpciones().size(), 0));
			List<Double> prom = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
			r.setPersonasOpcion(personas);
			r.setRespuestas(new ArrayList<String>());
			r.setPromedio(prom);
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			break;
		case 4: // BARRA DE VALOR
			List<Double> pP4 = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
			List<String> i4 = new ArrayList<String>();
			r.getOpciones().forEach(x -> {
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
		case 5: // PREGUNTA ABIERTA
			List<String> resp = new ArrayList<String>();
			r.setRespuestas(resp);
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			break;
		case 6: // KANO
			List<Double> pr6 = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
			r.setPromedioPonderado(pr6);
			r.setMayorEscogida("Sin respuestas");
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMenorEscogida("-1");
			r.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
			r.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
			break;
		case 7: // LIKERT
			List<Integer> pO7 = new ArrayList<Integer>(Collections.nCopies(r.getOpciones().size(), 0));
			List<Double> p7 = new ArrayList<Double>(Collections.nCopies(r.getOpciones().size(), 0.0));
			r.setPersonasOpcion(pO7); // <- ESTA
			r.setPromedio(p7); // <- Porcentaje
			r.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
			r.setMayorEscogida("-1");
			r.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
			r.setMenorEscogida("-1");
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
		if (errors)
			log.info("Resultado existe");

		List<Respuestas> respuestas = cbFactory.create("respuestas").run(
				() -> rClient.verRespuestasPreguntaProyecto(idProyecto, numeroPregunta, formulario),
				e -> encontrarRespuestasPreguntaProyecto(e));
		if (errors)
			log.info("Longitud respuestas: " + respuestas.size());
		resultado.setNumeroPersonas(respuestas.size());
		if (errors)
			log.info("Numero personas: " + resultado.getNumeroPersonas());
		List<List<Double>> listaProm = new ArrayList<List<Double>>();
		List<Double> promedioPonderado = new ArrayList<Double>(
				Collections.nCopies(resultado.getOpciones().size(), 0.0));
		List<String> impacto = new ArrayList<String>();
		List<Double> promedio = new ArrayList<Double>(Collections.nCopies(resultado.getOpciones().size(), 0.0));
		List<String> respUsuario = new ArrayList<String>();
		List<Integer> personasOpcion = new ArrayList<Integer>(Collections.nCopies(resultado.getOpciones().size(), 0));
		switch (resultado.getTipoConsulta()) {
		case 1: // ESCOJENCIA POR PREFERENCIA
			Double prom = (double) (100 / resultado.getOpciones().size());
			if (errors)
				log.info("Promedio: " + prom);
			respuestas.forEach(x -> {
				List<Double> l = new ArrayList<Double>(Collections.nCopies(resultado.getOpciones().size(), 0.0));
				x.getRespuestas().forEach(y -> {
					l.set(resultado.getOpciones().indexOf(y), 100.0 - (prom * x.getRespuestas().indexOf(y)));
					if (errors)
						log.info("lista arreglo: " + l);
				});
				listaProm.add(l);
			});
			if (errors)
				log.info("lista prom: " + listaProm);
			for (int i = 0; i < resultado.getOpciones().size(); i++) {
				Double val = 0.0;
				for (int j = 0; j < listaProm.size(); j++) {
					val += listaProm.get(j).get(i);
				}
				val /= resultado.getNumeroPersonas();
				promedioPonderado.set(i, val);
			}
			if (errors)
				log.info("promedio ponderado: " + promedioPonderado);
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
			if (errors)
				log.info("pass7");
			resultado.setPromedioPonderado(promedioPonderado);
			resultado.setImpacto(impacto);
			break;
		case 2: // 100 DOLARES
			for (int i = 0; i < respuestas.size(); i++) {
				for (int j = 0; j < respuestas.get(i).getRespuestas().size(); j++) {
					promedio.set(j, promedio.get(j) + Double.parseDouble(respuestas.get(i).getRespuestas().get(j)));
				}
			}
			if (errors)
				log.info("promedio: " + promedio);
			for (int i = 0; i < promedio.size(); i++) {
				promedio.set(i, promedio.get(i) / resultado.getNumeroPersonas());
			}
			if (errors)
				log.info("promedio dividido: " + promedio);
			resultado.setPromedio(promedio);
			resultado.setMayorEscogida(resultado.getOpciones()
					.get(promedio.indexOf(promedio.stream().max(Comparator.naturalOrder()).get())));
			resultado.setMenorEscogida(resultado.getOpciones()
					.get(promedio.indexOf(promedio.stream().min(Comparator.naturalOrder()).get())));
			break;
		case 3: // OPCION MULTIPLE
			respuestas.forEach(r -> {
				r.getRespuestas().forEach(ru -> {
					if (resultado.getOpciones().contains(ru)) {
						int i = resultado.getOpciones().indexOf(ru);
						personasOpcion.set(i, personasOpcion.get(i) + 1);
					} else
						respUsuario.add(ru);
				});
			});
			if (errors) {
				log.info("personas Opcion: " + personasOpcion);
				log.info("respuestas usuario: " + respUsuario);
			}
			for (int i = 0; i < promedio.size(); i++) {
				promedio.set(i, (double) (personasOpcion.get(i) * 100 / resultado.getNumeroPersonas()));
			}
			if (errors)
				log.info("promedio: " + promedio);
			resultado.setPersonasOpcion(personasOpcion);
			resultado.setRespuestas(respUsuario);
			resultado.setPromedio(promedio);
			break;
		case 4: // BARRA PORCENTAJE
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
				promedioPonderado.set(i, val);
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
		case 5: // PREGUNTA ABIERTA
			respuestas.forEach(r -> {
				r.getRespuestas().forEach(ur -> {
					respUsuario.add(ur);
				});
			});
			resultado.setRespuestas(respUsuario);
			break;
		case 6: // KANO
			List<Double> resultadoPersonas = tipoSeisPrimero(respuestas);
			resultado.setPromedioPonderado(resultadoPersonas);
			resultado.setMayorEscogida(tipoSeisSegundo(resultadoPersonas, resultado));
			break;
		case 7: // LIKERT
			respuestas.forEach(r -> {
				r.getRespuestas().forEach(ru -> {
					if (resultado.getOpciones().contains(ru)) {
						int i = resultado.getOpciones().indexOf(ru);
						personasOpcion.set(i, personasOpcion.get(i) + 1);
					}
				});
			});
			if (errors)
				log.info("personas Opcion: " + personasOpcion);
			for (int i = 0; i < promedio.size(); i++) {
				promedio.set(i, (double) (personasOpcion.get(i) * 100 / resultado.getNumeroPersonas()));
			}
			if (errors)
				log.info("promedio: " + promedio);
			resultado.setPersonasOpcion(personasOpcion);
			resultado.setPromedio(promedio);
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
