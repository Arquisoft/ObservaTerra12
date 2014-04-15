import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.SQLException;
import java.util.List;
import model.Organization;
import org.junit.Before;
import org.junit.Test;
import persistencia.JdbcDAOs.OrganizacionesJdbc;
import utils.DBConnection;

public class OrganizacionesTest {

	private Organization organizacion;

	@Before
	public void before() {
		this.organizacion = new Organization();
		this.organizacion.setNombre("ONG-Prueba");
		this.organizacion.setTipoOrganizacion("RELIGIOSA");
	}

	@Test
	public void test() throws SQLException {
		testCrearOrganizacion();
		testActualizarOrganizacion();
		testSuprimirOrganizacion();
	}

	/**
	 * Prueba que se cree una organización correctamente
	 * 
	 * @throws SQLException
	 */
	public void testCrearOrganizacion() throws SQLException {
		// Guarda a la nueva organizacion
		OrganizacionesJdbc organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		this.organizacion = organizacionesJDBC
				.crearOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		Organization orgLeida = organizacionesJDBC
				.leerOrganizacion(this.organizacion.getIdOrganizacion());

		assertNotNull(orgLeida);
		assertEquals(this.organizacion.getNombre(), orgLeida.getNombre());
		assertEquals(this.organizacion.getTipoOrganizacion(),
				orgLeida.getTipoOrganizacion());

		// Probar a listar todos
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		List<Organization> orgns = organizacionesJDBC.listarOrganizaciones();

		assertTrue(orgns.contains(this.organizacion));
	}

	/**
	 * Prueba que se actualice una organización correctamente
	 * 
	 * @throws SQLException
	 */
	public void testActualizarOrganizacion() throws SQLException {
		// Actualiza los datos de la organizacion
		this.organizacion.setNombre("org-0001");
		this.organizacion.setTipoOrganizacion("No religiosa");

		// Guarda los cambios
		OrganizacionesJdbc organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		organizacionesJDBC.actualizarOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		Organization orgLeida = organizacionesJDBC
				.leerOrganizacion(this.organizacion.getIdOrganizacion());

		assertNotNull(orgLeida);
		assertEquals("org-0001", orgLeida.getNombre());
		assertEquals("No religiosa", orgLeida.getTipoOrganizacion());

		// Probar a listar todos
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		List<Organization> orgns = organizacionesJDBC.listarOrganizaciones();

		assertTrue(orgns.contains(this.organizacion));
	}

	public void testSuprimirOrganizacion() throws SQLException {
		// Borra la organización
		OrganizacionesJdbc organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		organizacionesJDBC.borrarOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		Organization orgLeida = organizacionesJDBC
				.leerOrganizacion(this.organizacion.getIdOrganizacion());

		assertNull(orgLeida);

		// Probar a listar todos
		organizacionesJDBC = new OrganizacionesJdbc();
		organizacionesJDBC.setConnection(DBConnection.getConnection());
		List<Organization> orgns = organizacionesJDBC.listarOrganizaciones();

		assertFalse(orgns.contains(this.organizacion));
	}

}
