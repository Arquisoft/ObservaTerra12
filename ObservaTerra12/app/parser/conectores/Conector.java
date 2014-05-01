package parser.conectores;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import model.Country;
import model.Provider;
import model.User;
import persistencia.AreasDAO;
import persistencia.EntradasDAO;
import persistencia.ObservacionesDAO;
import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.UsuariosDAO;

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
	protected AreasDAO areasDao;
	protected OrganizacionesDAO organizacionesDao;
	protected EntradasDAO entradasDao;
	protected UsuariosDAO usersDao;
	protected ObservacionesDAO observacionesDao;
	protected User user;

	protected void preparaConector(String propertiesFileLocation)
			throws IOException {
		cargaProperties(propertiesFileLocation);
		usersDao = PersistenceFactory.createUsuariosDAO();
		organizacionesDao = PersistenceFactory.createOrganizacionesDAO();
		areasDao = PersistenceFactory.createAreasDAO();
		entradasDao = PersistenceFactory.createEntradasDAO();
		observacionesDao = PersistenceFactory.createObservacionesDAO();
		user = getCrawlerUser();
	}

	/**
	 * Carga el fichero de properties donde tenemos las consultas a la API
	 * 
	 * @param propertiesFileLocation
	 * @throws IOException
	 */
	private void cargaProperties(String propertiesFileLocation)
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

	/**
	 * Recupera el provider de la organizacion o lo crea en el caso de que no
	 * exista
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Provider getProvider(String providerName, String providerCountryName, String providerTipo)
			throws SQLException {
		Provider provider = organizacionesDao.leerProvedor(providerName);
		if (provider == null) {
			Country providerCountry = areasDao.leerPais(providerCountryName);
			if (providerCountry == null) {
				areasDao.crearPais(new Country(providerCountryName));
				providerCountry = areasDao.leerPais(providerCountryName);
			}
			organizacionesDao
					.crearProveedor(new Provider(providerName, providerCountry, providerTipo));
			provider = organizacionesDao.leerProvedor(providerName);
		}

		return provider;
	}

	private User getCrawlerUser() {
		try {
			user = usersDao.leerUsuario("crawler", "crawler");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

}
