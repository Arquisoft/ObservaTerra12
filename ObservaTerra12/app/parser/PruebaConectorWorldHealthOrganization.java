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
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;
import persistencia.MedidasDAO;
import persistencia.ObservacionesDAO;
import persistencia.TiempoDAO;
import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.IndicadoresJdbcDAO;
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

			// Metodo para pruebas
			borraTodasObservacionesDeLaBaseDeDatos();

			// arrayObjetivo.size()
			for (int i = 0; i < arrayObjetivo.size(); i++) {

				// Map<String, String> map = new HashMap<String, String>();

				// Nueva observacion

				//

				Country country = new Country(arrayObjetivo.get(i)
						.getAsJsonObject().get("COUNTRY").getAsString());
				AreasJdbcDAO areasDao = new AreasJdbcDAO();
				if (areasDao.leerPais(country.getName()) != null)
					areasDao.crearArea(country);

				//

				Indicator indicator = new Indicator(arrayObjetivo.get(i)
						.getAsJsonObject().get("GHO").getAsString());
				IndicadoresJdbcDAO indicatorDao = new IndicadoresJdbcDAO();
				if (indicatorDao.leerIndicador(indicator.getNombre()) != null)
					indicatorDao.añadirIndicador(indicator);

				//

				// TODO: Leer bien el measure.unit del JSON
				Measure measure = new Measure(arrayObjetivo.get(i)
						.getAsJsonObject().get("Value").getAsString(), "prueba");
				//
				MedidasDAO medidasDao = new MedidasJdbcDAO();
				medidasDao.crearMedida(measure);

				//

				String year = arrayObjetivo.get(i).getAsJsonObject()
						.get("YEAR").getAsString();
				Date startDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-01-01 00:00:00.000000");
				Date endDate = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
						+ "-12-31 23:59:59.000000");
				Time time = new Time(startDate, endDate);

				TiempoDAO tiempoDao = new TiempoJdbcDAO();
				tiempoDao.crearIntervalo(time);

				//

				// TODO
				Provider provider = new Provider("nombreprueba", country,
						"tipoorganizacionprueba");
				OrganizacionesJdbcDAO orgDao = new OrganizacionesJdbcDAO();

				orgDao.crearOrganizacion(provider);

				// TODO

				User usuario = new User();
				usuario.setIdUser(1L);
				Submission submission = new Submission(new Date(), usuario);

				// Add observacion a la base de datos

				Observation obs = new Observation(country, indicator, measure,
						time, provider, submission);

				obsDao = new ObservacionesJdbcDAO();

				try {
					obsDao.insertarObservacion(obs);
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

		print();

	}

	/*
	 * Metodo creado para hacer las pruebas mas rapido
	 */
	private static void borraTodasObservacionesDeLaBaseDeDatos() {
		ObservacionesDAO obsDao = new ObservacionesJdbcDAO();
		List<Observation> observaciones;

		try {
			observaciones = obsDao.listarTodasObservaciones();
			for (int i = 0; i < observaciones.size(); i++) {
				obsDao.eliminarObservacion(observaciones.get(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void print() {
		ObservacionesDAO obsDao = new ObservacionesJdbcDAO();
		List<Observation> lista;
		System.out.println();
		System.out.println("**** LISTADO DE OBSERVACIONES ****");
		System.out.println();

		AreasJdbcDAO areasDao = new AreasJdbcDAO();
		IndicadoresJdbcDAO indicatorDao = new IndicadoresJdbcDAO();
		MedidasDAO medidasDao = new MedidasJdbcDAO();
		TiempoDAO tiempoDao = new TiempoJdbcDAO();

		try {
			lista = obsDao.listarTodasObservaciones();
			for (int i = 0; i < lista.size(); i++) {
				System.out.println(lista.get(i));
				System.out
						.println("\t"
								+ areasDao.leerPais(lista.get(i).getArea()
										.getIdArea()));
				System.out.println("\t"
						+ indicatorDao.leerIndicador(lista.get(i)
								.getIndicator().getIdIndicator()));
				System.out.println("\t"
						+ medidasDao.buscarMedidaPorIdentificador((lista.get(i)
								.getMeasure().getIdMeasure())));
				System.out.println("\t"
						+ tiempoDao.buscarIntervaloTiempo(lista.get(i)
								.getTime().getIdTime()));

			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				Country country = new Country(name);
				areasDao.crearArea(country);

			}

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
