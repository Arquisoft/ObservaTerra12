package parser.conectores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.Indicator;
import model.Provider;
import model.Submission;

import org.apache.commons.io.FileUtils;

import parser.ParserFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * Clase conector con la API de la World Health Organization
 * 
 * 
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorWorldHealthOrganization extends Conector {

	private static ConectorWorldHealthOrganization instance;
	private Map<String, String> consultasDisponibles;

	private ConectorWorldHealthOrganization(String keyBusquedaProperties)
			throws IOException {
		this.keyBusquedaProperties = keyBusquedaProperties;
	}

	public static ConectorWorldHealthOrganization getInstance(
			String keyBusquedaProperties) throws IOException {
		if (instance == null) {
			instance = new ConectorWorldHealthOrganization(
					keyBusquedaProperties);
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
		str.append((String) properties.get(keyBusquedaProperties + "_URL_INIT"));

		// La etiqueta especifica de cada consulta
		str.append(label);

		// Todas las url's acaban igual tambien
		str.append((String) properties.get(keyBusquedaProperties + "_URL_END"));

		return str.toString();
	}

	/**
	 * Hace una consulta a la API de la WorldHealthOrganization preguntando por
	 * todas las listas de observaciones disponibles, parsea ese JSON y se queda
	 * con la etiqueta (label) para hacer la llamada y el indicador (display) de
	 * esas observaciones
	 */
	@Override
	public void preparar() throws IOException {
		super.preparar();
		String url = (String) properties.get(keyBusquedaProperties + "_LIST");
		consultasDisponibles = new HashMap<String, String>(); // Label - Display
		BufferedReader br;
		JsonParser parser;

		// ********************
		try {
			// Guardando el fichero y trabajando sobre la version local

			File fileListado = new File(
					"public/crawler/downloads/WHO/listLabelObservationsWorldHealthOrganization.json");
			FileUtils.copyURLToFile(new URL(url), fileListado);

			br = new BufferedReader(new FileReader(fileListado));

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
		}

	}

	/**
	 * Vamos guardando en local cada JSON de la lista de consultas Luego
	 * llamamos al parseador para que nos devuelva las observaciones ya listas e
	 * invocamos a insertaObservaciones()
	 */
	@Override
	public void start() {
		super.start();

		Iterator<Entry<String, String>> it = consultasDisponibles.entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<String, String> pairs = (Entry<String, String>) it.next();
			String label = pairs.getKey().toString();
			String display = pairs.getValue().toString();

			try {
				// Guardando el fichero y trabajando sobre la version local

				descargaFicheroJson(construyeUrl(label), label);

				Provider provider = generarProvider(
						(String) properties
								.get(keyBusquedaProperties + "_NAME"),
						(String) properties.get(keyBusquedaProperties
								+ "_COUNTRY"), (String) properties
								.get(keyBusquedaProperties + "_TYPE"));
				Submission submission = new Submission(new Date(), user);

				Indicator indicator = new Indicator(display);
				miParser = ParserFactory.getParser(
						keyBusquedaProperties,
						(String) properties.get(keyBusquedaProperties
								+ "_FORMAT"));
				miParser.setFile(file);
				miParser.setKeySearch((String) properties
						.get(keyBusquedaProperties + "_KEY"));
				miParser.setIndicator(indicator);
				miParser.setProvider(provider);
				miParser.setSubmission(submission);

				observations = miParser.getParsedObservations();
				insertaObservaciones();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

}
