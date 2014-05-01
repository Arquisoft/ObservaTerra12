package parser.conectores;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import model.Country;
import model.Observation;
import model.Provider;
import model.User;
import parser.Parser;
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

	Properties properties;
	AreasDAO areasDao;
	OrganizacionesDAO organizacionesDao;
	EntradasDAO entradasDao;
	UsuariosDAO usersDao;
	ObservacionesDAO observacionesDao;
	User user;
	Parser miParser;
	String key;
	List<Observation> observations;
	static final String PROPERTIES = "public/crawler/configuration/conector.properties";

	protected void inicializaConector() throws IOException {
		cargaProperties();
		usersDao = PersistenceFactory.createUsuariosDAO();
		organizacionesDao = PersistenceFactory.createOrganizacionesDAO();
		areasDao = PersistenceFactory.createAreasDAO();
		entradasDao = PersistenceFactory.createEntradasDAO();
		observacionesDao = PersistenceFactory.createObservacionesDAO();
		setUser();
	}

	public void preparar() {
	}

	public void start() {
	}

	/**
	 * Carga el fichero de properties donde tenemos las consultas a la API
	 * 
	 * @param propertiesFileLocation
	 * @throws IOException
	 */
	private void cargaProperties() throws IOException {
		properties = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(PROPERTIES);
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
	protected Provider getProvider(String providerName,
			String providerCountryName, String providerTipo)
			throws SQLException {
		Provider provider = organizacionesDao.leerProvedor(providerName);
		if (provider == null) {
			Country providerCountry = areasDao.leerPais(providerCountryName);
			if (providerCountry == null) {
				providerCountry = new Country(providerCountryName);
				areasDao.crearArea(providerCountry);
				providerCountry = areasDao.leerPais(providerCountryName);
				System.out.println(providerCountry);
			}
			organizacionesDao.crearProveedor(new Provider(providerName,
					providerCountry, providerTipo));
			provider = organizacionesDao.leerProvedor(providerName);
		}

		return provider;
	}

	/**
	 * Inserta cada observacion de la lista en nuestra base de datos
	 * 
	 * @throws SQLException
	 */
	protected void insertaObservaciones() throws SQLException {
		for (Observation observacion : observations) {
			entradasDao.crearEntrada(observacion.getSubmission());
			observacionesDao.insertarObservacion(observacion);
			// TODO: Quitar estos System.out de pruebas
			if (observacion.getIdObservation() == null)
				System.out
						.println("Insertando observacion: FALLO al insertar (La observacion ya existe)");
			else
				System.out.println("Insertando observacion: " + observacion);
		}
	}

	/**
	 * Recupera el usuario del Crawler para insertar datos
	 * 
	 */
	private void setUser() {
		try {
			user = usersDao.leerUsuario("crawler", "crawler");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
