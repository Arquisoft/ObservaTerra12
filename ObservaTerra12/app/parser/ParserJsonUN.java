package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import persistencia.PersistenceFactory;
import persistencia.TiempoDAO;
import persistencia.implJdbc.TiempoJdbc;
import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ParserJsonUN extends AbstractParser {

	BufferedReader br;
	JsonParser parser;

	public ParserJsonUN() {
		observations = new ArrayList<Observation>();
	}

	@Override
	public List<Observation> getParsedObservations() {
		try {
			br = new BufferedReader(new FileReader(file));
			parser = new JsonParser();

			JsonArray arrayJsonOriginal = parser.parse(br).getAsJsonArray();

			// ********************************************************************************************

			// fichero.size()
			for (int i = 0; i < arrayJsonOriginal.size(); i++) {

				JsonElement value = arrayJsonOriginal.get(i).getAsJsonObject()
						.get("_2012_life_expectancy_at_birth");

				if (value != null) {

					Area area = new Country(arrayJsonOriginal.get(i)
							.getAsJsonObject().get("name").getAsString());

					Measure measure = new Measure(value.getAsString(), "years");

					String year = "2012";
					Date startDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-01-01 00:00:00.000000");
					Date endDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-12-31 23:59:59.000000");
					TiempoDAO tiempoDao = PersistenceFactory.createTiempoDAO();
					Time time = tiempoDao.buscarIntervaloTiempo(startDate,
							endDate);
					if (time == null)
						time = new Time(startDate, endDate);

					Observation obs = new Observation(area, indicator, measure,
							time, provider, submission);

					observations.add(obs);

					// TODO: Quitar estos System.out de pruebas

				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return observations;
	}

	@Override
	public void setTags(String countryTag, String indicatorTag,
			String measureTag, String timeTag) {
		// TODO Auto-generated method stub

	}

}
