package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Area;
import model.Country;
import model.Measure;
import model.Observation;
import model.Time;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parseador de JSON de la API de la World Health Organization
 * 
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ParserJsonWHO extends AbstractParser {

	BufferedReader br;
	JsonParser parser;

	public ParserJsonWHO() {
		observations = new ArrayList<Observation>();
	}

	@Override
	public List<Observation> getParsedObservations() {
		try {
			br = new BufferedReader(new FileReader(file));
			parser = new JsonParser();

			String medida = getMeasure(indicator.getNombre());

			JsonObject ficheroJsonOriginal = parser.parse(br).getAsJsonObject();

			JsonArray arrayJsonDatosObservaciones = ficheroJsonOriginal
					.getAsJsonObject().get(keySearch).getAsJsonArray();

			for (int i = 0; i < arrayJsonDatosObservaciones.size(); i++) {

				try {
					Area area;

					// Comprobamos los dos posibles casos de ficheros de WHO

					JsonElement countryElement = (arrayJsonDatosObservaciones
							.get(i).getAsJsonObject().get("COUNTRY"));

					if (countryElement != null) {
						area = new Country(arrayJsonDatosObservaciones.get(i)
								.getAsJsonObject().get("COUNTRY").getAsString());
					} else {
						area = new Area(arrayJsonDatosObservaciones.get(i)
								.getAsJsonObject().get("MGHEREG").getAsString());
					}

					Measure measure = new Measure(arrayJsonDatosObservaciones
							.get(i).getAsJsonObject().get("Value")
							.getAsString(), medida);

					String year = arrayJsonDatosObservaciones.get(i)
							.getAsJsonObject().get("YEAR").getAsString();
					Date startDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-01-01 00:00:00.000000");
					Date endDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-12-31 23:59:59.000000");
					Time time = new Time(startDate, endDate);

					// Add observacion al listado

					Observation obs = new Observation(area, indicator, measure,
							time, provider, submission);

					observations.add(obs);

				} catch (NullPointerException e) {
					// Problema con la observacion (Formato no compatible)
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return observations;
	}

	private String getMeasure(String textoCompleto) {
		String resultado = textoCompleto;

		if (textoCompleto.contains("(")) {
			int startIndex = textoCompleto.indexOf("(");
			int endIndex = textoCompleto.lastIndexOf(")");

			resultado = textoCompleto.substring(startIndex + 1, endIndex);
		}
		System.out.println(resultado);
		return resultado;
	}

}
