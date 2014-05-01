package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Country;
import model.Organization;
import model.Provider;

public class OrganizacionesJdbc {

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
		String SQL = "INSERT INTO ORGANIZACION (ID_ORGANIZACION,NOMBRE_ORGANIZACION,TIPO,ES_PROVEEDOR,ID_PAIS) VALUES (?,?,?,?,?)";

		PreparedStatement pst = null;
		try {
			con.setAutoCommit(false);
			// Calcular el proximo identificador
			organizacion.setIdOrganization(proximoIdentificadorOrganizacion());
			// Guardar la organización nueva
			pst = con.prepareStatement(SQL);
			pst.setLong(1, organizacion.getIdOrganization());
			pst.setString(2, organizacion.getNombre());
			pst.setString(3, organizacion.getTipoOrganizacion());

			// registrar como proveedor o no
			if (organizacion instanceof Provider)
				pst.setString(4, "SI");
			else
				pst.setString(4, "NO");
			
			//PAIS
			if(organizacion.getCountry() != null && organizacion.getCountry().getIdArea() != null)
				pst.setLong(5, organizacion.getCountry().getIdArea());
			else
				pst.setNull(5, java.sql.Types.INTEGER);

			pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
		} finally {
			pst.close();
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

		rs.close();
		pst.close();

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
		String SQL = "SELECT * FROM organizacion WHERE id_organizacion = ? and es_proveedor='NO' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idOrganizacion);
		ResultSet rs = pst.executeQuery();

		Organization org = null;

		while (rs.next()) {
			// Comprueba si es proveedora o no
			String esProveedor = rs.getString("es_proveedor");
			switch (esProveedor.toUpperCase()) {
			case ("SI"):
				org = new Provider();
				break;
			case ("NO"):
				org = new Organization();
				break;
			}

			org.setIdOrganization(idOrganizacion);
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
		}

		pst.close();
		rs.close();

		return org;
	}

	/**
	 * Borra una organización determinada del sistema solo si no hay usuarios
	 * registrados que pertenezcan a ella.
	 * 
	 * @param organizacion
	 *            - Organización a borrar.
	 * @throws SQLException
	 */
	public void borrarOrganizacion(Organization organizacion)
			throws SQLException {
		String SQL = "DELETE FROM organizacion WHERE id_organizacion=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, organizacion.getIdOrganization());

		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Actualiza los datos de una organización determinada.
	 * 
	 * @param organizacion
	 *            - Organización a actualizar.
	 * @throws SQLException
	 */
	public void actualizarOrganizacion(Organization organizacion)
			throws SQLException {
		String SQL = "UPDATE ORGANIZACION SET nombre_organizacion=?,tipo=? WHERE id_organizacion=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, organizacion.getNombre());
		pst.setString(2, organizacion.getTipoOrganizacion());
		pst.setLong(3, organizacion.getIdOrganization());

		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Genera un listado con todas las organizaciones registradas en el sistema.
	 * 
	 * @return - Listado de organizaciones registradas.
	 * @throws SQLException
	 */
	public List<Organization> listarOrganizaciones() throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE es_proveedor='NO' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Organization> organizaciones = new ArrayList<Organization>();

		while (rs.next()) {
			Organization org = null;
			// Comprueba si es proveedora o no
			String esProveedor = rs.getString("es_proveedor");
			switch (esProveedor.toUpperCase()) {
			case ("SI"):
				org = new Provider();
				break;
			case ("NO"):
				org = new Organization();
				break;
			}
			org.setIdOrganization(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
			
			organizaciones.add(org);
		}

		rs.close();
		pst.close();

		return organizaciones;
	}

	public List<Provider> listarProveedores() throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE es_proveedor='SI' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Provider> proveedores = new ArrayList<Provider>();

		while (rs.next()) {
			Provider org = new Provider();
			org.setIdOrganization(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
			
			proveedores.add(org);
		}

		pst.close();
		rs.close();

		return proveedores;
	}

	public Provider leerProveedor(Long idProveedor) throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE id_organizacion=? AND es_proveedor='SI' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idProveedor);
		ResultSet rs = pst.executeQuery();

		Provider org = null;

		while (rs.next()) {
			org = new Provider();
			org.setIdOrganization(idProveedor);
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
		}

		pst.close();
		rs.close();

		return org;
	}

	/**
	 * Busca un proveedor por nombre
	 * 
	 * @param nombre
	 *            - Nombre del proveedor
	 * @return proveedor encontrado
	 * @throws SQLException
	 */
	public Provider buscarProveedorPorNombre(String nombre) throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE nombre_organizacion = ? and es_proveedor = 'SI'";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombre);
		ResultSet rs = pst.executeQuery();

		Provider org = null;
		while (rs.next()) {
			org = new Provider();
			org.setIdOrganization(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
		}

		rs.close();
		pst.close();

		return org;
	}

	/**
	 * Recoge un proveedor del sistema en base a su nombre
	 * 
	 * @param nombreProveedor - Nombre del proveedor.
	 * @return Proveedor encontrado
	 * @throws SQLException 
	 */
	public Provider leerProveedor(String nombreProveedor) throws SQLException 
	{
		String SQL = "SELECT * FROM organizacion WHERE nombre_organizacion=? AND es_proveedor='SI' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreProveedor);
		ResultSet rs = pst.executeQuery();

		Provider org = null;

		while (rs.next()) {
			org = new Provider();
			org.setIdOrganization(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
		}

		pst.close();
		rs.close();

		return org;
	}

	/**
	 * Recupera una organización de la base de datos en base a su nombre.
	 * 
	 * @param nombreOrganizacion
	 *            - Nombre de la organización a buscar.
	 * @return - Organización encontrada.
	 * @throws SQLException
	 */
	public Organization buscarOrganizacionPorNombre(String nombreOrganizacion) throws SQLException {
		String SQL = "SELECT * FROM organizacion WHERE nombre_organizacion=? AND es_proveedor='NO' ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreOrganizacion);
		ResultSet rs = pst.executeQuery();

		Organization org = null;

		while (rs.next()) {
			org = new Organization();
			org.setIdOrganization(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));
			
			//País
			Long idPais = rs.getLong("id_pais");
			if(idPais != null)
			{
				Country c = new Country();
				c.setIdArea(idPais);
			}
		}

		pst.close();
		rs.close();

		return org;
	}

}
