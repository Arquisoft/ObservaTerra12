package utils;

import java.io.IOException;
import java.util.Properties;

public class LectorConsultas {

private static final String CONF_FILE = "consultas.properties";
	
	private static LectorConsultas instance;
	private Properties properties;

	private LectorConsultas() {
		properties = new Properties();
		try {
			properties.load(LectorConsultas.class.getClassLoader().getResourceAsStream(CONF_FILE));
		} catch (IOException e) {
			throw new RuntimeException("Propeties file can not be loaded", e);
		}
	}
	
	public static String get(String key) {
		return getInstance().getProperty( key );
	}

	private String getProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new RuntimeException("Property not found in config file");
		}
		return value;
	}

	private static LectorConsultas getInstance() {
		if (instance == null) {
			instance = new LectorConsultas();
		}
		return instance;
	}
}
