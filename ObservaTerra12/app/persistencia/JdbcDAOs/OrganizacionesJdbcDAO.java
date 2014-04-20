package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.Provider;
import persistencia.OrganizacionesDAO;
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
		this.organizacionesJDBC.setConnection(con);
		Organization org = this.organizacionesJDBC
				.crearOrganizacion(organizacion);
		con.close();
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
		Organization org = this.organizacionesJDBC
				.leerOrganizacion(idOrganizacion);
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
		this.organizacionesJDBC.setConnection(con);
		this.organizacionesJDBC.borrarOrganizacion(organizacion);
		con.close();
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
		this.organizacionesJDBC.setConnection(con);
		this.organizacionesJDBC.actualizarOrganizacion(organizacion);
		con.close();
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
		List<Organization> org = this.organizacionesJDBC.listarOrganizaciones();
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
		this.organizacionesJDBC.setConnection(con);
		Provider org = (Provider) this.organizacionesJDBC.crearOrganizacion(proveedor);
		con.close();
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
		con.close();
		return org;
	}
}
