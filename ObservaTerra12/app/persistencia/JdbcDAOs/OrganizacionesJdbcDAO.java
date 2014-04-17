package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Organization;
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

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#crearOrganizacion(model.Organization)
	 */
	@Override
	public Organization crearOrganizacion(Organization organizacion)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Organization org = this.organizacionesJDBC
				.crearOrganizacion(organizacion);
		con.close();
		return org;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#leerOrganizacion(java.lang.Long)
	 */
	@Override
	public Organization leerOrganizacion(Long idOrganizacion)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		Organization org = this.organizacionesJDBC
				.leerOrganizacion(idOrganizacion);
		con.close();
		return org;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#borrarOrganizacion(model.Organization)
	 */
	@Override
	public void borrarOrganizacion(Organization organizacion)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		this.organizacionesJDBC.borrarOrganizacion(organizacion);
		con.close();
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.OrganizacionesDAO#actualizarOrganizacion(model.Organization)
	 */
	@Override
	public void actualizarOrganizacion(Organization organizacion)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.organizacionesJDBC.setConnection(con);
		this.organizacionesJDBC.actualizarOrganizacion(organizacion);
		con.close();
	}

	/* (non-Javadoc)
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
}
