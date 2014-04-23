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
		if (area == null)
			throw new IllegalArgumentException(
					"No se ha indicado un area a crear");

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
		if (area == null)
			throw new IllegalArgumentException(
					"No se ha indicado el area a eliminar");
		else if (area.getIdArea() == null)
			throw new IllegalArgumentException(
					"No se puede borrar un área sin identificador único");

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
		if (area == null)
			throw new IllegalArgumentException(
					"No se ha indicado el area a actualizar");
		else if (area.getIdArea() == null)
			throw new IllegalArgumentException(
					"No se puede actualizar un área sin identificador único");

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
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("Parámetro malformado");

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
		if (idArea == null)
			throw new IllegalArgumentException("Parámetro malformado");

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
	public Area leerSubAreas(Area area) throws SQLException 
	{
		if (area == null)
			throw new IllegalArgumentException("No se ha indicado el area");
		else if (area.getIdArea() == null)
			throw new IllegalArgumentException("No se pueden leer las subareas sin el identificador único");
		
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
	public void asociarSubarea(Area areaPertenece, Area subarea) throws SQLException {
		if (areaPertenece == null || subarea == null)
			throw new IllegalArgumentException("No se ha indicado uno de los areas");
		else if (areaPertenece.getIdArea() == null || subarea.getIdArea() == null)
			throw new IllegalArgumentException("Uno de los areas no dispone de identificador único");
		
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
	public void anularAsociacionSubarea(Area areaPertenece, Area subarea) throws SQLException {
		if (areaPertenece == null || subarea == null)
			throw new IllegalArgumentException("No se ha indicado uno de los areas");
		else if (areaPertenece.getIdArea() == null || subarea.getIdArea() == null)
			throw new IllegalArgumentException("Uno de los areas no dispone de identificador único");
		
		
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.anularAsociacionSubarea(areaPertenece, subarea);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#crearPais(model.Country)
	 */
	@Override
	public void crearPais(Country pais) throws SQLException 
	{
		if(pais == null)
			throw new IllegalArgumentException("No se ha indicado el país");
		
		
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		this.areasJDBC.crearAreaySubAreas(pais);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#leerPais(java.lang.String)
	 */
	@Override
	public Country leerPais(String nombre) throws SQLException {
		if(nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("Parámetro malformado");
		
		
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Country leida = this.areasJDBC.leerPais(nombre);
		con.close();
		return leida;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.AreasDAO#leerPais(java.lang.Long)
	 */
	@Override
	public Country leerPais(Long idPais) throws SQLException {
		if(idPais == null)
			throw new IllegalArgumentException("Parámetro malformado");
		
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Country leida = this.areasJDBC.leerPais(idPais);
		con.close();
		return leida;
	}

}
