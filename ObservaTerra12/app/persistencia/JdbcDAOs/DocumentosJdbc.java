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

	public long guardarDocumento(User usuario, File file) throws SQLException,
			IOException {

		Connection con = DBConnection.getConnection();
		String SQL = LectorConsultas.get("GUARDAR_DOCUMENTO");


		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUsuario());


		byte[] fileData = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close();
		pst.setBytes(2, fileData);

		pst.setString(3, file.getName());

		pst.executeUpdate();

		Long idDocumento = getIdDocumento(con, usuario.getIdUsuario());

		con.close();
		return idDocumento;
	}

	private long getIdDocumento(Connection con, long idUsuario)
			throws SQLException {

		String SQL = LectorConsultas.get("MAX_ID_DOCUMENTO");
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
	 * Carga un documento del repositorio en base a su identificador único.
	 * 
	 * @param idDocumento
	 *            - Identificador único de la base de datos del documento.
	 * @return - Documento a crear cargado en memoria, ojo.
	 * @throws SQLException
	 * @throws IOException
	 */
	public File leerDocumento(long idDocumento) throws SQLException,
			IOException {
		// Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = LectorConsultas.get("LEER_DOCUMENTO");

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
		// Devuelve la imagen del fichero.
		return image;
	}

	public void borrarDocumento(long idDocumento) throws SQLException,
			IOException {
		// Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = LectorConsultas.get("ELIMINAR_DOCUMENTO");

		// Preparar el statement,
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.executeUpdate();
	}
}
