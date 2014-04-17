import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.User;

import org.junit.Before;
import org.junit.Test;

import persistencia.implJdbc.UsuariosJdbc;
import utils.DBConnection;


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
		org.setIdOrganizacion(0L);
		
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
		UsuariosJdbc usuariosJDBC = new UsuariosJdbc();
		Connection con = DBConnection.getConnection();
		usuariosJDBC.setConnection( con );
		this.usuario = usuariosJDBC.crearUsuario(this.usuario);
		con.commit();
		
		//Probar a recuperarlo individualmente
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( con );
		User usuarioLeido = usuariosJDBC.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNotNull(usuarioLeido);
		assertEquals(usuario.getName(), usuarioLeido.getName());
		assertEquals(usuario.getRol(), usuarioLeido.getRol());
		assertNull(usuarioLeido.getEmail());
		assertNull(usuarioLeido.getSurname());
		
		//Probar a listar todos
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( con );
		List<User> usuariosLeidos = usuariosJDBC.listarUsuarios();
		
		assertTrue( usuariosLeidos.contains(this.usuario) );
		
		con.close();
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
		UsuariosJdbc usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( DBConnection.getConnection() );
		usuariosJDBC.actualizarUsuario(this.usuario);
		
		
		//Probar a recuperarlo individualmente
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( DBConnection.getConnection() );
		User usuarioLeido = usuariosJDBC.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNotNull(usuarioLeido);
		assertEquals(usuario.getName(), usuarioLeido.getName());
		assertEquals(usuario.getRol(), usuarioLeido.getRol());
		assertNotNull(usuarioLeido.getEmail());
		assertEquals(usuarioLeido.getEmail(), this.usuario.getEmail());
		assertNotNull(usuarioLeido.getSurname());
		assertEquals(usuarioLeido.getSurname(), this.usuario.getSurname());
		
		//Probar a listar todos
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( DBConnection.getConnection() );
		List<User> usuariosLeidos = usuariosJDBC.listarUsuarios();
		
		assertTrue( usuariosLeidos.contains(this.usuario) );
	}
	
	/**
	 * Prueba que se elimine un usuario correctamente
	 * @throws SQLException 
	 */
	public void testSuprimirUsuario() throws SQLException 
	{
		//Borra al nuevo usuario cread0
		UsuariosJdbc usuariosJDBC = new UsuariosJdbc();
		Connection con = DBConnection.getConnection();
		usuariosJDBC.setConnection( con );
		usuariosJDBC.eliminarUsuario(this.usuario);
		con.commit();
		
		//Probar a recuperarlo individualmente
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( con );
		User usuarioLeido = usuariosJDBC.leerUsuario(this.usuario.getUserName(), this.usuario.getPassword() );
		
		assertNull(usuarioLeido);
		
		//Probar a listar todos
		usuariosJDBC = new UsuariosJdbc();
		usuariosJDBC.setConnection( con );
		List<User> usuariosLeidos = usuariosJDBC.listarUsuarios();
		
		assertFalse( usuariosLeidos.contains(this.usuario) );
	}
}
