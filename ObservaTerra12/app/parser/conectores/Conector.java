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
	protected ObservacionesDAO obsDao;
	protected User usuario;

	protected void preparaConector(String propertiesFileLocation)
			throws IOException {
		cargaProperties(propertiesFileLocation);
		usersDao = PersistenceFactory.createUsuariosDAO();
		organizacionesDao = PersistenceFactory.createOrganizacionesDAO();
		areasDao = PersistenceFactory.createAreasDAO();
		entradasDao = PersistenceFactory.createEntradasDAO();
		obsDao = PersistenceFactory.createObservacionesDAO();
		usuario = getCrawlerUser();
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
	protected Provider getProvider(String nombre, String pais, String tipo)
			throws SQLException {
		Provider provider = organizacionesDao.leerProvedor(nombre);
		if (provider == null) {
			Country country = areasDao.leerPais(pais);
			if (country == null) {
				areasDao.crearPais(new Country(pais));
				country = areasDao.leerPais(pais);
			}
			organizacionesDao
					.crearProveedor(new Provider(nombre, country, tipo));
			provider = organizacionesDao.leerProvedor(nombre);
		}

		return provider;
	}

	private User getCrawlerUser() {
		try {
			usuario = usersDao.leerUsuario("crawler", "crawler");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return usuario;
	}

}
