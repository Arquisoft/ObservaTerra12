package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;

import model.Organization;

public class OrganizacionesJDBC {
	
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

	//Crear una organizacion
	public void crearOrganizacion(Organization organizacion)
	{
		
	}	
	
	//Leer una organizacion
	public Organization leerOrganizacion()
	{
		return null;
	}
	
}
