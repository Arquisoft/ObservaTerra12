package tests.parser;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import model.Country;
import model.Observation;
import model.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.conectores.ConectorUnitedNations;
import parser.conectores.ConectorWorldHealthOrganization;
import persistencia.AreasDAO;
import persistencia.IndicadoresDAO;
import persistencia.MedidasDAO;
import persistencia.ObservacionesDAO;
import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;
import persistencia.TiempoDAO;
import persistencia.JdbcDAOs.AreasJdbcDAO;

/**
 * 
 * Prueba de varios conectores
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class ConectorTest {

	private Country country1;
	ConectorWorldHealthOrganization conectorWHO;
	ConectorUnitedNations conectorUN;
	AreasDAO areasDao;
	ObservacionesDAO obsDao;
	IndicadoresDAO indicatorDao;
	MedidasDAO medidasDao;
	TiempoDAO tiempoDao;
	OrganizacionesDAO orgsDao;

	@Before
	public void before() {
		areasDao = PersistenceFactory.createAreasDAO();
		obsDao = PersistenceFactory.createObservacionesDAO();
		indicatorDao = PersistenceFactory.createIndicadoresDAO();
		medidasDao = PersistenceFactory.createMedidasDAO();
		tiempoDao = PersistenceFactory.createTiempoDAO();
		orgsDao = PersistenceFactory.createOrganizacionesDAO();

		this.country1 = new Country();
		country1.setName("Australia");

		conectorWHO = ConectorWorldHealthOrganization.getInstance("WHO");
		conectorUN = ConectorUnitedNations.getInstance();
	}

	@Test
	public void testUN() {

		conectorUN.rellenaObservaciones("COMPONENTS");
		try {

			assertTrue(areasDao.leerPais("Australia").getName()
					.equals(country1.getName()));

			assertTrue(areasDao.leerPais("Spain").getName().equals("Spain"));

			List<Provider> lista = orgsDao.listarProveedores();
			Provider prueba = null;

			for (Provider proveedor : lista) {
				if (proveedor.getNombre().equals("United Nations"))
					prueba = proveedor;
			}

			assertTrue(prueba != null);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * CUIDADO: Este test descarga todos los JSON de la API de la World Health
	 * Organization, los analiza e intenta insertar las observaciones. En total
	 * son 28 JSON y mas de 1000 observaciones y le lleva un rato largo
	 */
	@Test
	public void testWHO() {
		conectorWHO.preparar();
		conectorWHO.start();

		try {

			List<Provider> lista = orgsDao.listarProveedores();
			Provider prueba = null;

			for (Provider proveedor : lista) {
				if (proveedor.getNombre().equals("World Health Organization"))
					prueba = proveedor;
			}

			assertTrue(prueba != null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {

		printObservations(0);
	}

	/*
	 * limite: Para no imprimir todo el listado podemos especificarle un limite
	 * e imprimira desde 0 hasta ese limite. Limite = 0 => Sin limite
	 */
	public void printObservations(int limite) {
		List<Observation> lista;

		System.out.println("/n**** LISTADO DE OBSERVACIONES ****/n");

		try {
			lista = obsDao.listarTodasObservaciones();
			if (limite == 0)
				limite = lista.size();
			for (int i = 0; i < limite; i++) {
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
				System.out.println("\t"
						+ orgsDao.leerProvedor(lista.get(i).getProvider()
								.getIdOrganization()));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}