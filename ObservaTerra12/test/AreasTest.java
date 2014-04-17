import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;

import org.junit.Before;
import org.junit.Test;

import persistencia.implJdbc.AreasJdbc;
import utils.DBConnection;

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
		AreasJdbc areasJDBC = new AreasJdbc();
		Connection con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		areasJDBC.crearAreaySubAreas(this.area);
		con.close();
		
		//Recuperarla por nombre
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		Area leida = areasJDBC.leerArea("Area principal");
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
				
		//Recuperarla por id
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		leida = areasJDBC.buscarArea(this.area.getId_area());
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());
		
		//Leer subareas
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		leida = areasJDBC.leerSubAreas(this.area);
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
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
		AreasJdbc areasJDBC = new AreasJdbc();
		Connection con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		areasJDBC.actualizarArea(this.area);
		con.close();
		
		//Recuperarla por nombre
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		Area leida = areasJDBC.leerArea("nombreArea00001");
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
		org.junit.Assert.assertEquals(leida.getName(), this.area.getName());	
	}

	
	//Anular la asociacion y comprobar
	private void testAnularAsociacion() throws SQLException
	{
		this.areaAnulada = this.area.getAreas().get(0);
		this.area.removeArea(this.areaAnulada);
		
		//Añadir un área al sistema
		AreasJdbc areasJDBC = new AreasJdbc();
		Connection con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		areasJDBC.anularAsociacionSubarea(this.area, this.areaAnulada);
		con.close();
		
		//Comprobar subareas
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		Area leida = areasJDBC.leerSubAreas(this.area);
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
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
		AreasJdbc areasJDBC = new AreasJdbc();
		Connection con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		areasJDBC.asociarSubarea(this.area, this.areaAnulada);
		con.close();
		
		//Comprobar subareas
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		Area leida = areasJDBC.leerSubAreas(this.area);
		con.close();
		
		org.junit.Assert.assertTrue(leida.getId_area() == this.area.getId_area());
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
		AreasJdbc areasJDBC = new AreasJdbc();
		Connection con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		areasJDBC.eliminarArea(this.area);
		con.close();
		
		//Recuperarla por nombre
		con = DBConnection.getConnection();
		areasJDBC.setConnection(con);
		Area leida = areasJDBC.leerArea("nombreArea00001");
		con.close();
		
		org.junit.Assert.assertNull(leida);
	}

}

