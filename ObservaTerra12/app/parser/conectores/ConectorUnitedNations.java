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

	private ConectorUnitedNations(String keyBusquedaProperties) {
		this.keyBusquedaProperties = keyBusquedaProperties;
	}

	public static ConectorUnitedNations getInstance(String keyBusquedaProperties)
			throws IOException {
		if (instance == null) {
			instance = new ConectorUnitedNations(keyBusquedaProperties);
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
					(String) properties.get(keyBusquedaProperties + "_NAME"),
					(String) properties.get(keyBusquedaProperties + "_COUNTRY"),
					(String) properties.get(keyBusquedaProperties + "_TYPE"));
			Submission submission = new Submission(new Date(), user);

			Indicator indicator = new Indicator(display);
			miParser = ParserFactory.getParser(keyBusquedaProperties,
					(String) properties.get(keyBusquedaProperties + "_FORMAT"));
			miParser.setFile(file);
			miParser.setKeySearch((String) properties.get(keyBusquedaProperties + "_KEY"));
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
