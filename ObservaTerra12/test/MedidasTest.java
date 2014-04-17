import java.sql.Connection;
import java.sql.SQLException;

import model.Measure;

import org.junit.Before;
import org.junit.Test;

import persistencia.implJdbc.MedidasJdbc;
import utils.DBConnection;

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
		Connection con = DBConnection.getConnection();

		// Crear la medida
		MedidasJdbc medidasJDBC = new MedidasJdbc();
		medidasJDBC.setConnection(con);
		this.medida = medidasJDBC.crearMedida(this.medida);

		// Probar a recuperarla y comparar
		Measure leida = medidasJDBC.buscarMedidaPorIdentificador(this.medida
				.getIdMedida());
		org.junit.Assert.assertNotNull(leida);

		org.junit.Assert.assertEquals(leida.getUnit(), this.medida.getUnit());
		org.junit.Assert.assertEquals(leida.getValue(), this.medida.getValue());

		con.close();
	}

	private void testBorrarYRecuperar() throws SQLException {
		Connection con = DBConnection.getConnection();

		//Borrar la medida
		MedidasJdbc medidasJDBC = new MedidasJdbc();
		medidasJDBC.setConnection(con);
		medidasJDBC.borrarMedida(this.medida);

		// Probar a recuperarla y comparar
		Measure leida = medidasJDBC.buscarMedidaPorIdentificador(this.medida
				.getIdMedida());
		org.junit.Assert.assertNull(leida);

		con.close();
	}

}
