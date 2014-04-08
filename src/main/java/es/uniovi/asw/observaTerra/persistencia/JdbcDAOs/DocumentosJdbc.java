package es.uniovi.asw.observaTerra.persistencia.JdbcDAOs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.uniovi.asw.observaTerra.model.User;
import es.uniovi.asw.observaTerra.utils.DBConnection;
import es.uniovi.asw.observaTerra.utils.LectorConsultas;

public class DocumentosJdbc 
{
	
	public long guardarDocumento(User usuario, File file) throws SQLException, FileNotFoundException
	{
		//Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = LectorConsultas.get("GUARDAR_DOCUMENTO");
		
		//Preparar el statement, y meter el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUsuario()  );
		
		//Meter el chorro de bytes
		FileInputStream fis = new FileInputStream(file);
		pst.setBinaryStream(2, (InputStream)fis, (int)file.length());
		
		//Set the document name
		pst.setString(3, file.getName());
		
		pst.executeUpdate();	
		
		Long idDocumento = getIdDocumento(con, usuario.getIdUsuario());
		
		con.close();
		return idDocumento;
	}

	private long getIdDocumento(Connection con, long idUsuario) throws SQLException 
	{
		String SQL = LectorConsultas.get("MAX_ID_DOCUMENTO");
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idUsuario);
		ResultSet rs = pst.executeQuery();
		Long idDocumento = null;
		
		while(rs.next())
		{
			idDocumento = rs.getLong("id_repositorio");
		}
		
		return idDocumento;		
	}
	
	public File leerDocumento(long idDocumento) throws SQLException, IOException
	{
		//Carga de la consulta
		Connection con = DBConnection.getConnection();
		String SQL = LectorConsultas.get("LEER_DOCUMENTO");
		
		//Preparar el statement, y meter el id del usuario
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idDocumento);
		ResultSet rs = pst.executeQuery();
		File file = null;		
		while(rs.next())
		{
			Blob blob = rs.getBlob("documento");
			byte[] bytes = blob.getBytes(0, (int)blob.length());
			file = new File(rs.getString("nombre_documento"));
			file.createNewFile();
			FileOutputStream f1 = new FileOutputStream(file);
			f1.write(bytes);
			f1.flush();
			f1.close();
		}
		
		return file;
	}
	
}
