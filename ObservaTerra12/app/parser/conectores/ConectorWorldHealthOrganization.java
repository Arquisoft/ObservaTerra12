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

import parser.ParserJsonCasia;

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
	private String key;
	private Map<String, String> disponibles;
	private ParserJsonCasia miParser;
	List<Observation> observations;

	private ConectorWorldHealthOrganization(String key) throws IOException {
		preparaConector("public/crawler/configuration/conector.properties");
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
		str.append((String) properties.get(key + "_INIT"));

		// La etiqueta especifica de cada consulta
		str.append(label);

		// Todas las url's acaban igual tambien
		str.append((String) properties.get(key + "_END"));

		return str.toString();
	}

	/**
	 * Hace una consulta a la API de la WorldHealthOrganization preguntando por
	 * todas las listas de observaciones disponibles, parsea ese JSON y se queda
	 * con la etiqueta (label) para hacer la llamada y el indicador (display) de
	 * esas observaciones
	 */
	public void preparar() {
		String url = (String) properties.get(key + "_LIST");
		disponibles = new HashMap<String, String>(); // Label - Display
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

				disponibles.put(arrayCode.get(i).getAsJsonObject().get("label")
						.getAsString(),
						arrayCode.get(i).getAsJsonObject().get("display")
								.getAsString());

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Vamos guardando en local cada JSON de la lista de consultas, lo parseamos
	 * e insertamos en la base de datos las observaciones
	 */
	public void start() {
		Iterator<Entry<String, String>> it = disponibles.entrySet().iterator();

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

				Provider provider = getProvider("World Health Organization",
						"Switzerland", "ONG");
				Submission submission = new Submission(new Date(), user);

				Indicator indicator = new Indicator(display);
				miParser = new ParserJsonCasia(file, "fact", provider,
						submission, indicator);

				observations = miParser.parsea();
				insertaObservaciones();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	private void insertaObservaciones() throws SQLException {
		for (Observation observacion : observations) {
			entradasDao.crearEntrada(observacion.getSubmission());
			observacionesDao.insertarObservacion(observacion);
		}
	}

}
