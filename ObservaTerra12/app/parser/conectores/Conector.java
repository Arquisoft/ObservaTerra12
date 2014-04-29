package parser.conectores;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public abstract class Conector {

	protected Properties properties;

	protected void cargaProperties(String propertiesFileLocation) {
		properties = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(propertiesFileLocation);
			properties.load(is);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

}
