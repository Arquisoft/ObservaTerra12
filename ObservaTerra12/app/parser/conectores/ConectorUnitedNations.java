package parser.conectores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import persistencia.PersistenceFactory;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * Prueba de un conector de nuestra aplicacion con la API de United Nations
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorUnitedNations extends Conector {

	private static ConectorUnitedNations instance;

	private ConectorUnitedNations(String key) {

	}

	public static ConectorUnitedNations getInstance(String key)
			throws IOException {
		if (instance == null) {
			instance = new ConectorUnitedNations(key);
		}
		return instance;
	}

	@Override
	public void preparar() throws IOException {
		super.preparar();
	}

	@Override
	public void start() {

		BufferedReader br;
		JsonParser parser;

		try {
			File file = new File(
					"public/crawler/temp/observationsPrueba1UnitedNations.json");
			// FileUtils.copyURLToFile(new URL(url), file);
			br = new BufferedReader(new FileReader(file));

			// ********************

			parser = new JsonParser();

			JsonArray fichero = parser.parse(br).getAsJsonArray();

			// ********************************************************************************************

			// fichero.size()
			for (int i = 0; i < fichero.size(); i++) {

				JsonElement value = fichero.get(i).getAsJsonObject()
						.get("_2012_life_expectancy_at_birth");

				if (value != null) {

					Country country = new Country(fichero.get(i)
							.getAsJsonObject().get("name").getAsString());

					Indicator indicator = new Indicator(
							"Life Expectancy at birth");

					Measure measure = new Measure(value.getAsString(), "years");

					String year = "2012";
					Date startDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-01-01 00:00:00.000000");
					Date endDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-12-31 23:59:59.000000");
					Time time = new Time(startDate, endDate);

					Provider provider = getProvider("United Nations",
							"United States of America", "ONG");

					Submission submission = new Submission(new Date(), user);
					entradasDao.crearEntrada(submission);

					// Add observacion a la base de datos

					Observation obs = new Observation(country, indicator,
							measure, time, provider, submission);

					observacionesDao.insertarObservacion(obs);
					if (obs.getIdObservation() == null)
						System.out
								.println("Insertando observacion: FALLO al insertar");
					else
						System.out.println("Insertando observacion: " + obs);

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}
}
