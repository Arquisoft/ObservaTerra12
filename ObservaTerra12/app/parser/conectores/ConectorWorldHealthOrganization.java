package parser.conectores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import persistencia.ObservacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.EntradasJdbcDAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * Prueba de un conector de nuestra aplicacion con la API de la World Health
 * Organization
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorWorldHealthOrganization extends Conector {

	private static ConectorWorldHealthOrganization instance;
	private String key;
	private Map<String, String> disponibles;

	private ConectorWorldHealthOrganization(String key) {
		cargaProperties("public/crawler/configuration/conector.properties");
		disponibles = new HashMap<String, String>(); // Label - Display
		this.key = key;
	}

	public static ConectorWorldHealthOrganization getInstance(String key) {
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
		str.append((String) properties.get(key + "_INIT"));
		str.append(label);
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
		BufferedReader br;
		JsonParser parser;

		// ********************
		try {
			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/crawler/temp/downloads/listLabelObservationsWorldHealthOrganization.json");
			// FileUtils.copyURLToFile(new URL(url), file);
			br = new BufferedReader(new FileReader(file));

			parser = new JsonParser();

			JsonObject fichero = parser.parse(br).getAsJsonObject();

			JsonArray arrayDimension = fichero.getAsJsonObject()
					.get("dimension").getAsJsonArray();

			JsonArray arrayCode = arrayDimension.get(0).getAsJsonObject()
					.get("code").getAsJsonArray();

			for (int i = 0; i < arrayCode.size(); i++) {

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
		BufferedReader br;
		JsonParser parser;
		ArrayList<String> campos = new ArrayList<String>();
		ObservacionesDAO obsDao = null;

		Iterator<Entry<String, String>> it = disponibles.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			String label = pairs.getKey().toString();
			String display = pairs.getValue().toString();
			String url = construyeUrl(label);

			try {
				// Guardando el fichero y trabajando sobre la version local
				File file = new File("public/crawler/temp/downloads/" + label
						+ ".json");
				FileUtils.copyURLToFile(new URL(url), file);
				br = new BufferedReader(new FileReader(file));
				parser = new JsonParser();

				JsonObject fichero = parser.parse(br).getAsJsonObject();

				JsonArray arrayCampos = fichero.getAsJsonObject()
						.get("dimension").getAsJsonArray();

				JsonArray arrayObjetivo = fichero.getAsJsonObject().get("fact")
						.getAsJsonArray();

				for (int i = 0; i < arrayCampos.size(); i++) {
					campos.add(arrayCampos.get(i).getAsJsonObject()
							.get("label").getAsString());
				}

				for (int i = 0; i < arrayObjetivo.size(); i++) {

					try {
						Area area;

						JsonElement countryElement = (arrayObjetivo.get(i)
								.getAsJsonObject().get("COUNTRY"));

						if (countryElement != null) {
							area = new Country(arrayObjetivo.get(i)
									.getAsJsonObject().get("COUNTRY")
									.getAsString());
						} else {
							area = new Area(arrayObjetivo.get(i)
									.getAsJsonObject().get("MGHEREG")
									.getAsString());
						}

						Indicator indicator = new Indicator(display);

						// TODO: Leer bien el measure.unit del JSON
						Measure measure = new Measure(arrayObjetivo.get(i)
								.getAsJsonObject().get("Value").getAsString(),
								"prueba");

						String year = arrayObjetivo.get(i).getAsJsonObject()
								.get("YEAR").getAsString();
						Date startDate = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
								+ "-01-01 00:00:00.000000");
						Date endDate = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
								+ "-12-31 23:59:59.000000");
						Time time = new Time(startDate, endDate);

						// TODO: Rellenar aqui bien los datos de la World Health
						// Organization
						Country country = new Country();
						Provider provider = new Provider(
								"World Health Organization", country,
								"tipoorganizacionprueba");

						// TODO: Crear usuario "Crawler" en la base de datos e
						// indicar aqui que es el quien hace estas inserciones
						User usuario = new User();
						usuario.setIdUser(1L);
						Submission submission = new Submission(new Date(),
								usuario);
						EntradasJdbcDAO entradasDao = new EntradasJdbcDAO();
						entradasDao.crearEntrada(submission);

						// Add observacion a la base de datos

						obsDao = PersistenceFactory.createObservacionesDAO();
						Observation obs = new Observation(area, indicator,
								measure, time, provider, submission);

						obsDao.insertarObservacion(obs);

						// TODO: Quitar estos System.out de pruebas
						if (obs.getIdObservation() == null)
							System.out
									.println("Insertando observacion: FALLO al insertar (La observacion ya existe)");
						else
							System.out
									.println("Insertando observacion: " + obs);
					} catch (NullPointerException e) {
						System.out
								.println("Insertando observacion: FALLO al insertar (Formato no compatible)");
					}

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
