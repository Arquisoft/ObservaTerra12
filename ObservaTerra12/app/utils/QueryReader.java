package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class QueryReader {

	private static String fileLocation = "public/BaseDatosUsuarios/queries.properties";

	private static QueryReader instance;

	public static QueryReader instanciar() {
		if (instance == null) {
			instance = new QueryReader();
		}
		return instance;
	}

	public String leerPropiedad(String propiedad) {
		Properties properties = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(fileLocation);
			properties.load(is);
			return (String) properties.get(propiedad);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

		return null;
	}

}
