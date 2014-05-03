package tests.PersistenceTests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Document;
import model.User;

import org.junit.Before;
import org.junit.Test;

import persistencia.DocumentosDAO;
import persistencia.PersistenceFactory;
import persistencia.implJdbc.DocumentosJdbc;
import utils.DBConnection;

public class DocumentosTest {

	private Document documento;

	@Before
	public void preparacion() throws Throwable {
		User usuario = new User();
		usuario.setIdUser(1L);
		
		this.documento = new Document();
		this.documento.setFile(new File("build.sbt"));
		this.documento.setUser(usuario);
		this.documento.setName("1readme");
	}

	/**
	 * Básicamente, guarda un fichero de texto, lo recupera y compara linea a
	 * linea ambos, y luego lo borra.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void testGuardarBorrarFichero() throws SQLException, IOException {

		guardarFicheroTexto();
		recuperarYCompararFichero();
		borrarFicheroTexto();
	}

	/**
	 * Comprueba que la clase no acepte conexiones invalidas
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConexionCerrada() throws SQLException {
		java.sql.Connection con = DBConnection.getConnection();
		con.close();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);
	}

	/**
	 * Comprueba que no se permite borrar el documento de otro usuario
	 */
	@Test(expected = SecurityException.class)
	public void testBorrarDocumentoOtroUsuario() throws SQLException,
			IOException {
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();

		// Insertar documento
		guardarFicheroTexto();
		
		// Intento de borrado de otro usuario
		User user = new User();
		user.setIdUser(34L);
		try {
			Document d1 = this.documento;
			d1.setUser(user);
			documentosDAO.borrarDocumento(d1);
		} finally {
			// Borrar el fichero
			User usuario = new User();
			usuario.setIdUser(1L);
			this.documento.setUser(usuario);
			borrarFicheroTexto();
		}
		
	}

	/**
	 * Realiza pruebas sobre el método que genera el listado con los documentos
	 * privados del usuario.
	 */
	@Test
	public void listarRepositoriosUsuario() throws SQLException, IOException {
		// Insertar uno
		guardarFicheroTexto();

		// Establecer la conexión
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();

		// Comprobar que ahora mismo existe el fichero
		List<Document> repos = documentosDAO.listarRepositoriosUsuario(this.documento.getUser());
		
		assertFalse(repos.isEmpty());
		assertTrue(repos.contains(this.documento));

		// borrarlo
		borrarFicheroTexto();

		// Comprobar que se ha eliminado
		repos = documentosDAO.listarRepositoriosUsuario(this.documento.getUser());
		assertFalse(repos.contains(this.documento));
	}

	/**
	 * Realiza pruebas con las operaciones de compartir un documento y la de
	 * generar el listado de documentos compartidos con un usuario determinado.
	 */
	@Test
	public void testCompartirDocumento() throws SQLException, IOException {
		// Insertar un documento
		guardarFicheroTexto();

		// Establecer la conexión
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();

		User usuario2 = new User();
		usuario2.setIdUser(2L);

		// Compartir el recurso con el usuario dos
		documentosDAO.compartirRepositorioConUsuario(this.documento, usuario2);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroCompartido(usuario2);

		// Dejar de compartir el recurso con el usuario dos
		documentosDAO.anularCompartirRepositorioConUsuario(this.documento, usuario2);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroNoCompartido(usuario2);

		// borra fichero
		borrarFicheroTexto();
	}
	
	
	/**
	 * Realiza pruebas con las operaciones de compartir un documento y la de
	 * anular todas las comparticiones del mismo.
	 */
	@Test
	public void testAnularComparticionDocumento() throws SQLException, IOException {
		// Insertar un documento
		guardarFicheroTexto();

		// Establecer la conexión
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();

		User usuario2 = new User();
		usuario2.setIdUser(2L);

		// Compartir el recurso con el usuario dos
		documentosDAO.compartirRepositorioConUsuario(this.documento, usuario2);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroCompartido(usuario2);

		//Borra el documento, arrastrando así sus comparticiones
		documentosDAO.borrarDocumento(this.documento);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroNoCompartido(usuario2);
	}

	/**
	 ************************ 
	 ** Métodos auxiliares 
	 * @throws IOException **
	 ************************ 
	 */

	private void comprobarFicheroNoCompartido(User usuario2)
			throws SQLException, IOException {
		// Establecer la conexión
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();
		
		// Comprobar que el usuario dos ya no puede ver el repositorio
		List<Document> repos = documentosDAO.listarRespositoriosAccesiblesUsuario(usuario2);
		assertFalse(repos.contains(this.documento));
	}

	private void comprobarFicheroCompartido(User usuario2) throws SQLException, IOException {
		// Establecer la conexión
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();

		// Comprobar que el usuario dos ya no puede ver el repositorio
		List<Document> repos = documentosDAO.listarRespositoriosAccesiblesUsuario(usuario2);
		assertTrue(repos.contains(this.documento));
	}

	/**
	 * Borra el fichero de texto de la base de datos
	 * 
	 * @throws SQLException
	 */
	private void borrarFicheroTexto() throws SQLException {
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();
		documentosDAO.borrarDocumento(this.documento);
	}

	/**
	 * Lee el fichero de la base de datos, y compara linea a linea el contenido
	 * del original y del que recupera
	 */
	private void recuperarYCompararFichero() throws SQLException, IOException,
			FileNotFoundException {
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();
		File recovered = documentosDAO.leerDocumento(this.documento.getIdDocumento()).getFile();

		// Descomentar para crear fichero recovered.createNewFile();

		BufferedReader bfRecovered = new BufferedReader(new FileReader(recovered));
		BufferedReader bfOriginal = new BufferedReader(new FileReader(this.documento.getFile()));

		String line = bfOriginal.readLine();
		String line2;

		while (line != null) {
			line2 = bfRecovered.readLine();
			org.junit.Assert.assertEquals(line, line2);
			line = bfOriginal.readLine();
		}

		bfRecovered.close();
		bfOriginal.close();
	}

	/**
	 * Inserta el fichero en la base de datos
	 */
	public void guardarFicheroTexto() throws SQLException, IOException {
		// INSERTAR UN ARCHIVO DE PRUEBA
		DocumentosDAO documentosDAO = PersistenceFactory.createDocumentosDAO();		
		this.documento = documentosDAO.guardarDocumento(this.documento);
	}

}
