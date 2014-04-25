package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import model.Area;
import model.Country;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;
import persistencia.AreasDAO;
import persistencia.IndicadoresDAO;
import persistencia.MedidasDAO;
import persistencia.ObservacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.TiempoDAO;
import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.EntradasJdbcDAO;
import persistencia.JdbcDAOs.IndicadoresJdbcDAO;
import persistencia.JdbcDAOs.MedidasJdbcDAO;
import persistencia.JdbcDAOs.ObservacionesJdbcDAO;
import persistencia.JdbcDAOs.OrganizacionesJdbcDAO;
import persistencia.JdbcDAOs.TiempoJdbcDAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * Prueba de un conector de nuestra aplicacion con la API de la World Health
 * Organization
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class PruebaConectorWorldHealthOrganization {

	public static void main(String[] args) throws SQLException, IOException {

		ConectorWorldHealthOrganization conectorWHO = new ConectorWorldHealthOrganization();
		conectorWHO.rellenaObservaciones("LIFE_EXPECTANCY_AT_BIRTH");
		// conectorWHO.rellenaPaises("LIST_COUNTRIES");

		printObservaciones();

	}

	public static void printObservaciones() {

		ObservacionesDAO obsDao = PersistenceFactory.createObservacionesDAO();
		AreasDAO areasDao = PersistenceFactory.createAreasDAO();
		IndicadoresDAO indicatorDao = PersistenceFactory.createIndicadoresDAO();
		MedidasDAO medidasDao = PersistenceFactory.createMedidasDAO();
		TiempoDAO tiempoDao = PersistenceFactory.createTiempoDAO();

		List<Observation> lista;

		System.out.println("/n**** LISTADO DE OBSERVACIONES ****/n");

		try {
			lista = obsDao.listarTodasObservaciones();
			for (int i = 0; i < lista.size(); i++) {
				System.out.println(lista.get(i));
				System.out
						.println("\t"
								+ areasDao.leerPais(lista.get(i).getArea()
										.getIdArea()));
				System.out.println("\t"
						+ indicatorDao.leerIndicador(lista.get(i)
								.getIndicator().getIdIndicator()));
				System.out.println("\t"
						+ medidasDao.buscarMedidaPorIdentificador((lista.get(i)
								.getMeasure().getIdMeasure())));
				System.out.println("\t"
						+ tiempoDao.buscarIntervaloTiempo(lista.get(i)
								.getTime().getIdTime()));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
