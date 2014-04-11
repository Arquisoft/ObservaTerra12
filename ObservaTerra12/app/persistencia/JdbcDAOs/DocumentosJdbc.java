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

public class DocumentosJdbc {

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

		//Establece la conexión y crea la consulta
		Connection con = DBConnection.getConnection();
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
	 * 
	 */
	private long getIdDocumento(Connection con, long idUsuario)
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
	 * Carga un documento del repositorio en base a su identificador Ãºnico.
	 * 
	 * @param idDocumento
	 *            - Identificador Ãºnico de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo.
	 * @throws SQLException
	 * @throws IOException
	 */
	public File leerDocumento(long idDocumento) throws SQLException,
			IOException {
		// Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = "SELECT documento,nombre_documento FROM REPOSITORIOS where id_repositorio = ?";

		// Preparar el statement, y meter el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idDocumento);
		ResultSet rs = pst.executeQuery();
		File image = null;
		while (rs.next()) {
			// Crea el documento y aÃ±ade el prefijo 'download'
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
		// Devuelve la imagen del fichero.
		return image;
	}

	public void borrarDocumento(long idDocumento) throws SQLException,
			IOException {
		// Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = "DELETE FROM REPOSITORIOS WHERE ID_DOCUMENTO = ?";

		// Preparar el statement,
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.executeUpdate();
	}
	
	public List<Long> listarRepositoriosUsuarios(Usuario usuario)
	{
		
	}
	
	public List<Long> listarRespositoriosAccesiblesUsuario(Usuario usuario)
	{
		
	}
	
	public void compartirRepositorioConUsuario(Long idRepositorio, Usuario usuarioACompartir)
	{
		
	}
}
