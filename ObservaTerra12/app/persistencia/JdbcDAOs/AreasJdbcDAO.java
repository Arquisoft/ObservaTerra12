package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;

import model.Area;
import model.Country;
import persistencia.AreasDAO;
import persistencia.implJdbc.AreasJdbc;
import utils.DBConnection;

public class AreasJdbcDAO implements AreasDAO {

	private AreasJdbc areasJDBC;

	public AreasJdbcDAO() {
		this.areasJDBC = new AreasJdbc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#getAreasJDBC()
	 */
	public AreasJdbc getAreasJDBC() {
		return areasJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.AreasDAO#setAreasJDBC(persistencia.implJdbc.AreasJdbc
	 * )
	 */
	public void setAreasJDBC(AreasJdbc areasJDBC) {
		this.areasJDBC = areasJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#crearArea(model.Area)
	 */
	@Override
	public void crearArea(Area area) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.crearAreaySubAreas(area);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#eliminarArea(model.Area)
	 */
	@Override
	public void eliminarArea(Area area) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.eliminarArea(area);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#actualizarArea(model.Area)
	 */
	@Override
	public void actualizarArea(Area area) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.actualizarArea(area);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#leerArea(java.lang.String)
	 */
	@Override
	public Area leerArea(String nombre) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.leerArea(nombre);
		con.close();
		return a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#buscarArea(java.lang.Long)
	 */
	@Override
	public Area buscarArea(Long idArea) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.buscarArea(idArea);
		con.close();
		return a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#leerSubAreas(model.Area)
	 */
	@Override
	public Area leerSubAreas(Area area) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.leerSubAreas(area);
		con.close();
		return a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#asociarSubarea(model.Area,
	 * model.Area)
	 */
	@Override
	public void asociarSubarea(Area areaPertenece, Area subarea)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.asociarSubarea(areaPertenece, subarea);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#anularAsociacionSubarea(model.Area,
	 * model.Area)
	 */
	@Override
	public void anularAsociacionSubarea(Area areaPertenece, Area subarea)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.anularAsociacionSubarea(areaPertenece, subarea);
		con.close();
	}

	@Override
	public void crearPais(Country pais) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Country leerPais(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country leerPais(Long idPais) {
		// TODO Auto-generated method stub
		return null;
	}

}
