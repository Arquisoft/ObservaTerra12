package tests.PersistenceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.Date;

import model.Submission;
import model.User;

import org.junit.Before;
import org.junit.Test;

import persistencia.EntradasDAO;
import persistencia.PersistenceFactory;


public class EntradasTest {

	public Submission entrada;
	
	@Before
	public void before()
	{
		this.entrada = new Submission();
		this.entrada.setDate(new Date());
		User usuario = new User();
		usuario.setIdUser(1L);
		this.entrada.setUser(usuario);
	}
	
	
	@Test
	public void test() throws SQLException {
		//Añadir una entrada
		EntradasDAO entradaDAO = PersistenceFactory.createEntradasDAO();
		this.entrada = entradaDAO.crearEntrada(this.entrada);
		
		//Recuperarla
		Submission leida = entradaDAO.leerEntrada(this.entrada.getIdSubmission());
		assertEquals(leida, this.entrada);
		
		//Borrarla
		entradaDAO.eliminarEntrada(this.entrada);
		
		//Recuperarla
	    leida = entradaDAO.leerEntrada(this.entrada.getIdSubmission());
		assertNull(leida);
	}

}
