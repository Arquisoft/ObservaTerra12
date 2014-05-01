package persistencia.implJdbc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Document;
import model.User;

/**
 * Clase encargada de consultar la base de datos para todas las operaciones
 * relacionadas con los documentos.
 * 
 * @author Jose Enrique
 */
public class DocumentosJdbc {

	private Connection con;

	/**
	 * Establece la conexión a utilizar en las operaciones contra la base de
	 * datos. Realiza una primera comprobación de su estado.
	 * 
	 * @throws SQLException
	 */
	public void setConnection(Connection con) throws SQLException {
		if (con == null)
			throw new IllegalArgumentException(
					"Conexión invalida - parámetro null.");

		if (con.isClosed())
			throw new IllegalArgumentException("La conexión no está activa");

		this.con = con;
	}

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
	public long guardarDocumento(User usuario, File file, String nombreDocumento) throws SQLException,
			IOException {

		// Crea la consulta
		String SQL = "INSERT INTO REPOSITORIOS (ID_USUARIO,DOCUMENTO,NOMBRE_DOCUMENTO) VALUES (?,?,?)";

		// Precompila el statement y establece el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUser());

		// Convierte el archivo a un chorro de bytes, que permitirá guardarlo
		// mas tarde.
		byte[] fileData = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close();
		pst.setBytes(2, fileData);

		// Almacena el nombre del fichero, necesario para poder restaurarlo mas
		// tarde
		pst.setString(3, nombreDocumento);

		// Inserta el archivo y recupera su identificador.
		pst.executeUpdate();
		Long idDocumento = getIdDocumento(con, usuario.getIdUser());

		// Libera los recursos utilizados
		pst.close();
		return idDocumento;
	}

	/**
	 * Función auxiliar que permite recuperar el identificador del repositorio
	 * del último documento cargado por el usuario.
	 * 
	 * @param con
	 *            - Conexión abierta a reutilizar.
	 * @param idUsuario
	 *            - identificador del usuario que ha cargado los documentos
	 * @return - Identificador del último repositorio cargado por el usuario.
	 */
	private Long getIdDocumento(Connection con, Long idUsuario)
			throws SQLException {

		String SQL = "SELECT max(id_repositorio) as id_repositorio from repositorios where id_usuario = ?";
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idUsuario);
		ResultSet rs = pst.executeQuery();
		Long idDocumento = null;

		while (rs.next()) {
			idDocumento = rs.getLong("id_repositorio");
		}
		
		rs.close();pst.close();

