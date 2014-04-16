package persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import persistence.fachada.OrganizacionesGateway;
import model.Organization;

public class OrganizacionesJdbc implements OrganizacionesGateway {

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
	 * Registra una nueva organización en la base de datos.
	 * 
	 * @param organizacion
	 *            - Nueva organización a crear.
	 * @return - Objeto organización con su nuevo identificador único.
	 * @throws SQLException
	 */
	public Organization crearOrganizacion(Organization organizacion)
			throws SQLException {
		String SQL = "INSERT INTO ORGANIZACION (ID_ORGANIZACION,NOMBRE_ORGANIZACION,TIPO) VALUES (?,?,?)";

		PreparedStatement pst = null;
		try {
			con.setAutoCommit(false);
			// Calcular el proximo identificador
			organizacion.setIdOrganizacion(proximoIdentificadorOrganizacion());
			// Guardar la organización nueva
			pst = con.prepareStatement(SQL);
			pst.setLong(1, organizacion.getIdOrganizacion());
			pst.setString(2, organizacion.getNombre());
			pst.setString(3, organizacion.getTipoOrganizacion());
			pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
		} finally {
			pst.close();
			con.close();
		}

		return organizacion;
	}

	/**
	 * Calcula el proximo identificador que se le asignará a la nueva
	 * organización. ATENCIÓN: Este método deberá ejecutarse dentro de una
	 * transacción abierta.
	 * 
	 * @return - Próximo identificador de registro para una nueva organización.
	 * @throws SQLException
	 */
	private Long proximoIdentificadorOrganizacion() throws SQLException {
		String SQL = "SELECT max(id_organizacion) as maximo FROM organizacion";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();
		Long resultado = null;

		while (rs.next()) {
			resultado = rs.getLong("maximo");
		}

		return resultado + 1;
	}

	/**
	 * Recupera una organización de la base de datos en base a su identificador
	 * único.
	 * 
	 * @param idOrganizacion
	 *            - Identificador de la organización a buscar.
	 * @return - Organización encontrada.
	 * @throws SQLException
	 */
	public Organization leerOrganizacion(Long idOrganizacion)
			throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE id_organizacion = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idOrganizacion);
		ResultSet rs = pst.executeQuery();
		Organization org = null;

		while (rs.next()) {
			org = new Organization();
			org.setIdOrganizacion(idOrganizacion);
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
		}

		pst.close();
		rs.close();
		con.close();

		return org;
	}

	/**
	 * Borra una organización determinada del sistema solo si no hay usuarios
	 * registrados que pertenezcan a ella.
	 * 
	 * @param organizacion - Organización a borrar.
	 * @throws SQLException
	 */
	public void borrarOrganizacion(Organization organizacion)
			throws SQLException {
		String SQL = "DELETE FROM organizacion WHERE id_organizacion=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, organizacion.getIdOrganizacion());

		pst.executeUpdate();

		pst.close();
		con.close();
	}

	/**
	 * Actualiza los datos de una organización determinada.
	 * 
	 * @param organizacion - Organización a actualizar.
	 * @throws SQLException
	 */
	public void actualizarOrganizacion(Organization organizacion)
			throws SQLException {
		String SQL = "UPDATE ORGANIZACION SET nombre_organizacion = ?,tipo = ? where id_organizacion = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, organizacion.getNombre());
		pst.setString(2, organizacion.getTipoOrganizacion());
		pst.setLong(3, organizacion.getIdOrganizacion());

		pst.executeUpdate();

		pst.close();
		con.close();
	}

	/**
	 * Genera un listado con todas las organizaciones registradas en el sistema.
	 * 
	 * @return - Listado de organizaciones registradas.
	 * @throws SQLException 
	 */
	public List<Organization> listarOrganizaciones() throws SQLException
	{
		String SQL = "SELECT * FROM ORGANIZACION";
		
		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();
		
		List<Organization> organizaciones = new ArrayList<Organization>();
		
		while(rs.next())
		{
			Organization org = new Organization();
			org.setIdOrganizacion( rs.getLong("id_organizacion") );
			org.setNombre( rs.getString("nombre_organizacion") );
			org.setTipoOrganizacion( rs.getString("tipo") );
			organizaciones.add( org );
		}
		
		rs.close();
		pst.close();
		con.close();
		
		return organizaciones;
	}
}
