package com.app.estadistica.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.estadistica.models.PreguntasRespuestas;
import com.app.estadistica.request.Resultados;

@Service
public class EstadisticaService implements IEstadisticaService {

	@Override
	public List<List<String>> tipoUnoCuatro(int i, PreguntasRespuestas preguntasRespuestas, Resultados resultados) {
		List<Double> promedioPonderado = new ArrayList<Double>();
		List<String> impacto = new ArrayList<String>();
		List<String> promedioPonderadoString = new ArrayList<String>();
		List<List<String>> tipoUno = new ArrayList<List<String>>();
		for (int k = 0; k < preguntasRespuestas.getPreguntas().get(i).getOpciones().size(); k++) {
			System.out.println("Opcion #:" + k);
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
			for (int j = 0; j < preguntasRespuestas.getRespuestas().get(i).size(); j++) {
				if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 90
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) <= 100) {
					count90100++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 80
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 90) {
					count8090++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 70
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 80) {
					count7080++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 60
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 70) {
					count6070++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 50
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 60) {
					count5060++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 40
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 50) {
					count4050++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 30
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 40) {
					count3040++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 20
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 30) {
					count2030++;
				} else if (Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) >= 10
						&& Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)) < 20) {
					count1020++;
				} else {
					count010++;
				}
			}
			System.out.println("Sin error hasta aqui");
			total = count90100 * 95 + count8090 * 85 + count7080 * 75 + count6070 * 65 + count5060 * 55 + count4050 * 45
					+ count3040 * 35 + count2030 * 25 + count1020 * 15 + count010 * 5;
			Double resultadoPromedioPonderado = 0.00;
			if (resultados.getNumeroPersonas() != 0)
				resultadoPromedioPonderado = total / resultados.getNumeroPersonas();
			BigDecimal bdlat = new BigDecimal(resultadoPromedioPonderado).setScale(2, RoundingMode.HALF_UP);

			promedioPonderado.add(bdlat.doubleValue());
		}
		for (int j = 0; j < promedioPonderado.size(); j++) {
			if (promedioPonderado.get(j) >= 80) {
				impacto.add("Excelente impacto");
			} else if (promedioPonderado.get(j) >= 60) {
				impacto.add("Alto impacto");
			} else if (promedioPonderado.get(j) >= 40) {
				impacto.add("Medio impacto");
			} else if (promedioPonderado.get(j) >= 20) {
				impacto.add("Bajo impacto");
			} else {
				impacto.add("Pesimo impacto");
			}
		}

		for (Double d : promedioPonderado) {
			promedioPonderadoString.add(d.toString());
		}
		tipoUno.add(promedioPonderadoString);
		tipoUno.add(impacto);
		return tipoUno;
	}

	@Override
	public List<List<String>> tipoDos(int i, PreguntasRespuestas preguntasRespuestas, Resultados resultados) {
		List<Double> promedio = new ArrayList<Double>();
		List<String> promedioString = new ArrayList<String>();
		List<String> menorMayor = new ArrayList<String>();
		List<List<String>> tipoDos = new ArrayList<List<String>>();
		for (int k = 0; k < preguntasRespuestas.getPreguntas().get(i).getOpciones().size(); k++) {
			promedio.add(0.0);
			for (int j = 0; j < preguntasRespuestas.getRespuestas().get(i).size(); j++) {
				promedio.set(k,
						promedio.get(k) + Double.parseDouble(preguntasRespuestas.getRespuestas().get(i).get(j).get(k)));
			}
			Integer numPersonas = resultados.getNumeroPersonas();
			Double operacion = 0.0;
			if (numPersonas != 0)
				operacion = promedio.get(k) / resultados.getNumeroPersonas();
			promedio.set(k, operacion);
		}

		for (Double d : promedio) {
			BigDecimal bdlat = new BigDecimal(d).setScale(2, RoundingMode.HALF_UP);
			Double d2 = bdlat.doubleValue();
			promedioString.add(d2.toString());
		}

		tipoDos.add(promedioString);
		menorMayor.add(
				resultados.getOpciones().get(promedio.indexOf(promedio.stream().min(Comparator.naturalOrder()).get())));
		menorMayor.add(
				resultados.getOpciones().get(promedio.indexOf(promedio.stream().max(Comparator.naturalOrder()).get())));
		tipoDos.add(menorMayor);
		return tipoDos;
	}

	@Override
	public List<List<String>> tipoTres(int i, PreguntasRespuestas preguntasRespuestas, Resultados resultados) {
		List<Integer> personasOpcion = new ArrayList<Integer>();
		List<String> personasOpcionString = new ArrayList<String>();
		List<Double> promedioOpcion3 = new ArrayList<Double>();
		List<String> respuestas = new ArrayList<String>();
		List<Double> promedio = new ArrayList<Double>();
		List<String> promedioString = new ArrayList<String>();
		List<List<String>> tipoTres = new ArrayList<List<String>>();

		for (int j = 0; j < resultados.getOpciones().size(); j++) {
			promedioOpcion3.add(0.0);
		}
		for (int j = 0; j < preguntasRespuestas.getRespuestas().get(i).size(); j++) {
			for (int k = 0; k < preguntasRespuestas.getRespuestas().get(i).get(j).size(); k++) {
				if (resultados.getOpciones().contains(preguntasRespuestas.getRespuestas().get(i).get(j).get(k))) {
					int index = resultados.getOpciones()
							.indexOf(preguntasRespuestas.getRespuestas().get(i).get(j).get(k));
					promedioOpcion3.set(index, promedioOpcion3.get(index) + 1);
				} else {
					respuestas.add(preguntasRespuestas.getRespuestas().get(i).get(j).get(k));
				}
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
			double op2 = resultados.getNumeroPersonas();
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

	@Override
	public List<Resultados> obtenerPreguntasCreacion(PreguntasRespuestas p) {

		List<Resultados> lResultados = new ArrayList<Resultados>();
		p.getPreguntas().forEach(i -> {
			List<Double> promedioPonderado = new ArrayList<Double>();
			List<Double> promedio = new ArrayList<Double>();
			List<Integer> personasOpcion = new ArrayList<Integer>();
			List<String> respuestas = new ArrayList<String>();
			Resultados resultado = new Resultados();
			resultado.setEnunciado(i.getPregunta());
			resultado.setTipoConsulta(i.getTipoConsulta());
			resultado.setOpciones(i.getOpciones());
			resultado.setNumeroPersonas(p.getRespuestas().size());
			switch (i.getTipoConsulta()) {
			case 1:
				List<List<String>> tipoUno = tipoUnoCuatro(p.getPreguntas().indexOf(i), p, resultado);
				for (String d : tipoUno.get(0)) {
					promedioPonderado.add(Double.parseDouble(d));
				}
				;
				resultado.setPromedioPonderado(promedioPonderado);
				resultado.setImpacto(tipoUno.get(1));
				resultado.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setMayorEscogida("-1");
				resultado.setMenorEscogida("-1");
				resultado.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
				resultado.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
				break;
			case 2:
				List<List<String>> tipoDos = tipoDos(p.getPreguntas().indexOf(i), p, resultado);
				for (String d : tipoDos.get(0)) {
					promedio.add(Double.parseDouble(d));
				}
				resultado.setPromedio(promedio);
				resultado.setMayorEscogida(tipoDos.get(1).get(1));
				resultado.setMenorEscogida(tipoDos.get(1).get(0));
				resultado.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
				resultado.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
				resultado.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
				break;
			case 3:
				List<List<String>> tipoTres = tipoTres(p.getPreguntas().indexOf(i), p, resultado);
				for (String d : tipoTres.get(0)) {
					personasOpcion.add(Integer.valueOf(d));
				}
				for (String d : tipoTres.get(2)) {
					promedio.add(Double.parseDouble(d));
				}
				resultado.setPersonasOpcion(personasOpcion);
				resultado.setRespuestas(tipoTres.get(1));
				resultado.setPromedio(promedio);
				resultado.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
				resultado.setMayorEscogida("-1");
				resultado.setMenorEscogida("-1");
				break;
			case 4:
				List<List<String>> tipoCuatro = tipoUnoCuatro(p.getPreguntas().indexOf(i), p, resultado);
				for (String d : tipoCuatro.get(0)) {
					promedioPonderado.add(Double.parseDouble(d));
				}
				resultado.setPromedioPonderado(promedioPonderado);
				resultado.setImpacto(tipoCuatro.get(1));
				resultado.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setMayorEscogida("-1");
				resultado.setMenorEscogida("-1");
				resultado.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
				resultado.setRespuestas(new ArrayList<String>(Arrays.asList("-1")));
				break;
			case 5:
				for (int j = 0; j < p.getRespuestas().size(); j++) {
					respuestas.add(p.getRespuestas().get(p.getPreguntas().indexOf(i)).get(j).get(0));
				}
				resultado.setRespuestas(respuestas);
				resultado.setPromedioPonderado(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setImpacto(new ArrayList<String>(Arrays.asList("-1")));
				resultado.setPromedio(new ArrayList<Double>(Arrays.asList(-1.0)));
				resultado.setMayorEscogida("-1");
				resultado.setMenorEscogida("-1");
				resultado.setPersonasOpcion(new ArrayList<Integer>(Arrays.asList(-1)));
				break;
			default:
				break;
			}
			lResultados.add(resultado);
		});
		return lResultados;
	}

}
