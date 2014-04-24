package tests.PersistenceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import model.Area;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;
import model.User;

import org.junit.Before;
import org.junit.Test;

import persistencia.ObservacionesDAO;
import persistencia.PersistenceFactory;

public class ObservacionesTest {

	private Observation observacion;
	private Area area;
	private Provider proveedor;
	private Measure medida;
	private Time intervalo;
	private Submission entrada;

	@Before
	public void before() throws SQLException {
		this.observacion = new Observation();

		//Creacion de la entrada
		this.entrada = new Submission();
		this.entrada.setDate(new Date());
		User usuario = new User();
		usuario.setIdUser(1L);
		this.entrada.setUser(usuario);
		this.observacion.setSubmission(this.entrada);
		
		// Creación del area
		this.area = new Area();
		this.area.setName("AreaPrueba000001");
		this.observacion.setArea(this.area);

		// Creacion del proveedor
		this.proveedor = new Provider();
		this.proveedor.setNombre("ProveedorPrueba00001");
		this.proveedor.setTipoOrganizacion("Estadística de prueba");
		this.observacion.setProvider( this.proveedor);

		// Creación de la medida
		this.medida = new Measure();
		this.medida.setValue("Valor de prueba");
		this.medida.setUnit("Medida de prueba");
		this.observacion.setMeasure(this.medida);

		// Creacion del intervalo
		this.intervalo = new Time();
		this.intervalo.setStartDate(new Date());
		this.observacion.setTime(this.intervalo);
		
		//Creacion del indicador
		Indicator indicador = new Indicator();
		indicador.setNombre("Nombre de prueba");
		this.observacion.setIndicator(indicador);
	}

	@Test
	public void test() throws SQLException {
		testCrearObservacionYRecuperar();
		testVolverAInsertarObservacion();
		testLeerObservacionesDelArea();
		testLeerObservacionesDeUnIndicador();
		testEliminarObservacionYRecuperar();
		testEliminarCambios();
	}

	private void testVolverAInsertarObservacion() throws SQLException 
	{
		//Copia el id de la observacion y los demas dtos
		Long idObv = this.observacion.getIdObservation();
		Long idArea = this.observacion.getArea().getIdArea();
		Long idIndicador = this.observacion.getIndicator().getIdIndicator();
		Long idMedida = this.observacion.getMeasure().getIdMeasure();
		Long idProvider = this.observacion.getProvider().getIdOrganization();
		Long idTiempo = this.observacion.getTime().getIdTime();
		
		//Borrar los ids
		this.observacion.setIdObservation(null);
		this.observacion.getArea().setIdArea(null);
		this.observacion.getIndicator().setIdIndicator(null);
		this.observacion.getMeasure().setIdMeasure(null);
		this.observacion.getProvider().setIdOrganization(null);
		this.observacion.getTime().setIdTime(null);
		
		// Insertar la observacion
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		this.observacion = obvDAO.insertarObservacion(this.observacion);
		
		//Comprobar que se les ha asignado el mismo id
		assertEquals(idObv, this.observacion.getIdObservation());
		assertEquals(idArea, this.observacion.getArea().getIdArea());
		assertEquals(idIndicador, this.observacion.getIndicator().getIdIndicator());
		assertEquals(idProvider, this.observacion.getProvider().getIdOrganization());
		assertEquals(idMedida, this.observacion.getMeasure().getIdMeasure());
		assertEquals(idTiempo,this.observacion.getTime().getIdTime());
		
		
	}

	private void testLeerObservacionesDeUnIndicador() throws SQLException 
	{
		// Recuperar la observacion
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		List<Observation> leidas = obvDAO.leerObservacionesDeUnIndicador("Nombre de prueba");

		assertEquals(leidas.size(), 1);
		assertEquals(leidas.get(0).getArea(), this.observacion.getArea());
		assertEquals(leidas.get(0).getIndicator(), this.observacion.getIndicator());
		assertEquals(leidas.get(0).getProvider(), this.observacion.getProvider());
		assertEquals(leidas.get(0).getMeasure(), this.observacion.getMeasure());
		assertEquals(leidas.get(0).getTime(),this.observacion.getTime());
		assertEquals(leidas.get(0).getSubmission(), this.observacion.getSubmission());
	}

	//Busca la observacion introducida buscando por su area
	private void testLeerObservacionesDelArea() throws SQLException 
	{
		// Recuperar la observacion
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		List<Observation> leidas = obvDAO.leerObservacionesDeUnArea(this.observacion.getArea());

		assertEquals(leidas.size(), 1);
		assertEquals(leidas.get(0).getArea().getIdArea(), this.observacion.getArea().getIdArea());
		assertEquals(leidas.get(0).getArea(), this.observacion.getArea());
		assertEquals(leidas.get(0).getIndicator(), this.observacion.getIndicator());
		assertEquals(leidas.get(0).getProvider(), this.observacion.getProvider());
		assertEquals(leidas.get(0).getMeasure(), this.observacion.getMeasure());
		assertEquals(leidas.get(0).getTime(),this.observacion.getTime());
		assertEquals(leidas.get(0).getSubmission(), this.observacion.getSubmission());
	}

	private void testCrearObservacionYRecuperar() throws SQLException {
		// Insertar la observacion
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		this.observacion = obvDAO.insertarObservacion(this.observacion);

		// Recuperar la observacion
		Observation leida = obvDAO.buscarObservacionPorIdentificador(this.observacion
						.getIdObservation());

		assertNotNull(leida);
		assertEquals(leida.getArea(), this.observacion.getArea());
		assertEquals(leida.getIndicator(), this.observacion.getIndicator());
		assertEquals(leida.getProvider(), this.observacion.getProvider());
		assertEquals(leida.getMeasure(), this.observacion.getMeasure());
		assertEquals(leida.getTime(),this.observacion.getTime());
		assertEquals(leida.getSubmission(), this.observacion.getSubmission());
		
		//recuperar todas las observaciones
		List<Observation> todas = obvDAO.listarTodasObservaciones();
		org.junit.Assert.assertTrue(todas.contains(this.observacion));
		
	}

	private void testEliminarObservacionYRecuperar() throws SQLException {
		//Eliminar la observacion
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		obvDAO.eliminarObservacion(this.observacion);

		// Recuperar la observacion
		Observation leida = obvDAO.buscarObservacionPorIdentificador(this.observacion
						.getIdObservation());

		assertNull(leida);
	}

	private void testEliminarCambios() throws SQLException {
		ObservacionesDAO obvDAO = PersistenceFactory.createObservacionesDAO();
		obvDAO.eliminarObservacion(this.observacion);
	}

}
