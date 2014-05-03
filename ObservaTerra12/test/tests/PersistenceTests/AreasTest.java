package tests.PersistenceTests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;
import model.Country;

import org.junit.Before;
import org.junit.Test;

import persistencia.AreasDAO;
import persistencia.PersistenceFactory;

public class AreasTest {

	private Area area;
	private Area areaAnulada;

	@Before
	public void prepare() {
		this.area = new Area();
		area.setName("Area principal");

		List<Area> subareas = new ArrayList<Area>();
		Area area1 = new Area();
		area1.setName("Subarea 1");
		subareas.add(area1);

		Area area2 = new Area();
		area2.setName("Subarea 2");
		subareas.add(area2);

		area.setAreas(subareas);
	}

	@Test
	public void test() throws SQLException {
		testAñadirYBorrar();
		testActualizarArea();
		testAnularAsociacion();
		testAsociacion();
		testBuscarAreaOPais();
		testEliminar();
	}

	/**
	 * Prueba a añadir y a recuperar un área
	 * 
	 * @throws SQLException
	 */
	private void testAñadirYBorrar() throws SQLException {
		// Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.crearArea(this.area);

		// Comprueba que me devuelve el id al crear
		org.junit.Assert.assertNotNull(this.area.getIdArea());

		// Recuperarla por nombre
		Area leida = areasDAO.leerArea("Area principal");

		// Comprobar que son iguales
		org.junit.Assert.assertEquals(leida, this.area);

		// Buscar por id
		leida = areasDAO.buscarArea(this.area.getIdArea());

		// Comprobar que son iguales
		org.junit.Assert.assertEquals(leida, this.area);

		// Eliminar subareas y volverlas a leer
		leida.setAreas(null);
		leida = areasDAO.leerSubAreas(this.area);

		// Comprobar que son iguales
		org.junit.Assert.assertEquals(leida, this.area);
	}

	// Actualizar area
	private void testActualizarArea() throws SQLException {
		this.area.setName("nombreArea00001");

		// Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.actualizarArea(this.area);

		// Recuperarla por nombre
		Area leida = areasDAO.leerArea("nombreArea00001");

		// Comprobar que son iguales
		org.junit.Assert.assertEquals(leida, this.area);
		
		//Probar a recuperar el listado
		List<Area> listado = areasDAO.listadoAreas();
		org.junit.Assert.assertFalse(listado.isEmpty());
		org.junit.Assert.assertTrue(listado.contains(this.area));	
		
		//Probar a recuperar el listado de áreas y paises
		listado = areasDAO.listadoAreasYPaises();;
		org.junit.Assert.assertFalse(listado.isEmpty());
		org.junit.Assert.assertTrue(listado.contains(this.area));
	}

	// Anular la asociacion y comprobar
	private void testAnularAsociacion() throws SQLException {
		// Borrar la subarea 1
		this.areaAnulada = this.area.getAreas().get(0);

		// Eliminar la subarea del modelo
		this.area.removeArea(this.areaAnulada);

		// Elimina de la base de datos el area (debería de eliminar la
		// asociación)
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.eliminarArea(this.areaAnulada);

		// Comprobar subareas
		this.area.setAreas(null);
		Area leida = areasDAO.leerSubAreas(this.area);

		org.junit.Assert.assertEquals(leida, this.area);
	}

	// Asociar y comprobar
	private void testAsociacion() throws SQLException {
		// añadir el area al objeto del model de dominio
		this.area.addArea(this.areaAnulada);

		// Añadir el área eliminada al sistema al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.crearArea(this.areaAnulada);
		areasDAO.asociarSubarea(this.area, this.areaAnulada);

		// Comprobar subareas
		Area leida = areasDAO.leerSubAreas(this.area);

		org.junit.Assert.assertEquals(leida, this.area);
	}
	
	//Buscar área o país
	private void testBuscarAreaOPais() throws SQLException
	{
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		
		//Buscar por nombre y comprobar que son iguales
		Area leida = areasDAO.buscarAreaYPaisPorNombre(this.area.getName());	
		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida, this.area);
		
		//Buscar por id y comprobar que son iguales
		leida = areasDAO.buscarAreaYPaisPorId(this.area.getIdArea());
		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida, this.area);
	}

	// Eliminar y recuperar
	private void testEliminar() throws SQLException {
		// Eliminar un area
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.eliminarArea(this.area);

		// Recuperarla por nombre
		Area leida = areasDAO.leerArea("nombreArea00001");

		org.junit.Assert.assertNull(leida);
	}

	@Test
	public void testEsPais() throws SQLException {
		// Añadir un pais
		Country pais = new Country();
		pais.setName("pruebaTest000001");
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.crearPais(pais);

		// Recuperarlo por nombre
		Country leida = areasDAO.leerPais("pruebaTest000001");
		org.junit.Assert.assertEquals(leida, pais);

		// Recuperarlo por id
		leida = areasDAO.leerPais(pais.getIdArea());
		org.junit.Assert.assertEquals(leida, pais);
		
		//Buscar por nombre y comprobar que son iguales
		leida = (Country) areasDAO.buscarAreaYPaisPorNombre(pais.getName());	
		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida, pais);
		
		//Buscar por id y comprobar que son iguales
		leida = (Country) areasDAO.buscarAreaYPaisPorId(pais.getIdArea());
		org.junit.Assert.assertNotNull(leida);
		org.junit.Assert.assertEquals(leida, pais);
		
		//Probar a recuperar el listado
		List<Country> listado = areasDAO.listadoPaises();
		org.junit.Assert.assertFalse(listado.isEmpty());
		org.junit.Assert.assertTrue(listado.contains(pais));	
		
		//Probar a recuperar el listado de áreas y paises
		List<Area> lista = areasDAO.listadoAreasYPaises();;
		org.junit.Assert.assertFalse(lista.isEmpty());
		org.junit.Assert.assertTrue(lista.contains(pais));

		// Eliminarlo
		areasDAO.eliminarArea(pais);

		// Comprobar que se elimino
		leida = areasDAO.leerPais("pruebaTest000001");
		org.junit.Assert.assertNull(leida);
	}

}
