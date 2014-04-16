import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.User;

import org.junit.Before;
import org.junit.Test;

import persistence.impl.DocumentosJdbc;
import utils.DBConnection;

public class DocumentosTest {

	private File file;
	private Long idDocumento;
	private User usuario;

	@Before
	public void preparacion() throws Throwable {
		this.file = new File("readme");
		this.usuario = new User();
		this.usuario.setIdUsuario(1);
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
		java.sql.Connection con = DBConnection.getConnection();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);

		// Insertar documento
		guardarFicheroTexto();

		// Intento de borrado de otro usuario
		User user = new User();
		user.setIdUsuario(34);
		try {
			docs.borrarDocumento(user, idDocumento);
		} finally {
			// Borrar el fichero
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
		java.sql.Connection con = DBConnection.getConnection();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);

		// Comprobar que ahora mismo existe el fichero
		List<Long> repos = docs.listarRepositoriosUsuario(usuario);
		assertFalse(repos.isEmpty());
		assertTrue(repos.contains(idDocumento));

		// borrarlo
		borrarFicheroTexto();

		// Comprobar que se ha eliminado
		con = DBConnection.getConnection();
		docs.setConnection(con);
		repos = docs.listarRepositoriosUsuario(usuario);
		assertFalse(repos.contains(idDocumento));
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
		java.sql.Connection con = DBConnection.getConnection();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);

		User usuario2 = new User();
		usuario2.setIdUsuario(2);

		// Compartir el recurso con el usuario dos
		docs.compartirRepositorioConUsuario(idDocumento, usuario2, usuario);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroCompartido(usuario2);

		// Establecer la conexión
		con = DBConnection.getConnection();
		docs.setConnection(con);

		// Dejar de compartir el recurso con el usuario dos
		docs.anularCompartirRepositorioConUsuario(idDocumento, usuario2,
				usuario);

		// Comprueba que el fichero se ha compartido
		comprobarFicheroNoCompartido(usuario2);

		// borra fichero
		borrarFicheroTexto();
	}

	/**
	 ************************ 
	 ** Métodos auxiliares **
	 ************************ 
	 */

	private void comprobarFicheroNoCompartido(User usuario2)
			throws SQLException {
		// Establecer la conexión
		java.sql.Connection con = DBConnection.getConnection();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);

		// Comprobar que el usuario dos ya no puede ver el repositorio
		List<Long> repos = docs.listarRespositoriosAccesiblesUsuario(usuario2);
		assertFalse(repos.contains(idDocumento));
	}

	private void comprobarFicheroCompartido(User usuario2) throws SQLException {
		// Establecer la conexión
		java.sql.Connection con = DBConnection.getConnection();
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(con);

		// Comprobar que el usuario dos ya no puede ver el repositorio
		List<Long> repos = docs.listarRespositoriosAccesiblesUsuario(usuario2);
		assertTrue(repos.contains(idDocumento));
	}

	/**
	 * Borra el fichero de texto de la base de datos
	 * 
	 * @throws SQLException
	 */
	private void borrarFicheroTexto() throws SQLException {
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(DBConnection.getConnection());
		docs.borrarDocumento(usuario, idDocumento);
	}

	/**
	 * Lee el fichero de la base de datos, y compara linea a linea el contenido
	 * del original y del que recupera
	 */
	private void recuperarYCompararFichero() throws SQLException, IOException,
			FileNotFoundException {
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(DBConnection.getConnection());
		File recovered = docs.leerDocumento(idDocumento);

		// Descomentar para crear fichero recovered.createNewFile();

		BufferedReader bfRecovered = new BufferedReader(new FileReader(
				recovered));
		BufferedReader bfOriginal = new BufferedReader(new FileReader(file));

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
		DocumentosJdbc docs = new DocumentosJdbc();
		docs.setConnection(DBConnection.getConnection());
		idDocumento = docs.guardarDocumento(usuario, file);
	}

}
