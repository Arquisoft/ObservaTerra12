package persistencia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Document;
import model.User;

public interface DocumentosDAO {

	/**
	 * Método que permite guardar cualquier archivo en el repositorio privado
	 * del usuario. Inicialmente no se compartirá con nadie No se recomienda
	 * permitir almacenar archivos *.txt porque no se recuperan del todo bien en
	 * el 100% de los casos.
	 * 
	 * @param Document
	 *            - Archivo a guardar y usuario propietario.
	 * @return archivo guardado en la base de datos.
	 */
	public Document guardarDocumento(Document documento) throws SQLException,
			IOException;

	/**
	 * Recupera un documento del repositorio en base a su identificador único.
	 * 
	 * @param idDocumento
	 *            - Identificador único de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo. No está escrito en
	 *         disco.
	 */
	public Document leerDocumento(Long idDocumento) throws SQLException,
			IOException;

	/**
	 * Borra un documento del repositorio en base a su identificador único. Para
	 * borrarlo, deberá asegurarse de que ya no está compartido con nadie. En
	 * caso contrario, lanzará una SQLException.
	 * 
	 * @param documento
	 *            - Documento a borrar de la base de datos.
	 * 
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public void borrarDocumento(Document documento) throws SQLException;

	/**
	 * Genera un listado con todos los identificadores de los repositorios de un
	 * usuario determinado. Solo obtenemos los identificadores de sus propios
	 * documentos, no de los que han sido compartidos con el.
	 * 
	 * @param usuario
	 *            - Usuario del que queremos saber sus documentos.
	 * @return - Listado de los documentos de un usuario determinado.
	 * @throws SQLException
	 * @throws IOException 
	 */
	public List<Document> listarRepositoriosUsuario(User usuario)
			throws SQLException, IOException;

	/**
	 * Genera un listado con los identificadores de los repositorios compartidos
	 * con un usuario. Este listado no incluirá sus propios repositorios, solo
	 * los que se han compartido con él.
	 * 
	 * @param usuario
	 *            - Usuario determinado.
	 * @return - Listado con los documentos que un usuario determinado tiene
	 *         accesibles, ATENCIÓN: Sin indicar el usuario propietario.
	 * @throws SQLException
	 * @throws IOException 
	 */
	public List<Document> listarRespositoriosAccesiblesUsuario(User usuario)
			throws SQLException, IOException;

	/**
	 * Comparte un repositorio con un usuario determinado. Desde este momento,
	 * ese repositorio queda accesible para el otro usuario totalmente.
	 * 
	 * @param documento
	 *            - Documento a compartir y usuario propietario.
	 * @param usuarioACompartir
	 *            - usuario a compartir con.
	 * 
	 * @throws SQLException
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public void compartirRepositorioConUsuario(Document documento,
			User usuarioACompartir) throws SQLException;

	/**
	 * Anula la compartición de un repositorio con un usuario determinado. Desde
	 * este momento, ese repositorio ya no queda accesible para el otro usuario
	 * totalmente.
	 * 
	 * @param documento
	 *            - documento compartido.
	 * @param usuarioACompartir
	 *            - Usuario que va ya no va a tener acceso al documento.
	 * @throws SQLException
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public void anularCompartirRepositorioConUsuario(Document documento,
			User usuarioACompartir) throws SQLException;

}