package tests.PersistenceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.List;

import model.Indicator;

import org.junit.Before;
import org.junit.Test;

import persistencia.IndicadoresDAO;
import persistencia.PersistenceFactory;

public class IndicadoresTest 
{
	
	private IndicadoresDAO indicadorDAO;
	private Indicator indicador1;
	
	@Before
	public void prepare()
	{
		this.indicadorDAO = PersistenceFactory.createIndicadoresDAO();
		
		this.indicador1 = new Indicator();
		this.indicador1.setNombre("IndicadorPrueba");
	}

	@Test
	public void test() throws SQLException {
		testAñadirYLeer();
		testListados();
		testActualizarYLeer();
		testBorrarYLeer();
	}

	private void testAñadirYLeer() throws SQLException 
	{
		//Añadir el indicador
		this.indicador1 = this.indicadorDAO.añadirIndicador(this.indicador1);
		
		//Recuperarlo por nombre
		Indicator leida = this.indicadorDAO.leerIndicador(this.indicador1.getIdIndicator());
	    assertNotNull(leida);
	    assertEquals(leida, this.indicador1);
		
		//Recuperarlo por id
	    leida = this.indicadorDAO.leerIndicador("IndicadorPrueba");
	    assertNotNull(leida);
	    assertEquals(leida, this.indicador1);
	    
	    //Recuperar uno que no exista
	    leida = this.indicadorDAO.leerIndicador("IndicaXPrueba");
	    assertNull(leida);		
	}

	private void testListados() throws SQLException 
	{
		List<Indicator> indicadores = this.indicadorDAO.listarTodosLosIndicadores();
		org.junit.Assert.assertTrue(indicadores.size() > 0);
		org.junit.Assert.assertTrue(indicadores.contains(this.indicador1));		
	}
	
	
	private void testActualizarYLeer() throws SQLException {
		//Actualizar el indicador
		this.indicador1.setNombre("IndicadorActualizado");
		this.indicadorDAO.actualizarIndicador(this.indicador1);
		
		//Recuperarlo por nombre
		Indicator leida = this.indicadorDAO.leerIndicador(this.indicador1.getIdIndicator());
	    assertNotNull(leida);
	    assertEquals(leida, this.indicador1);
		
		//Recuperarlo por id
	    leida = this.indicadorDAO.leerIndicador("IndicadorActualizado");
	    assertNotNull(leida);
	    assertEquals(leida, this.indicador1);
	    
	    //Recuperar uno que no exista
	    leida = this.indicadorDAO.leerIndicador("IndicaXPrueba");
	    assertNull(leida);	
		
	}
	
	private void testBorrarYLeer() throws SQLException 
	{
		//Borrar el indicador
		this.indicadorDAO.eliminarIndicador(this.indicador1);
		
		//Leer el indicador
		//Recuperarlo por id
		Indicator leida = this.indicadorDAO.leerIndicador(this.indicador1.getIdIndicator());
	    assertNull(leida);
		
		//Recuperarlo por nombre
	    leida = this.indicadorDAO.leerIndicador("IndicadorActualizado");
	    assertNull(leida);
		
	}
}
