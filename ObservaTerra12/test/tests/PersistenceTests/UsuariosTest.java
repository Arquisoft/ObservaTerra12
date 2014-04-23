package tests.PersistenceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.User;

import org.junit.Before;
import org.junit.Test;

import persistencia.PersistenceFactory;
import persistencia.UsuariosDAO;


public class UsuariosTest {

	private User usuario;
	
	@Before
	public void before()
	{
		this.usuario = new User();
		this.usuario.setUserName("username");
		this.usuario.setPassword("password");
		this.usuario.setRol("ADMINISTRADOR");
		this.usuario.setName("admin001");
		
		Organization org = new Organization();
		org.setIdOrganization(0L);
		
		this.usuario.setOrganization(org);		
	}
	
	@Test
	public void test() throws SQLException
	{
		testCrearUsuario();
		testActualizarUsuario();
		testSuprimirUsuario();
	}
	
	
	/**
	 * Prueba que se cree un usuario correctamente
	 * @throws SQLException 
	 */
	public void testCrearUsuario() throws SQLException 
	{
		//Guarda al nuevo usuario cread0
		UsuariosDAO usuariosDAO = PersistenceFactory.createUsuariosDAO();
		this.usuario = usuariosDAO.crearUsuario(this.usuario);
		
		//Probar a recuperarlo individualmente
		User usuarioLeido = usuariosDAO.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNotNull(usuarioLeido);
		assertEquals(usuario.getName(), usuarioLeido.getName());
		assertEquals(usuario.getRol(), usuarioLeido.getRol());
		
		//Probar a listar todos
		List<User> usuariosLeidos = usuariosDAO.listarUsuarios();
		
		assertTrue( usuariosLeidos.contains(this.usuario) );
	}
	
	/**
	 * Prueba que se actualicen los datos del usuario correctamente
	 * @throws SQLException 
	 */
	public void testActualizarUsuario() throws SQLException 
	{
		//Actualiza los datos del usuario
		this.usuario.setEmail("hola@yahoo.com");
		this.usuario.setSurname("surname");
		this.usuario.setName("hello");
		
		//Baja los cambios a la base de datos
		UsuariosDAO usuariosDAO = PersistenceFactory.createUsuariosDAO();
		usuariosDAO.actualizarUsuario(this.usuario);
		
		
		//Probar a recuperarlo individualmente
		User usuarioLeido = usuariosDAO.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNotNull(usuarioLeido);
		assertEquals(usuario.getName(), usuarioLeido.getName());
		assertEquals(usuario.getRol(), usuarioLeido.getRol());
		assertNotNull(usuarioLeido.getEmail());
		assertEquals(usuarioLeido.getEmail(), this.usuario.getEmail());
		assertNotNull(usuarioLeido.getSurname());
		assertEquals(usuarioLeido.getSurname(), this.usuario.getSurname());
		
		//Probar a listar todos
		List<User> usuariosLeidos = usuariosDAO.listarUsuarios();
		
		assertTrue( usuariosLeidos.contains(this.usuario) );
	}
	
	/**
	 * Prueba que se elimine un usuario correctamente
	 * @throws SQLException 
	 */
	public void testSuprimirUsuario() throws SQLException 
	{
		//Borra al nuevo usuario cread0
		UsuariosDAO usuariosDAO = PersistenceFactory.createUsuariosDAO();
		usuariosDAO.eliminarUsuario(this.usuario);
		
		//Probar a recuperarlo individualmente
		User usuarioLeido = usuariosDAO.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNull(usuarioLeido);
		
		//Probar a listar todos
		List<User> usuariosLeidos = usuariosDAO.listarUsuarios();
		
		assertFalse( usuariosLeidos.contains(this.usuario) );
	}
}