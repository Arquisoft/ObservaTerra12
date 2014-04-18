import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import model.Time;

import org.junit.Before;
import org.junit.Test;

import persistencia.implJdbc.TiempoJdbc;
import utils.DBConnection;

public class TiempoTest {

	private Time tiempo;

	@Before
	public void before() {
		this.tiempo = new Time();

		Date datebegining = new Date(System.currentTimeMillis() / 3);
		this.tiempo.setStartDate(datebegining);

		Date dateEnd = new Date(System.currentTimeMillis() / 4);
		this.tiempo.setEndDate(dateEnd);
	}

	@Test
	public void test() throws SQLException {
		testCrearYRecuperar();
		testBorrarYRecuperar();
	}

	private void testCrearYRecuperar() throws SQLException {
		Connection con = DBConnection.getConnection();

		// Crear la medida de tiempo
		TiempoJdbc tiempoJDBC = new TiempoJdbc();
		tiempoJDBC.setConnection(con);
		this.tiempo = tiempoJDBC.crearIntervalo(this.tiempo);

		// Probar a recuperarla y comparar
		Time leida = tiempoJDBC
				.buscarIntervaloTiempo(this.tiempo.getIdTime());

		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida.getStartDate(),
				this.tiempo.getStartDate());
		org.junit.Assert.assertEquals(leida.getEndDate(),
				this.tiempo.getEndDate());

		con.close();
	}

	private void testBorrarYRecuperar() throws SQLException {
		Connection con = DBConnection.getConnection();

		// borrar la medida de tiempo
		TiempoJdbc tiempoJDBC = new TiempoJdbc();
		tiempoJDBC.setConnection(con);
		tiempoJDBC.borrarIntervalo(this.tiempo);

		// Probar a recuperarla y comparar
		Time leida = tiempoJDBC
				.buscarIntervaloTiempo(this.tiempo.getIdTime());

		org.junit.Assert.assertNull(leida);

		con.close();
	}
}
