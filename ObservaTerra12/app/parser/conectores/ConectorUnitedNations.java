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
import java.util.Iterator;
import java.util.Map.Entry;

import parser.ParserFactory;
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
 * VERSION PRELIMINAR
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorUnitedNations extends Conector {

	private static ConectorUnitedNations instance;

	private ConectorUnitedNations(String key) {
		this.key = key;
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
		super.start();

		// String label = pairs.getKey().toString();
		String display = "Life Expectancy at birth";

		try {
			descargaFicheroJson("http://data.undp.org/resource/wxub-qc5k.json",
					"observationsPrueba1UnitedNations");

			Provider provider = generarProvider(
					(String) properties.get(key + "_NAME"),
					(String) properties.get(key + "_COUNTRY"),
					(String) properties.get(key + "_TYPE"));
			Submission submission = new Submission(new Date(), user);

			Indicator indicator = new Indicator(display);
			miParser = ParserFactory.getParser(key,
					(String) properties.get(key + "_FORMAT"));
			miParser.setFile(file);
			miParser.setKeySearch((String) properties.get(key + "_KEY"));
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
