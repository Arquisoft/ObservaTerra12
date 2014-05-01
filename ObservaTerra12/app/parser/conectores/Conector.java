package parser.conectores;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Clase abstracta que modela un conector con una API externa de la que
 * obtenemos datos
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public abstract class Conector {

	protected Properties properties;

	/**
	 * Carga el fichero de properties donde tenemos las consultas a la API
	 * 
	 * @param propertiesFileLocation
	 * @throws IOException
	 */
	protected void cargaProperties(String propertiesFileLocation)
			throws IOException {
		properties = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(propertiesFileLocation);
			properties.load(is);
		} catch (IOException e) {
			throw new IOException("Fichero de propiedades no encontrado");
		}

	}

}
