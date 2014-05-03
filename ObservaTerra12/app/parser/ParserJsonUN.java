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
import com.google.gson.JsonParser;

/**
 * 
 * Parseador del formato JSON con los campos usados por la API de United Nations
 * a nuestro formato
 * 
 * 
 * VERSION FINAL
 * 
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

			for (int i = 0; i < arrayJsonOriginal.size(); i++) {

				JsonElement value = arrayJsonOriginal.get(i).getAsJsonObject()
						.get(busquedaTag);

				if (value != null) {

					Area area = new Country(arrayJsonOriginal.get(i)
							.getAsJsonObject().get("name").getAsString());

					Measure measure = new Measure(value.getAsString(), "years");

					Date startDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(timeTag
							+ "-01-01 00:00:00.000000");
					Date endDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(timeTag
							+ "-12-31 23:59:59.000000");
					Time time = new Time(startDate, endDate);

					Observation obs = new Observation(area, indicator, measure,
							time, provider, submission);

					observations.add(obs);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return observations;
	}

}
