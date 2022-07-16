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
		default:
			break;
		}
		rRepository.save(r);
	}

	@Override
	public List<Resultados> obtenerResultados(Integer idProyecto, Integer formulario) {
		List<Resultados> lR = rRepository.findByIdProyectoAndFormulario(idProyecto, formulario);
		lR.forEach(x -> {
			List<Double> promedioPonderado = new ArrayList<Double>();
			List<Double> promedio = new ArrayList<Double>();
			List<Integer> personasOpcion = new ArrayList<Integer>();
			List<String> respuestas = new ArrayList<String>();
			List<Respuestas> r = cbFactory.create("respuestas").run(
					() -> rClient.verRespuestasPreguntaProyecto(idProyecto, x.getNumeroPregunta(), formulario),
					e -> encontrarRespuestasPreguntaProyecto(e));

			x.setNumeroPersonas(r.size());
			switch (x.getTipoConsulta()) {
			case 1:
				List<List<String>> uno = tipoUno(r, x);
				for (String d : uno.get(0)) {
					promedioPonderado.add(Double.parseDouble(d));
				}
				x.setPromedioPonderado(promedioPonderado);
				x.setImpacto(uno.get(1));
				break;
			case 2:
				List<List<String>> dos = tipoDos(r, x);
				for (String d : dos.get(0)) {
					promedio.add(Double.parseDouble(d));
				}
				x.setMenorEscogida(dos.get(1).get(0));
				x.setMayorEscogida(dos.get(1).get(1));
				x.setPromedio(promedio);
				break;
			case 3:
				List<List<String>> tres = tipoTres(r, x);
				for (String d : tres.get(0)) {
					personasOpcion.add(Integer.valueOf(d));
				}
				for (String d : tres.get(2)) {
					promedio.add(Double.parseDouble(d));
				}
				x.setPersonasOpcion(personasOpcion);
				x.setRespuestas(tres.get(1));
				x.setPromedio(promedio);
				break;
			case 4:
				List<List<String>> cuatro = tipoUno(r, x);
				for (String d : cuatro.get(0)) {
					promedioPonderado.add(Double.parseDouble(d));
				}
				x.setPromedioPonderado(promedioPonderado);
				x.setImpacto(cuatro.get(1));
				break;
			case 5:
				for (int i = 0; i < r.size(); i++) {
					r.get(i).getRespuestas().forEach(resp -> respuestas.add(resp));
				}
				x.setRespuestas(respuestas);
				break;
			case 6:
				List<Double> resultadoPersonas = tipoSeisPrimero(r);
				x.setPromedioPonderado(resultadoPersonas);
				x.setMayorEscogida(tipoSeisSegundo(resultadoPersonas, x));
				break;
			default:
				break;
			}
			rRepository.save(x);
		});

		return lR;
	}

	private List<List<String>> tipoUno(List<Respuestas> r, Resultados x) {
		List<Double> promedioPonderado = new ArrayList<Double>();
		List<String> impacto = new ArrayList<String>();
		List<String> promedioPonderadoString = new ArrayList<String>();
		List<List<String>> tipoUno = new ArrayList<List<String>>();
		for (int i = 0; i < r.size(); i++) {
			int count90100 = 0;
			int count8090 = 0;
			int count7080 = 0;
			int count6070 = 0;
			int count5060 = 0;
			int count4050 = 0;
			int count3040 = 0;
			int count2030 = 0;
			int count1020 = 0;
			int count010 = 0;
			double total = 0.0;
			for (int j = 0; j < r.get(i).getRespuestas().size(); j++) {
				double parcial = Double.parseDouble(r.get(i).getRespuestas().get(j));
				if (parcial >= 90 && parcial <= 100)
					count90100++;
				if (parcial >= 80 && parcial < 90)
					count8090++;
				if (parcial >= 70 && parcial < 80)
					count7080++;
				if (parcial >= 60 && parcial < 70)
					count6070++;
				if (parcial >= 50 && parcial < 60)
					count5060++;
				if (parcial >= 40 && parcial < 50)
					count4050++;
				if (parcial >= 30 && parcial < 40)
					count3040++;
				if (parcial >= 20 && parcial < 30)
					count2030++;
				if (parcial >= 10 && parcial < 20)
					count1020++;
				if (parcial >= 0 && parcial < 10)
					count010++;
			}
			total = count90100 * 95 + count8090 * 85 + count7080 * 75 + count6070 * 65 + count5060 * 55 + count4050 * 45
					+ count3040 * 35 + count2030 * 25 + count1020 * 15 + count010 * 5;
			Double resultadoPromedioPonderado = 0.00;
			if (x.getNumeroPersonas() != 0)
				resultadoPromedioPonderado = total / x.getNumeroPersonas();
			BigDecimal bdlat = new BigDecimal(resultadoPromedioPonderado).setScale(2, RoundingMode.HALF_UP);
			promedioPonderado.add(bdlat.doubleValue());
		}
		for (int j = 0; j < promedioPonderado.size(); j++) {
			if (promedioPonderado.get(j) >= 80) {
				impacto.add(x.getMensajeImpacto().get(0));
			} else if (promedioPonderado.get(j) >= 60) {
				impacto.add(x.getMensajeImpacto().get(1));
			} else if (promedioPonderado.get(j) >= 40) {
				impacto.add(x.getMensajeImpacto().get(2));
			} else if (promedioPonderado.get(j) >= 20) {
				impacto.add(x.getMensajeImpacto().get(3));
			} else {
				impacto.add(x.getMensajeImpacto().get(4));
			}
		}

		for (Double d : promedioPonderado) {
			promedioPonderadoString.add(d.toString());
		}
		tipoUno.add(promedioPonderadoString);
		tipoUno.add(impacto);
		return tipoUno;
	}

	private List<List<String>> tipoDos(List<Respuestas> r, Resultados x) {
		List<Double> promedio = new ArrayList<Double>();
		List<String> promedioString = new ArrayList<String>();
		List<List<String>> tipoDos = new ArrayList<List<String>>();
		List<String> menorMayor = new ArrayList<String>();
		for (int i = 0; i < r.size(); i++) {
			promedio.add(0.0);
			for (int j = 0; j < r.get(i).getRespuestas().size(); j++) {
				promedio.set(j, promedio.get(j) + Double.parseDouble(r.get(i).getRespuestas().get(j)));
			}
			Integer numPersonas = x.getNumeroPersonas();
			Double operacion = 0.0;
			if (numPersonas != 0)
				operacion = promedio.get(i) / x.getNumeroPersonas();
			promedio.set(i, operacion);
		}
		for (Double d : promedio) {
			BigDecimal bdlat = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
			Double d2 = bdlat.doubleValue();
			promedioString.add(d2.toString());
		}

		tipoDos.add(promedioString);
		menorMayor.add(x.getOpciones().get(promedio.indexOf(promedio.stream().min(Comparator.naturalOrder()).get())));
		menorMayor.add(x.getOpciones().get(promedio.indexOf(promedio.stream().max(Comparator.naturalOrder()).get())));
		tipoDos.add(menorMayor);
		return tipoDos;
	}

	private List<List<String>> tipoTres(List<Respuestas> r, Resultados x) {
		List<Double> promedioOpcion3 = new ArrayList<Double>();
		List<String> respuestas = new ArrayList<String>();
		List<Integer> personasOpcion = new ArrayList<Integer>();
		List<String> personasOpcionString = new ArrayList<String>();
		List<Double> promedio = new ArrayList<Double>();
		List<String> promedioString = new ArrayList<String>();
		List<List<String>> tipoTres = new ArrayList<List<String>>();
		for (int j = 0; j < x.getOpciones().size(); j++) {
			promedioOpcion3.add(0.0);
		}
		for (int i = 0; i < r.size(); i++) {
			for (int j = 0; j < r.get(i).getRespuestas().size(); j++) {
				if (x.getOpciones().contains(r.get(i).getRespuestas().get(j))) {
					int index = x.getOpciones().indexOf(r.get(i).getRespuestas().get(j));
					promedioOpcion3.set(index, promedioOpcion3.get(index) + 1);
				} else
					respuestas.add(r.get(i).getRespuestas().get(j));

			}
		}
		for (Double d : promedioOpcion3) {
			personasOpcion.add(d.intValue());
		}

		for (Integer d : personasOpcion) {
			personasOpcionString.add(d.toString());
		}

		for (int j = 0; j < personasOpcion.size(); j++) {
			double op1 = personasOpcion.get(j);
			double op2 = x.getNumeroPersonas();
			if (op2 != 0)
				promedio.add((op1 / op2) * 100);
			else
				promedio.add(0.0);
		}

		for (Double d : promedio) {
			BigDecimal bdlat = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
			Double d2 = bdlat.doubleValue();
			promedioString.add(d2.toString());
		}

		tipoTres.add(personasOpcionString);
		tipoTres.add(respuestas);
		tipoTres.add(promedioString);

		return tipoTres;
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
