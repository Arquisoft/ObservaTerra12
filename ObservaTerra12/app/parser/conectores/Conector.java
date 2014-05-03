package parser.conectores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import model.Country;
import model.Observation;
import model.Provider;
import model.Time;
import model.User;
import parser.Parser;
import persistencia.AreasDAO;
import persistencia.EntradasDAO;
import persistencia.ObservacionesDAO;
import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.TiempoDAO;
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
	String keyBusquedaProperties;
	File file;
	List<Observation> observations;
	static final String PROPERTIES = "public/crawler/configuration/conector.properties";

	/**
	 * Inicializa todos los parametros necesarios antes de la ejecucion
	 * 
	 * @throws IOException
	 */
	public void preparar() throws IOException {
		cargaProperties();
		startDaos();
		setUser();
	}

	/**
	 * Comienza la ejecucion del conector
	 */
	public void start() {
	}

	private void startDaos() {
		usersDao = PersistenceFactory.createUsuariosDAO();
		organizacionesDao = PersistenceFactory.createOrganizacionesDAO();
		areasDao = PersistenceFactory.createAreasDAO();
		entradasDao = PersistenceFactory.createEntradasDAO();
		observacionesDao = PersistenceFactory.createObservacionesDAO();
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
	protected Provider generarProvider(String providerName,
			String providerCountryName, String providerTipo)
			throws SQLException {
		Provider provider = organizacionesDao.leerProvedor(providerName);
		if (provider == null) {
			Country providerCountry = areasDao.leerPais(providerCountryName);
			if (providerCountry == null) {
				providerCountry = new Country(providerCountryName);
				areasDao.crearArea(providerCountry);
				providerCountry = areasDao.leerPais(providerCountryName);
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

			TiempoDAO tiempoDao = PersistenceFactory.createTiempoDAO();

			Date startDate = observacion.getTime().getStartDate();
			Date endDate = observacion.getTime().getEndDate();

			Time time = tiempoDao.buscarIntervaloTiempo(startDate, endDate);
			if (time != null)
				observacion.setTime(time);

			entradasDao.crearEntrada(observacion.getSubmission());
			observacionesDao.insertarObservacion(observacion);

			// TODO: Quitar estos System.out de pruebas
			if (observacion.getIdObservation() == null)
				System.out.println("Insertando observacion: FALLO al insertar");
			else
				System.out.println("Insertada observacion: " + observacion);
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

	protected void descargaFicheroJson(String url, String label) {
		try {
			file = new File("public/crawler/downloads/" + keyBusquedaProperties
					+ "/" + label + ".json");

			FileUtils.copyURLToFile(new URL(url), file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
