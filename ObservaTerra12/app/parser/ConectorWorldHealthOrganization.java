package parser;

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
import java.util.Properties;

import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;

import org.apache.commons.io.FileUtils;

import persistencia.ObservacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.EntradasJdbcDAO;

import com.google.gson.JsonArray;
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
public class ConectorWorldHealthOrganization {

	Properties properties;

	public ConectorWorldHealthOrganization() {
		cargaProperties();
	}

	private void cargaProperties() {
		properties = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream("public/crawler/who.properties");
			properties.load(is);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	public void rellenaObservaciones(String key) {

		String url = (String) properties.get(key);

		BufferedReader br;
		JsonParser parser;
		ArrayList<String> campos = new ArrayList<String>();
		ObservacionesDAO obsDao = null;

		try {
			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/crawler/temp/observationsPrueba1WorldHealthOrganization.json");
			FileUtils.copyURLToFile(new URL(url), file);
			br = new BufferedReader(new FileReader(file));

			// ********************

			parser = new JsonParser();

			JsonObject fichero = parser.parse(br).getAsJsonObject();

			JsonArray arrayCampos = fichero.getAsJsonObject().get("dimension")
					.getAsJsonArray();

			JsonArray arrayObjetivo = fichero.getAsJsonObject().get("fact")
					.getAsJsonArray();

			for (int i = 0; i < arrayCampos.size(); i++) {
				campos.add(arrayCampos.get(i).getAsJsonObject().get("label")
						.getAsString());
			}

			// arrayObjetivo.size()
			for (int i = 0; i < arrayObjetivo.size(); i++) {

				Country country = new Country(arrayObjetivo.get(i)
						.getAsJsonObject().get("COUNTRY").getAsString());

				Indicator indicator = new Indicator(arrayObjetivo.get(i)
						.getAsJsonObject().get("GHO").getAsString());

				// TODO: Leer bien el measure.unit del JSON
				Measure measure = new Measure(arrayObjetivo.get(i)
						.getAsJsonObject().get("Value").getAsString(), "prueba");

				String year = arrayObjetivo.get(i).getAsJsonObject()
						.get("YEAR").getAsString();
				Date startDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-01-01 00:00:00.000000");
				Date endDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-12-31 23:59:59.000000");
				Time time = new Time(startDate, endDate);

				// TODO
				Provider provider = new Provider("nombreprueba", country,
						"tipoorganizacionprueba");

				// TODO
				User usuario = new User();
				usuario.setIdUser(1L);
				Submission submission = new Submission(new Date(), usuario);
				EntradasJdbcDAO entradasDao = new EntradasJdbcDAO();
				entradasDao.crearEntrada(submission);

				// Add observacion a la base de datos

				obsDao = PersistenceFactory.createObservacionesDAO();
				Observation obs = new Observation(country, indicator, measure,
						time, provider, submission);

				try {
					obsDao.insertarObservacion(obs);
					if (obs.getIdObservation() == null)
						System.out
								.println("Insertando observacion: FALLO al insertar");
					else
						System.out.println("Insertando observacion: " + obs);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
			// } catch (MalformedURLException e1) {
			//
			// e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void rellenaPaises(String key) {
		String url = (String) properties.get(key);

		InputStream is;
		BufferedReader br;
		AreasJdbcDAO areasDao = null;
		JsonParser parser;

		try {

			// Trabajamos sobre la version web
			// is = new URL(url).openStream();
			// br = new BufferedReader(new InputStreamReader(is,
			// Charset.forName("UTF-8")));

			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/crawler/temp/countriesWorldHealthOrganization.json");
			// org.apache.commons.io.FileUtils.copyURLToFile(new URL(url),
			// file);
			br = new BufferedReader(new FileReader(file));

			// ********************

			parser = new JsonParser();

			JsonArray array = parser.parse(br).getAsJsonObject()
					.get("dimension").getAsJsonArray();

			JsonArray arrayPaises = array.get(0).getAsJsonObject().get("code")
					.getAsJsonArray();
			areasDao = new AreasJdbcDAO();
			for (int i = 1; i < arrayPaises.size(); i++) {

				String code = arrayPaises.get(i).getAsJsonObject().get("label")
						.getAsString();
				String name = arrayPaises.get(i).getAsJsonObject()
						.get("display").getAsString();
				Country country = new Country(name);
				areasDao.crearArea(country);

			}

			System.out.println(areasDao.leerPais(new Long(5)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// } catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
