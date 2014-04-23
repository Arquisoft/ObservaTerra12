package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Submission;
import model.Time;
import persistencia.MedidasDAO;
import persistencia.ObservacionesDAO;
import persistencia.TiempoDAO;
import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.MedidasJdbcDAO;
import persistencia.JdbcDAOs.ObservacionesJdbcDAO;
import persistencia.JdbcDAOs.OrganizacionesJdbcDAO;
import persistencia.JdbcDAOs.TiempoJdbcDAO;

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
public class PruebaConectorWorldHealthOrganization {

	public static void main(String[] args) throws SQLException, IOException {

		// rellenaPaises();
		rellenaObservacionesPrueba1();

	}

	/*
	 * Prueba 1 de observaciones
	 * 
	 * La busqueda es para GHO: "Life expectancy at birth (years)"
	 */
	public static void rellenaObservacionesPrueba1() {
		String url = "http://apps.who.int/gho/athena/api/GHO/WHOSIS_000001.json?profile=simple";

		BufferedReader br;
		JsonParser parser;
		ArrayList<String> campos = new ArrayList<String>();
		ObservacionesDAO obsDao = null;

		try {
			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/pruebasCrawler/observationsPrueba1WorldHealthOrganization.json");
			// FileUtils.copyURLToFile(new URL(url), file);
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

			for (int i = 0; i < arrayObjetivo.size(); i++) {

				// Map<String, String> map = new HashMap<String, String>();

				// Nueva observacion

				Observation obs = new Observation();

				//

				Country country = new Country();
				country.setName(arrayObjetivo.get(i).getAsJsonObject()
						.get("COUNTRY").getAsString());
				AreasJdbcDAO areasDao = new AreasJdbcDAO();
				areasDao.crearArea(country);
				obs.setArea(country);

				//

				Indicator indicator = new Indicator();
				indicator.setNombre(arrayObjetivo.get(i).getAsJsonObject()
						.get("GHO").getAsString());

				obs.setIndicator(indicator);

				//

				Measure measure = new Measure();
				measure.setValue(arrayObjetivo.get(i).getAsJsonObject()
						.get("Value").getAsString());
				measure.setUnit("");
				MedidasDAO medidasDao = new MedidasJdbcDAO();
				medidasDao.crearMedida(measure);
				obs.setMeasure(measure);

				//

				Time time = new Time();

				String year = arrayObjetivo.get(i).getAsJsonObject()
						.get("YEAR").getAsString();
				Date startDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-01-01 00:00:00.000000");
				Date endDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-12-31 23:59:59.000000");
				time.setStartDate(startDate);
				time.setEndDate(endDate);
				TiempoDAO tiempoDao = new TiempoJdbcDAO();
				tiempoDao.crearIntervalo(time);
				obs.setTime(time);

				//

				Provider provider = new Provider();
				provider.setNombre("prueba");
				OrganizacionesJdbcDAO orgDao = new OrganizacionesJdbcDAO();
				orgDao.crearOrganizacion(provider);
				obs.setProvider(provider);

				Submission submission = new Submission();

				obs.setSubmission(submission);

				// Add observacion a la base de datos

				obsDao = new ObservacionesJdbcDAO();

				try {
					System.out.println(obs.getIdObservacion());
					obsDao.insertarObservacion(obs);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			for (int i = 0; i < 100; i++) {
				System.out.println(obsDao
						.buscarObservacionPorIdentificador(Integer
								.toUnsignedLong(i)));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}

	public static void rellenaPaises() {

		String url = "http://apps.who.int/gho/athena/api/COUNTRY?format=json";

		InputStream is;
		BufferedReader br;
		AreasJdbcDAO areasDao;
		JsonParser parser;

		try {

			// Trabajamos sobre la version web
			// is = new URL(url).openStream();
			// br = new BufferedReader(new InputStreamReader(is,
			// Charset.forName("UTF-8")));

			// Guardando el fichero y trabajando sobre la version local
			File file = new File(
					"public/pruebasCrawler/countriesWorldHealthOrganization.json");
			// org.apache.commons.io.FileUtils.copyURLToFile(new URL(url),
			// file);
			br = new BufferedReader(new FileReader(file));

			// ********************

			parser = new JsonParser();

			JsonArray array = parser.parse(br).getAsJsonObject()
					.get("dimension").getAsJsonArray();

			JsonArray arrayPaises = array.get(0).getAsJsonObject().get("code")
					.getAsJsonArray();

			for (int i = 1; i < arrayPaises.size(); i++) {

				areasDao = new AreasJdbcDAO();

				String code = arrayPaises.get(i).getAsJsonObject().get("label")
						.getAsString();
				String name = arrayPaises.get(i).getAsJsonObject()
						.get("display").getAsString();
				Country country = new Country();
				country.setName(name);
				areasDao.crearArea(country);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
