package tests.PersistenceTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import model.Country;
import model.Organization;
import model.Provider;

import org.junit.Before;
import org.junit.Test;

import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;

public class OrganizacionesTest {

	private Organization organizacion;

	@Before
	public void before() {
		this.organizacion = new Organization();
		this.organizacion.setNombre("ONG-Prueba");
		this.organizacion.setTipoOrganizacion("RELIGIOSA");
		Country count = new Country();
		count.setIdArea(1L);
		this.organizacion.setCountry(count);
	}

	@Test
	public void testOrganizaciones() throws SQLException {
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
		OrganizacionesDAO orgDAO = PersistenceFactory.createOrganizacionesDAO();
		this.organizacion = orgDAO.crearOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		Organization orgLeida = orgDAO.leerOrganizacion(this.organizacion.getIdOrganization());
		assertNotNull(orgLeida);
		assertEquals(this.organizacion, orgLeida);
		
		//Probar a recuperla por nombre
		orgLeida = orgDAO.buscarOrganizacionPorNombre("ONG-Prueba");
		assertNotNull(orgLeida);
		assertEquals(this.organizacion, orgLeida);
		
		//Probar a recuperar una organización inexistente
		orgLeida = orgDAO.buscarOrganizacionPorNombre("etwertwertwertwertw56twregfsdf");
		assertNull(orgLeida);
		
		//Probar a recuperar una organización inexistente
		orgLeida = orgDAO.buscarOrganizacionOProveedorPorNombre("sdfasdfajsdfioapjdsifoajdsf");
		assertNull(orgLeida);
		
		//Probar a recuperar otra vez
		orgLeida = orgDAO.buscarOrganizacionOProveedorPorNombre("ONG-Prueba");
		assertNotNull(orgLeida);
		assertEquals(this.organizacion, orgLeida);

		// Probar a listar todos
		List<Organization> orgns = orgDAO.listarOrganizaciones();

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
		OrganizacionesDAO orgDAO = PersistenceFactory.createOrganizacionesDAO();
		orgDAO.actualizarOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		Organization orgLeida = orgDAO.leerOrganizacion(this.organizacion
				.getIdOrganization());

		assertNotNull(orgLeida);
		assertEquals(this.organizacion, orgLeida);
		
		//Probar a recuperarla 
		orgLeida = orgDAO.buscarOrganizacionOProveedorPorNombre("org-0001");
		assertEquals(this.organizacion, orgLeida);

		// Probar a listar todos
		List<Organization> orgns = orgDAO.listarOrganizaciones();

		assertTrue(orgns.contains(this.organizacion));
	}

	public void testSuprimirOrganizacion() throws SQLException {
		// Borra la organización
		OrganizacionesDAO orgDAO = PersistenceFactory.createOrganizacionesDAO();
		orgDAO.borrarOrganizacion(this.organizacion);

		// Probar a recuperarlo individualmente
		Organization orgLeida = orgDAO.leerOrganizacion(this.organizacion.getIdOrganization());

		assertNull(orgLeida);

		// Probar a listar todos
		List<Organization> orgns = orgDAO.listarOrganizaciones();

		assertFalse(orgns.contains(this.organizacion));
	}

	@Test
	public void testProveedores() throws SQLException {
		Provider proveedor = new Provider();
		proveedor.setNombre("pruebaProveedores");
		proveedor.setTipoOrganizacion("No gubernamental");

		// Crear proveedor
		OrganizacionesDAO orgDAO = PersistenceFactory.createOrganizacionesDAO();
		proveedor = orgDAO.crearProveedor(proveedor);

		// Leer el proveedor
		Provider leido = orgDAO.leerProvedor(proveedor.getIdOrganization());
		assertTrue(leido instanceof Provider);
		assertNotNull(leido);
		assertEquals(leido, proveedor);

		//Probar a recuperarlo
		leido = (Provider) orgDAO.buscarOrganizacionOProveedorPorNombre("pruebaProveedores");
		assertEquals(leido, proveedor);
				
		// Leer como area
		Organization or = orgDAO.leerOrganizacion(proveedor.getIdOrganization());
		assertNull(or);

		// Probar a recuperar por nombre
		leido = orgDAO.leerProvedor("pruebaProveedores");
		assertNotNull(leido);
		assertEquals(leido, proveedor);

		// Listar proveedor
		List<Provider> proveedores = orgDAO.listarProveedores();
		assertTrue(proveedores.size() > 0);
		assertTrue(proveedores.contains(proveedor));

		// Eliminar el proveedor
		orgDAO.borrarOrganizacion(proveedor);

		// Buscarlo a ver
		leido = orgDAO.leerProvedor(proveedor.getIdOrganization());
		assertNull(leido);
	}
}
