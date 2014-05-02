package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.Provider;
import persistencia.OrganizacionesDAO;
import persistencia.implJdbc.AreasJdbc;
import persistencia.implJdbc.OrganizacionesJdbc;
import utils.DBConnection;

public class OrganizacionesJdbcDAO implements OrganizacionesDAO {

	private OrganizacionesJdbc organizacionesJDBC;

	public OrganizacionesJdbcDAO() {
		this.organizacionesJDBC = new OrganizacionesJdbc();
	}

	public OrganizacionesJdbc getOrganizacionesJDBC() {
		return organizacionesJDBC;
	}

	public void setOrganizacionesJDBC(OrganizacionesJdbc organizacionesJDBC) {
		this.organizacionesJDBC = organizacionesJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#crearOrganizacion(model.Organization
	 * )
	 */
	@Override
	public Organization crearOrganizacion(Organization organizacion)
			throws SQLException {
		if (organizacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado la organización a crear");

		Connection con = DBConnection.getConnection();
		Organization org;
		try {
			con.setAutoCommit(false);
			this.organizacionesJDBC.setConnection(con);
			org = this.organizacionesJDBC.crearOrganizacion(organizacion);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#leerOrganizacion(java.lang.Long)
	 */
	@Override
	public Organization leerOrganizacion(Long idOrganizacion)
			throws SQLException {
		if (idOrganizacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador de la organización a leer.");

		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		// Lee la organización
		Organization org = this.organizacionesJDBC.leerOrganizacion(idOrganizacion);

		// Si tiene un país asociado, leerlo
		if (org != null && org.getCountry() != null) // Si tiene país, recogerlo
		{
			AreasJdbc areaJDBC = new AreasJdbc();
			areaJDBC.setConnection(con);
			org.setCountry(areaJDBC.leerPais(org.getCountry().getIdArea()));
		}

		con.close();
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#borrarOrganizacion(model.Organization
	 * )
	 */
	@Override
	public void borrarOrganizacion(Organization organizacion)
			throws SQLException {
		if (organizacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado la organización a borrar.");
		else if (organizacion.getIdOrganization() == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador de la organización a borrar.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.organizacionesJDBC.setConnection(con);
			this.organizacionesJDBC.borrarOrganizacion(organizacion);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#actualizarOrganizacion(model.
	 * Organization)
	 */
	@Override
	public void actualizarOrganizacion(Organization organizacion)
			throws SQLException {
		if (organizacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado la organización a actualizar.");
		else if (organizacion.getIdOrganization() == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador de la organización a actualizar.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.organizacionesJDBC.setConnection(con);
			this.organizacionesJDBC.actualizarOrganizacion(organizacion);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#listarOrganizaciones()
	 */
	@Override
	public List<Organization> listarOrganizaciones() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		// Recoge todas las organizaciones
		List<Organization> org = this.organizacionesJDBC.listarOrganizaciones();

		// Recupera el país de cada una si lo tiene
		for (Organization organiz : org) {
			if (organiz.getCountry() != null) // Si tiene país, recogerlo
			{
				AreasJdbc areaJDBC = new AreasJdbc();
				areaJDBC.setConnection(con);
				organiz.setCountry(areaJDBC.leerPais(organiz.getCountry()
						.getIdArea()));
			}
		}

		con.close();
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#leerProveedor(java.lang.Long)
	 */
	@Override
	public Provider leerProvedor(Long idProveedor) throws SQLException {
		if (idProveedor == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador de la organización a recuperar.");

		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Provider org = this.organizacionesJDBC.leerProveedor(idProveedor);

		if (org != null && org.getCountry() != null) // Si tiene país, recogerlo
		{
			AreasJdbc areaJDBC = new AreasJdbc();
			areaJDBC.setConnection(con);
			org.setCountry(areaJDBC.leerPais(org.getCountry().getIdArea()));
		}

		con.close();
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#crearProveedor(model.Provider)
	 */
	@Override
	public Provider crearProveedor(Provider proveedor) throws SQLException {
		if (proveedor == null)
			throw new IllegalArgumentException(
					"No se ha indicado la organización a crear.");

		Connection con = DBConnection.getConnection();
		Provider org = null;
		try {
			con.setAutoCommit(false);
			this.organizacionesJDBC.setConnection(con);
			org = (Provider) this.organizacionesJDBC
					.crearOrganizacion(proveedor);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#listarProveedores()
	 */
	@Override
	public List<Provider> listarProveedores() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		List<Provider> org = this.organizacionesJDBC.listarProveedores();

		// Si tiene país, recogerlo
		for (Organization organiz : org) {
			if (organiz.getCountry() != null) // Si tiene país, recogerlo
			{
				AreasJdbc areaJDBC = new AreasJdbc();
				areaJDBC.setConnection(con);
				organiz.setCountry(areaJDBC.leerPais(organiz.getCountry()
						.getIdArea()));
			}
		}

		con.close();
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.OrganizacionesDAO#leerProveedor(java.lang.String)
	 */
	@Override
	public Provider leerProvedor(String nombreProveedor) throws SQLException {
		if (nombreProveedor == null || nombreProveedor.isEmpty())
			throw new IllegalArgumentException(
					"No se ha indicado el nombre del proveedor a recuperar.");

		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Provider org = this.organizacionesJDBC.leerProveedor(nombreProveedor);

		// si tiene país recogerlo
		if (org != null && org.getCountry() != null) // Si tiene país, recogerlo
		{
			AreasJdbc areaJDBC = new AreasJdbc();
			areaJDBC.setConnection(con);
			org.setCountry(areaJDBC.leerPais(org.getCountry().getIdArea()));
		}

		con.close();
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
	public Organization buscarOrganizacionPorNombre(String nombreOrganizacion)
			throws SQLException {
		if (nombreOrganizacion == null || nombreOrganizacion.isEmpty())
			throw new IllegalArgumentException(
					"No se ha indicado el nombre de la organizacion a recuperar.");

		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Organization org = this.organizacionesJDBC
				.buscarOrganizacionPorNombre(nombreOrganizacion);

		// Si tiene pais, crearlo
		if (org != null && org.getCountry() != null) // Si tiene país, recogerlo
		{
			AreasJdbc areaJDBC = new AreasJdbc();
			areaJDBC.setConnection(con);
			org.setCountry(areaJDBC.leerPais(org.getCountry().getIdArea()));
		}

		con.close();
		return org;
	}

	/**
	 * Recupera una organización o un proveedor de la base de datos en base a su
	 * nombre.
	 * 
	 * @param nombreOrganizacion
	 *            - Nombre de la organización a buscar.
	 * @return - Organización encontrada.
	 * @throws SQLException
	 */
	@Override
	public Organization buscarOrganizacionOProveedorPorNombre(
			String nombreOrganizacion) throws SQLException {
		if (nombreOrganizacion == null || nombreOrganizacion.isEmpty())
			throw new IllegalArgumentException(
					"No se ha indicado el nombre de la organizacion a recuperar.");

		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Organization org = this.organizacionesJDBC
				.buscarOrganizacionOProveedorPorNombre(nombreOrganizacion);

		// Si tiene país, recogerlo
		if (org != null && org.getCountry() != null) // Si tiene país, recogerlo
		{
			AreasJdbc areaJDBC = new AreasJdbc();
			areaJDBC.setConnection(con);
			org.setCountry(areaJDBC.leerPais(org.getCountry().getIdArea()));
		}

		con.close();
		return org;
	}
}
