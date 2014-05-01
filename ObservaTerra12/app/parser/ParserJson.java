package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;

/**
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ParserJson extends AbstractParser {

	BufferedReader br;
	JsonParser parser;

	public ParserJson() {
		observations = new ArrayList<Observation>();
	}

	@Override
	public List<Observation> getParsedObservations() {
		try {
			br = new BufferedReader(new FileReader(file));
			parser = new JsonParser();

			JsonObject ficheroJsonOriginal = parser.parse(br).getAsJsonObject();

			JsonArray arrayJsonDatosObservaciones = ficheroJsonOriginal.getAsJsonObject().get(keySearch)
					.getAsJsonArray();

			for (int i = 0; i < arrayJsonDatosObservaciones.size(); i++) {

				try {
					Area area;

					// Comprobamos los dos posibles casos de ficheros de WHO

					JsonElement countryElement = (arrayJsonDatosObservaciones.get(i)
							.getAsJsonObject().get("COUNTRY"));

					if (countryElement != null) {
						area = new Country(arrayJsonDatosObservaciones.get(i)
								.getAsJsonObject().get("COUNTRY").getAsString());
					} else {
						area = new Area(arrayJsonDatosObservaciones.get(i).getAsJsonObject()
								.get("MGHEREG").getAsString());
					}

					// TODO: Leer bien el measure.unit del JSON
					Measure measure = new Measure(arrayJsonDatosObservaciones.get(i)
							.getAsJsonObject().get("Value").getAsString(),
							"prueba");

					String year = arrayJsonDatosObservaciones.get(i).getAsJsonObject()
							.get("YEAR").getAsString();
					Date startDate;

					startDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-01-01 00:00:00.000000");

					Date endDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss.SSSSSS").parse(year
							+ "-12-31 23:59:59.000000");
					Time time = new Time(startDate, endDate);

					// Add observacion a la base de datos

					Observation obs = new Observation(area, indicator, measure,
							time, provider, submission);

					observations.add(obs);

					// TODO: Quitar estos System.out de pruebas
				} catch (NullPointerException e) {
					System.out
							.println("Insertando observacion: FALLO al insertar (Formato no compatible)");
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
