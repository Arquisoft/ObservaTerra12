package persistencia;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.User;

public interface DocumentosDAO {

	/**
	 * Método que permite guardar cualquier archivo en el repositorio privado
	 * del usuario. Inicialmente no se compartirá con nadie No se recomienda
	 * permitir almacenar archivos *.txt porque no se recuperan del todo bien en
	 * el 100% de los casos.
	 * 
	 * @param usuario
	 *            - Usuario propietario del archivo.
	 * @param file
	 *            - Archivo a almacenar en el repositorio.
	 * @return Identificador del archivo guardado en la base de datos, necesario
	 *         para poder recuperarlo mas tarde.
	 */
	public  Long guardarDocumento(User usuario, File file)
			throws SQLException, IOException;

	/**
	 * Recupera un documento del repositorio en base a su identificador único.
	 * 
	 * @param idDocumento
	 *            - Identificador único de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo. No está escrito en
	 *         disco.
	 */
	public  File leerDocumento(long idDocumento) throws SQLException,
			IOException;

	/**
	 * Borra un documento del repositorio en base a su identificador único. Para
	 * borrarlo, deberá asegurarse de que ya no está compartido con nadie. En
	 * caso contrario, lanzará una SQLException.
	 * 
	 * @param idDocumento
	 *            - Identificador del documento en la base de datos.
	 * @param usuario
	 *            - Usuario que va a realizar la operación
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public  void borrarDocumento(User user, Long idDocumento)
			throws SQLException;

	/**
	 * Genera un listado con todos los identificadores de los repositorios de un
	 * usuario determinado. Solo obtenemos los identificadores de sus propios
	 * documentos, no de los que han sido compartidos con el.
	 * 
	 * @param usuario
	 *            - Usuario del que queremos saber sus documentos.
	 * @return - Listado con los identificadores de los documentos de un usuario
	 *         determinado.
	 * @throws SQLException
	 */
	public  List<Long> listarRepositoriosUsuario(User usuario)
			throws SQLException;

	/**
	 * Genera un listado con los identificadores de los repositorios compartidos
	 * con un usuario. Este listado no incluirá sus propios repositorios, solo
	 * los que se han compartido con él.
	 * 
	 * @param usuario
	 *            - Usuario determinado.
	 * @return - Listado con los identificadores de los documentos que un
	 *         usuario determinado tiene accesibles.
	 * @throws SQLException
	 */
	public  List<Long> listarRespositoriosAccesiblesUsuario(User usuario)
			throws SQLException;

	/**
	 * Comparte un repositorio con un usuario determinado. Desde este momento,
	 * ese repositorio queda accesible para el otro usuario totalmente.
	 * 
	 * @param idRepositorio
	 *            - Identificador del documento a compartir.
	 * @param usuarioACompartir
	 *            - Usuario que va a tener acceso al documento.
	 * @param usuarioPropietario
	 *            - Usuario propietario del documento (por cuestiones de
	 *            seguridad)
	 * @throws SQLException
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public  void compartirRepositorioConUsuario(Long idRepositorio,
			User usuarioACompartir, User usuarioPropietario)
			throws SQLException;

	/**
	 * Anula la compartición de un repositorio con un usuario determinado. Desde
	 * este momento, ese repositorio ya no queda accesible para el otro usuario
	 * totalmente.
	 * 
	 * @param idRepositorio
	 *            - Identificador del documento a compartir.
	 * @param usuarioACompartir
	 *            - Usuario que va ya no va a tener acceso al documento.
	 * @param usuarioPropietario
	 *            - Usuario propietario del documento (por motivos de seguridad)
	 * @throws SQLException
	 * @throws SecurityException
	 *             - El usuario propietario no coincide con el registrado en la
	 *             base de datos.
	 */
	public  void anularCompartirRepositorioConUsuario(
			Long idRepositorio, User usuarioACompartir, User usuarioPropietario)
			throws SQLException;

}