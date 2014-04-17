import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.Area;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Time;

import org.junit.Before;
import org.junit.Test;

import persistencia.implJdbc.AreasJdbc;
import persistencia.implJdbc.MedidasJdbc;
import persistencia.implJdbc.ObservacionesJdbc;
import persistencia.implJdbc.OrganizacionesJdbc;
import persistencia.implJdbc.TiempoJdbc;
import utils.DBConnection;

public class ObservacionesTest {

	private Observation observacion;
	private Area area;
	private Provider proveedor;
	private Measure medida;
	private Time intervalo;

	@Before
	public void before() throws SQLException {
		this.observacion = new Observation();

		Connection con = DBConnection.getConnection();

		// Creación del area
		this.area = new Area();
		this.area.setName("AreaPrueba000001");
		AreasJdbc areasJDBC = new AreasJdbc();
		areasJDBC.setConnection(con);
		areasJDBC.crearAreaySubAreas(this.area);
		this.observacion.setArea(this.area);

		// Creacion del proveedor
		this.proveedor = new Provider();
		this.proveedor.setNombre("ProveedorPrueba00001");
		this.proveedor.setTipoOrganizacion("Estadística de prueba");
		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		this.proveedor = (Provider) orgJDBC.crearOrganizacion( (Organization)this.proveedor);
		this.observacion.setProvider( this.proveedor);

		// Creación de la medida
		this.medida = new Measure();
		this.medida.setValue("Valor de prueba");
		this.medida.setUnit("Medida de prueba");
		MedidasJdbc medidasJDBC = new MedidasJdbc();
		medidasJDBC.setConnection(con);
		this.medida = medidasJDBC.crearMedida(this.medida);
		this.observacion.setMeasure(this.medida);

		// Creacion del intervalo
		this.intervalo = new Time();
		this.intervalo.setStartDate(new Date());
		TiempoJdbc intJDBC = new TiempoJdbc();
		intJDBC.setConnection(con);
		this.intervalo = intJDBC.crearIntervalo(this.intervalo);
		this.observacion.setTime(this.intervalo);
		
		//Creacion del indicador
		Indicator indicador = new Indicator();
		indicador.setNombre("Nombre de prueba");
		this.observacion.setIndicator(indicador);
		con.commit();
		con.close();
	}

	@Test
	public void test() throws SQLException {
		testCrearObservacionYRecuperar();
		testLeerObservacionesDelArea();
		testLeerObservacionesDeUnIndicador();
		testEliminarObservacionYRecuperar();
		testEliminarCambios();
	}

	private void testLeerObservacionesDeUnIndicador() throws SQLException 
	{
		// Recuperar la observacion
		Connection con = DBConnection.getConnection();
		ObservacionesJdbc obvJDBC = new ObservacionesJdbc();
		obvJDBC.setConnection(con);
		List<Observation> leidas = obvJDBC.leerObservacionesDeUnIndicador("Nombre de prueba");

		assertEquals(leidas.size(), 1);
		assertEquals(leidas.get(0).getArea().getId_area(), this.area.getId_area());
		assertEquals(leidas.get(0).getIndicator().getNombre(), this.observacion
				.getIndicator().getNombre());
		assertEquals(leidas.get(0).getProvider().getIdOrganizacion(),
				this.proveedor.getIdOrganizacion());
		assertEquals(leidas.get(0).getMeasure().getIdMedida(),
				this.medida.getIdMedida());
		assertEquals(leidas.get(0).getTime().getIdTiempo(),
				this.intervalo.getIdTiempo());
		assertEquals(leidas.get(0).getIndicator().getNombre(), "Nombre de prueba");

		con.close();		
	}

	//Busca la observacion introducida buscando por su area
	private void testLeerObservacionesDelArea() throws SQLException 
	{
		// Recuperar la observacion
		Connection con = DBConnection.getConnection();
		ObservacionesJdbc obvJDBC = new ObservacionesJdbc();
		obvJDBC.setConnection(con);
		List<Observation> leidas = obvJDBC.leerObservacionesDeUnArea(this.area);

		assertEquals(leidas.size(), 1);
		assertEquals(leidas.get(0).getArea().getId_area(), this.area.getId_area());
		assertEquals(leidas.get(0).getIndicator().getNombre(), this.observacion
				.getIndicator().getNombre());
		assertEquals(leidas.get(0).getProvider().getIdOrganizacion(),
				this.proveedor.getIdOrganizacion());
		assertEquals(leidas.get(0).getMeasure().getIdMedida(),
				this.medida.getIdMedida());
		assertEquals(leidas.get(0).getTime().getIdTiempo(),
				this.intervalo.getIdTiempo());
		assertEquals(leidas.get(0).getIndicator().getNombre(), "Nombre de prueba");

		con.close();		
	}

	private void testCrearObservacionYRecuperar() throws SQLException {
		// Insertar la observacion
		Connection con = DBConnection.getConnection();
		ObservacionesJdbc obvJDBC = new ObservacionesJdbc();
		obvJDBC.setConnection(con);
		this.observacion = obvJDBC.insertarObservacion(this.observacion);

		// Recuperar la observacion
		Observation leida = obvJDBC
				.buscarObservacionPorIdentificador(this.observacion
						.getIdObservacion());

		assertNotNull(leida);
		assertEquals(leida.getArea().getId_area(), this.area.getId_area());
		assertEquals(leida.getIndicator().getNombre(), this.observacion
				.getIndicator().getNombre());
		assertEquals(leida.getProvider().getIdOrganizacion(),
				this.proveedor.getIdOrganizacion());
		assertEquals(leida.getMeasure().getIdMedida(),
				this.medida.getIdMedida());
		assertEquals(leida.getTime().getIdTiempo(),
				this.intervalo.getIdTiempo());
		assertEquals(leida.getIndicator().getNombre(), "Nombre de prueba");

		con.close();
	}

	private void testEliminarObservacionYRecuperar() throws SQLException {
		//Eliminar la observacion
		Connection con = DBConnection.getConnection();
		ObservacionesJdbc obvJDBC = new ObservacionesJdbc();
		obvJDBC.setConnection(con);
		obvJDBC.eliminarObservacion(this.observacion);

		// Recuperar la observacion
		Observation leida = obvJDBC
				.buscarObservacionPorIdentificador(this.observacion
						.getIdObservacion());

		assertNull(leida);

		con.close();
	}

	private void testEliminarCambios() throws SQLException {
		Connection con = DBConnection.getConnection();

		// Borrado del area
		AreasJdbc areasJDBC = new AreasJdbc();
		areasJDBC.setConnection(con);
		areasJDBC.eliminarArea(this.area);

		// Borrado del proveedor
		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		orgJDBC.borrarOrganizacion(this.proveedor);

		// Borrado de la medida
		MedidasJdbc medidasJDBC = new MedidasJdbc();
		medidasJDBC.setConnection(con);
		medidasJDBC.borrarMedida(this.medida);

		// Borrado del intervalo
		TiempoJdbc intJDBC = new TiempoJdbc();
		intJDBC.setConnection(con);
		intJDBC.borrarIntervalo(this.intervalo);

		con.close();
	}

}
