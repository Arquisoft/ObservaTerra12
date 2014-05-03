package tests.PersistenceTests;
import java.sql.SQLException;
import java.util.Date;

import model.Time;

import org.junit.Before;
import org.junit.Test;

import persistencia.PersistenceFactory;
import persistencia.TiempoDAO;

public class TiempoTest {

	private Time tiempo;

	@Before
	public void before() {
		this.tiempo = new Time();

		Date datebegining = new Date(System.currentTimeMillis() / 3);
		this.tiempo.setStartDate(datebegining);

		//Date dateEnd = new Date(System.currentTimeMillis() / 4);
		//this.tiempo.setEndDate(dateEnd);
	}

	@Test
	public void test() throws SQLException {
		testCrearYRecuperar();
		testBorrarYRecuperar();
	}

	private void testCrearYRecuperar() throws SQLException {

		// Crear la medida de tiempo
		TiempoDAO tiempoDAO = PersistenceFactory.createTiempoDAO();
		this.tiempo = tiempoDAO.crearIntervalo(this.tiempo);

		// Probar a recuperarla y comparar
		Time leida = tiempoDAO.buscarIntervaloTiempo(this.tiempo.getIdTime());
		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida, this.tiempo);
		
		//Recuperar por datos
		leida = tiempoDAO.buscarIntervaloTiempo(this.tiempo.getStartDate(), this.tiempo.getEndDate());
		org.junit.Assert.assertEquals(leida, this.tiempo);
	}

	private void testBorrarYRecuperar() throws SQLException {
		// borrar la medida de tiempo
		TiempoDAO tiempoDAO = PersistenceFactory.createTiempoDAO();
		tiempoDAO.borrarIntervalo(this.tiempo);

		// Probar a recuperarla y comparar
		Time leida = tiempoDAO.buscarIntervaloTiempo(this.tiempo.getIdTime());

		org.junit.Assert.assertNull(leida);
	}
}
