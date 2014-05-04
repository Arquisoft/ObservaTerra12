package parser.conectores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import model.Indicator;
import model.Provider;
import model.Submission;
import parser.ParserFactory;

/**
 * 
 * Conector de nuestra aplicacion con la API de United Nations
 * 
 * 
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

		consulta("_2012_life_expectancy_at_birth", "Life Expectancy at birth",
				"2012");
		consulta("_2011_expected_years_of_schooling",
				"Expected years of schooling", "2011");
		consulta("_2010_mean_years_of_schooling", "Mean years of schooling",
				"2010");

	}

	private void consulta(String tagBusqueda, String display, String year) {

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
			miParser.setKeySearch((String) properties.get(keyBusquedaProperties
					+ "_KEY"));
			miParser.setIndicator(indicator);
			miParser.setProvider(provider);
			miParser.setSubmission(submission);
			miParser.setTags(tagBusqueda, display, year);

			observations = miParser.getParsedObservations();
			insertaObservaciones();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