		return idDocumento;
	}

	/**
	 * Recupera un documento del repositorio en base a su identificador único.
	 * 
	 * @param idDocumento
	 *            - Identificador único de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo. No está escrito en
	 *         disco.
	 */
	public Document leerDocumento(Long idDocumento) throws SQLException,
			IOException {

		// Carga de la consulta
		String SQL = "SELECT * FROM REPOSITORIOS where id_repositorio = ?";

		// Preparar el statement, y meter el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idDocumento);
		ResultSet rs = pst.executeQuery();
		File image = null;
		String nombre = null;
		User usuario = null;
		while (rs.next()) {
			// Crea el documento 
			image = new File(rs.getString("nombre_documento"));
			FileOutputStream fos = new FileOutputStream(image);
			InputStream inputStream = rs.getBinaryStream("documento");
			// Carga de datos del chorro de bytes
			byte[] buffer = new byte[256];
			while (inputStream.read(buffer) > 0) {
				fos.write(buffer);
			}
			// Cierre del recurso
			fos.flush();
			fos.close();
			
			usuario = new User();
			usuario.setIdUser( rs.getLong("id_usuario"));
			nombre = rs.getString("nombre_documento");
		}

		// Liberar recursos
		pst.close();
		rs.close();

		// Devuelve la imagen del fichero.
		Document documento = new Document();
		documento.setFile(image);
		documento.setUser(usuario);
		documento.setIdDocumento(idDocumento);
		documento.setName(nombre);
		return documento;
	}

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
	public void borrarDocumento(User user, Long idDocumento)
			throws SQLException {

		if (!esPropietario(user, idDocumento))
			throw new SecurityException(
					"No es el propietario del documento: petición denegada.");

		// Carga de la consulta
		String SQL = "DELETE FROM REPOSITORIOS WHERE ID_REPOSITORIO = ?";

		// Preparar el statement, ejecutar la consulta
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idDocumento);
		pst.executeUpdate();

		// Liberar recursos
		pst.close();
	}

	/**
	 * Método que comprueba si un usuario es el propietario de un repositorio.
	 * 
	 * @param user
	 *            - Usuario a comprobar.
	 * @param idDocumento
	 *            - Identificador del repositorio.
	 * @return booleano resultado.
	 * @throws SQLException
	 */
	private boolean esPropietario(User user, Long idRepositorio)
			throws SQLException {
		// Definición de SQL
		String SQL = "SELECT id_usuario FROM repositorios WHERE id_repositorio = ?";

		// Carga del statement y ejecución de la consulta
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idRepositorio);
		ResultSet rs = pst.executeQuery();

		// Recogida de datos
		Long idUsuario = null;
		while (rs.next()) {
			idUsuario = rs.getLong("ID_USUARIO");
		}

		// Cierre de los recursos
		rs.close();

		// Devuelvo el resultado
		if (idUsuario != null && idUsuario == user.getIdUser())
			return true;
		return false;
	}

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
	public List<Long> listarRepositoriosUsuario(User usuario)
			throws SQLException {
		// Definición de SQL
		String SQL = "SELECT id_repositorio FROM repositorios WHERE id_usuario = ?";

		// Carga del statement y ejecución de la consulta
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUser());
		ResultSet rs = pst.executeQuery();

		// Recogida de datos
		List<Long> repositorios = new ArrayList<Long>();
		while (rs.next()) {
			Long id = rs.getLong("ID_REPOSITORIO");
			repositorios.add(id);
		}

		// Cierre de los recursos
		rs.close();
		pst.close();

		// Devuelvo el resultado
		return repositorios;
	}

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
	public List<Long> listarRespositoriosAccesiblesUsuario(User usuario)
			throws SQLException {
		// Definición de SQL
		String SQL = "SELECT id_repositorio FROM comparte WHERE id_usuario=?";

		// Carga del statement y ejecución de la consulta
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUser());
		ResultSet rs = pst.executeQuery();

		// Recogida de datos
		List<Long> repositorios = new ArrayList<Long>();
		while (rs.next()) {
			Long id = rs.getLong("ID_REPOSITORIO");
			repositorios.add(id);
		}

		// Cierre de los recursos
		rs.close();
		pst.close();

		// Devuelvo el resultado
		return repositorios;
	}

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
	public void compartirRepositorioConUsuario(Long idRepositorio,
			User usuarioACompartir, User usuarioPropietario)
			throws SQLException {
		// Compruebo que el usuario que comparte sea el propietario-en caso
		// contrario, no autorizado.
		if (!esPropietario(usuarioPropietario, idRepositorio))
			throw new SecurityException(
					"No es el propietario del documento: petición denegada.");

		// Definición de la consulta
		String SQL = "INSERT INTO COMPARTE (ID_USUARIO,ID_REPOSITORIO) VALUES (?,?)";

		// Carga de la consulta y ejecución
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuarioACompartir.getIdUser());
		pst.setLong(2, idRepositorio);
		pst.executeUpdate();

		// Cierre de recursos
		pst.close();
	}

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
	public void anularCompartirRepositorioConUsuario(Long idRepositorio,
			User usuarioACompartir, User usuarioPropietario)
			throws SQLException {
		// Compruebo que el usuario que comparte sea el propietario-en caso
		// contrario, no autorizado.
		if (!esPropietario(usuarioPropietario, idRepositorio))
			throw new SecurityException(
					"No es el propietario del documento: petición denegada.");

		// Definición de la consulta
		String SQL = "DELETE FROM COMPARTE WHERE ID_USUARIO = ? AND ID_REPOSITORIO = ?";

		// Carga de la consulta y ejecución
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuarioACompartir.getIdUser());
		pst.setLong(2, idRepositorio);
		pst.executeUpdate();

		// Cierre de recursos
		pst.close();
	}

	/**
	 * Borra todas las comparticiones de un documento de una tacada.
	 * @param documento - Documento seleccionado
	 * @throws SQLException 
	 */
	public void anularComparticionesDocumento(Document documento) throws SQLException 
	{
		if(documento == null || documento.getIdDocumento() == null || documento.getUser() == null)
			throw new IllegalArgumentException("Faltan argumentos en el documento.");
		if (!esPropietario(documento.getUser(), documento.getIdDocumento()))
			throw new SecurityException("No es el propietario del documento: petición denegada.");

		// Definición de la consulta
		String SQL = "DELETE FROM COMPARTE WHERE ID_REPOSITORIO = ?";

		// Carga de la consulta y ejecución
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, documento.getIdDocumento());
		pst.executeUpdate();

		// Cierre de recursos
		pst.close();
		
	}
}
