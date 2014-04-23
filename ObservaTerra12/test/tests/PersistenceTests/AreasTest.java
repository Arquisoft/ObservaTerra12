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
		testEliminar();
	}

	/**
	 * Prueba a añadir y a recuperar un área
	 * @throws SQLException 
	 */
	private void testAñadirYBorrar() throws SQLException 
	{
		//Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.crearArea(this.area);
		
		//Recuperarla por nombre
		Area leida = areasDAO.leerArea("Area principal");
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
				
		//Recuperarla por id
		org.junit.Assert.assertNotNull(this.area.getIdArea());
		leida = areasDAO.buscarArea(this.area.getIdArea());
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
		
		//Leer subareas
		leida = areasDAO.leerSubAreas(this.area);
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
		
		for(int i=0; i<leida.getAreas().size(); i++)
		{
			org.junit.Assert.assertEquals(leida.getAreas().get(i), this.area.getAreas().get(i));
		}	
		
	}
	
	//Actualizar area
	private void testActualizarArea() throws SQLException {
		this.area.setName("nombreArea00001");		
		
		//Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.actualizarArea(this.area);
		
		//Recuperarla por nombre
		Area leida = areasDAO.leerArea("nombreArea00001");
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());	
	}

	
	//Anular la asociacion y comprobar
	private void testAnularAsociacion() throws SQLException
	{
		this.areaAnulada = this.area.getAreas().get(0);
		this.area.removeArea(this.areaAnulada);
		
		//Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.anularAsociacionSubarea(this.area, this.areaAnulada);
		
		//Comprobar subareas
		Area leida = areasDAO.leerSubAreas(this.area);
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
		
		for(int i=0; i<leida.getAreas().size(); i++)
		{
			org.junit.Assert.assertEquals(leida.getAreas().get(i), this.area.getAreas().get(i));
		}
	}
	
	
	//Asociar y comprobar
	private void testAsociacion() throws SQLException
	{
		this.area.addArea(this.areaAnulada);
		
		//Añadir un área al sistema
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.asociarSubarea(this.area, this.areaAnulada);
		
		//Comprobar subareas
		Area leida = areasDAO.leerSubAreas(this.area);
		
		org.junit.Assert.assertTrue(leida.getIdArea() == this.area.getIdArea());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
		
		for(int i=0; i<leida.getAreas().size(); i++)
		{
			org.junit.Assert.assertEquals(leida.getAreas().get(i), this.area.getAreas().get(i));
		}
	}
		
	//Eliminar y recuperar
	private void testEliminar() throws SQLException
	{
		//Eliminar un area
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.eliminarArea(this.area);
		
		//Recuperarla por nombre
		Area leida = areasDAO.leerArea("nombreArea00001");
		
		org.junit.Assert.assertNull(leida);
	}
	
	@Test
	public void testEsPais() throws SQLException
	{
		//Añadir un pais
		Country pais = new Country();
		pais.setName("pruebaTest000001");
		AreasDAO areasDAO = PersistenceFactory.createAreasDAO();
		areasDAO.crearPais(pais);		
		
		//Recuperarlo por nombre
		Country leida = areasDAO.leerPais("pruebaTest000001");
		org.junit.Assert.assertEquals(leida, pais);		
		
		//Recuperarlo por id
		leida = areasDAO.leerPais(pais.getIdArea());
		org.junit.Assert.assertEquals(leida, pais);		
		
		//Eliminarlo
		areasDAO.eliminarArea(pais);
		
		//Comprobar que se elimino
		leida = areasDAO.leerPais("pruebaTest000001");
		org.junit.Assert.assertNull(leida);	
	}
	

}

