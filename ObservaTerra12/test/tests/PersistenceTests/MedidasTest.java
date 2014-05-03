package tests.PersistenceTests;
import java.sql.SQLException;

import model.Measure;

import org.junit.Before;
import org.junit.Test;

import persistencia.MedidasDAO;
import persistencia.PersistenceFactory;

public class MedidasTest {

	private Measure medida;

	@Before
	public void before() {
		this.medida = new Measure();
		this.medida.setUnit("kilometros/h");
		this.medida.setValue("125");
	}

	@Test
	public void test() throws SQLException {
		testCrearYRecuperar();
		testBorrarYRecuperar();
	}

	private void testCrearYRecuperar() throws SQLException {
		// Crear la medida
		MedidasDAO medidasDAO = PersistenceFactory.createMedidasDAO();
		this.medida = medidasDAO.crearMedida(this.medida);

		// Probar a recuperarla y comparar
		Measure leida = medidasDAO.buscarMedidaPorIdentificador(this.medida.getIdMeasure());
		org.junit.Assert.assertNotNull(leida);

		org.junit.Assert.assertEquals(leida, this.medida);
	}

	private void testBorrarYRecuperar() throws SQLException {
		//Borrar la medida
		MedidasDAO medidasDAO = PersistenceFactory.createMedidasDAO();
		medidasDAO.borrarMedida(this.medida);

		// Probar a recuperarla y comparar
		Measure leida = medidasDAO.buscarMedidaPorIdentificador(this.medida.getIdMeasure());
		org.junit.Assert.assertNull(leida);
	}

}
