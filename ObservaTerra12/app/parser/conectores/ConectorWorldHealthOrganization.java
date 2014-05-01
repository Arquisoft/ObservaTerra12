package parser.conectores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;

import org.apache.commons.io.FileUtils;

import parser.Parser;
import parser.ParserFactory;
import parser.ParserJson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * Clase conector con la API de la World Health Organization
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorWorldHealthOrganization extends Conector {

	private static ConectorWorldHealthOrganization instance;
	private Map<String, String> consultasDisponibles;

	private ConectorWorldHealthOrganization(String key) throws IOException {
		inicializaConector();
		this.key = key;
	}

	public static ConectorWorldHealthOrganization getInstance(String key)
			throws IOException {
		if (instance == null) {
			instance = new ConectorWorldHealthOrganization(key);
		}
		return instance;
	}

	/**
	 * Construye la URL de cada consulta especifica
	 * 
	 * @param label
	 *            La etiqueta de la consulta para la API de la
	 *            WorldHealthOrganization
	 * @return la url completa para hacer la llamada
	 */
	private String construyeUrl(String label) {
		StringBuilder str = new StringBuilder();

		// Todas las url's comienzan igual, tenemos eso guardado en el fichero
		// de properties
		str.append((String) properties.get(key + "_URL_INIT"));

		// La etiqueta especifica de cada consulta
		str.append(label);

		// Todas las url's acaban igual tambien
		str.append((String) properties.get(key + "_URL_END"));

		return str.toString();
	}

	/**
	 * Hace una consulta a la API de la WorldHealthOrganization preguntando por
	 * todas las listas de observaciones disponibles, parsea ese JSON y se queda
	 * con la etiqueta (label) para hacer la llamada y el indicador (display) de
	 * esas observaciones
	 */
	@Override
	public void preparar() {
		String url = (String) properties.get(key + "_LIST");
		consultasDisponibles = new HashMap<String, String>(); // Label - Display
		BufferedReader br;
		JsonParser parser;

		// ********************
		try {
			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/crawler/downloads/who/listLabelObservationsWorldHealthOrganization.json");
			FileUtils.copyURLToFile(new URL(url), file);
			br = new BufferedReader(new FileReader(file));

			parser = new JsonParser();

			JsonObject fichero = parser.parse(br).getAsJsonObject();

			JsonArray arrayDimension = fichero.getAsJsonObject()
					.get("dimension").getAsJsonArray();

			JsonArray arrayCode = arrayDimension.get(0).getAsJsonObject()
					.get("code").getAsJsonArray();

			for (int i = 0; i < arrayCode.size(); i++) {

				/*
				 * De cada consulta disponible nos quedamos con su "label"
				 * (clave a añadir en la url para hacer la consulta a la API) y
				 * su "display" (nuestro indicador)
				 */

				consultasDisponibles.put(arrayCode.get(i).getAsJsonObject()
						.get("label").getAsString(), arrayCode.get(i)
						.getAsJsonObject().get("display").getAsString());

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Vamos guardando en local cada JSON de la lista de consultas Luego
	 * llamamos al parseador para que nos devuelva las observaciones ya listas e
	 * invocamos a insertaObservaciones()
	 */
	@Override
	public void start() {
		Iterator<Entry<String, String>> it = consultasDisponibles.entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<String, String> pairs = (Entry<String, String>) it.next();
			String label = pairs.getKey().toString();
			String display = pairs.getValue().toString();
			String url = construyeUrl(label);

			try {
				// Guardando el fichero y trabajando sobre la version local
				File file = new File("public/crawler/downloads/who/" + label
						+ ".json");

				FileUtils.copyURLToFile(new URL(url), file);

				Provider provider = getProvider(
						(String) properties.get(key + "_NAME"),
						(String) properties.get(key + "_COUNTRY"),
						(String) properties.get(key + "_TYPE"));
				Submission submission = new Submission(new Date(), user);

				Indicator indicator = new Indicator(display);
				miParser = ParserFactory.getParser("json");
				miParser.setFile(file);
				miParser.setKeySearch((String) properties.get(key + "_KEY"));
				miParser.setIndicator(indicator);
				miParser.setProvider(provider);
				miParser.setSubmission(submission);

				observations = miParser.getParsedObservations();
				insertaObservaciones();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
