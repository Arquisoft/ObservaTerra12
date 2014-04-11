package persistencia.JdbcDAOs;

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

import model.User;
import utils.DBConnection;
import utils.LectorConsultas;

/**
 * Clase encargada de consultar la base de datos
 * para todas las operaciones relacionadas con los 
 * documentos.
 * @author Jose Enrique
 */
public class DocumentosJdbc {
	
	private Connection con;
	
	/**
	 * Establece la conexión a utilizar en las 
	 * operaciones contra la base de datos.
	 * Realiza una primera comprobación de su estado.
	 */
	public void setConnection(Connection con)
	{
		if(con == null)
			throw new IllegalArgumentException("Conexión invalida - parámetro null.")
		
		if(!con.isActive())
			throw new IllegalArgumentException("La conexión no está activa");
		
		this.con = con;
	}
	

	/**
	 * Método que permite guardar cualquier archivo en el repositorio 
	 * privado del usuario.
	 * Inicialmente no se compartirá con nadie
	 * No se recomienda permitir almacenar archivos *.txt porque no
	 * se recuperan del todo bien en el 100% de los casos.
	 * @param usuario - Usuario propietario del archivo.
	 * @param file - Archivo a almacenar en el repositorio.
	 * @return Identificador del archivo guardado en la base de datos, necesario para
	 * poder recuperarlo mas tarde.
	 */
	public long guardarDocumento(User usuario, File file) throws SQLException,
			IOException {

		//Crea la consulta
		String SQL = "INSERT INTO REPOSITORIOS (ID_USUARIO,DOCUMENTO,nombre_documento) VALUES (?,?,?)";

		//Precompila el statement y establece el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUsuario());

		//Convierte el archivo a un chorro de bytes, que permitirá guardarlo mas tarde.
		byte[] fileData = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close();
		pst.setBytes(2, fileData);

		//Almacena el nombre del fichero, necesario para poder restaurarlo mas tarde
		pst.setString(3, file.getName());

		//Inserta el archivo y recupera su identificador.
		pst.executeUpdate();
		Long idDocumento = getIdDocumento(con, usuario.getIdUsuario());

		//Libera los recursos utilizados
		pst.close();
		con.close();
		return idDocumento;
	}

	/**
	 * Función auxiliar que permite recuperar el identificador del repositorio
	 * del último documento cargado por el usuario.
	 * @param con - Conexión abierta a reutilizar.
	 * @param  idUsuario - identificador del usuario que ha cargado los documentos
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

		return idDocumento;
	}

	/**
	 * Recupera un documento del repositorio en base a su identificador único.
	 * 
	 * @param idDocumento
	 *            - Identificador único de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo. No está escrito en disco.
	 */
	public File leerDocumento(long idDocumento) throws SQLException,
			IOException {
		
		// Carga de la consulta
		String SQL = "SELECT documento,nombre_documento FROM REPOSITORIOS where id_repositorio = ?";

		// Preparar el statement, y meter el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idDocumento);
		ResultSet rs = pst.executeQuery();
		File image = null;
		while (rs.next()) {
			// Crea el documento y añade el prefijo 'download'
			image = new File("(download)" + rs.getString("nombre_documento"));
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
		}
		
		//Liberar recursos
		pst.close();
		rs.close();
		con.close();
		
		// Devuelve la imagen del fichero.
		return image;
	}

	/**
	 * Borra un documento del repositorio en base a su identificador único.
	 * Para borrarlo, deberá asegurarse de que ya no está compartido con nadie.
	 * En caso contrario, lanzará una SQLException.
	 * @param idDocumento - Identificador del documento en la base de datos.
	 * @param usuario - Usuario que va a realizar la operación
	 */
	public void borrarDocumento(Long idDocumento) throws SQLException,
			IOException {
		// Carga de la consulta
		String SQL = "DELETE FROM REPOSITORIOS WHERE ID_DOCUMENTO = ?";

		// Preparar el statement, ejecutar la consulta
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.executeUpdate();
		
		//Liberar recursos
		pst.close();
		con.close();
	}
	
	
	/******
	 * Método borrar documento - Comprobar que el que lo borra es el propietario.
	 */
	
	
	
	/**
	 * Genera un listado con todos los identificadores de los repositorios de 
	 * un usuario determinado.
	 * @param usuario - Usuario del que queremos saber sus documentos.
	 * @return - Listado con los identificadores de los documentos de un usuario determinado.
	 */
	public List<Long> listarRepositoriosUsuarios(Usuario usuario)
	{
		String SQL = "SELECT * FROM COMPARTE WHERE ID_USUARIO=?";
	}
	
	/**
	 * Genera un listado con los identificadores de los repositorios
	 * compartidos con un usuario. Este listado no incluirá sus propios repositorios, 
	 * solo los que se han compartido con él.
	 * @param usuario - Usuario determinado.
	 * @return - Listado con los identificadores de los documentos
	 * que un usuario determinado tiene accesibles.
	 */
	public List<Long> listarRespositoriosAccesiblesUsuario(Usuario usuario)
	{
		//TODO
	}
	
	/**
	 * Comparte un repositorio con un usuario determinado.
	 * Desde este momento, ese repositorio queda accesible
	 * para el otro usuario totalmente.
	 * @param idRepositorio - Identificador del documento a compartir.
	 * @param usuarioACompartir - Usuario que va a tener acceso al documento.
	 */
	public void compartirRepositorioConUsuario(Long idRepositorio, Usuario usuarioACompartir)
	{
		//TODO
	}
	
	/**
	 * Anula la compartición de un repositorio con un usuario determinado.
	 * Desde este momento, ese repositorio ya no queda accesible
	 * para el otro usuario totalmente.
	 * @param idRepositorio - Identificador del documento a compartir.
	 * @param usuarioACompartir - Usuario que va ya no va a tener acceso al documento.
	 */
	public void anularCompartirRepositorioConUsuario(Long idRepositorio, Usuario usuarioACompartir)
	{
		//TODO
	}
}
