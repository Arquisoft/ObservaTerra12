package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.crearAreaySubAreas(area);
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
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.eliminarArea(area);
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
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.actualizarArea(area);
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
	 * @see persistencia.JdbcDAOs.AreasDAO#leerArea(java.lang.String)
	 */
	@Override
	public Area leerArea(String nombre) throws SQLException {
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.leerArea(nombre);
		if (a != null)
			a = this.areasJDBC.leerSubAreas(a);
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
		if (a != null)
			a = this.areasJDBC.leerSubAreas(a);
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
		if (area == null)
			throw new IllegalArgumentException("No se ha indicado el area");
		else if (area.getIdArea() == null)
			throw new IllegalArgumentException(
					"No se pueden leer las subareas sin el identificador único");

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
		if (areaPertenece == null || subarea == null)
			throw new IllegalArgumentException(
					"No se ha indicado uno de los areas");
		else if (areaPertenece.getIdArea() == null
				|| subarea.getIdArea() == null)
			throw new IllegalArgumentException(
					"Uno de los areas no dispone de identificador único");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.asociarSubarea(areaPertenece, subarea);
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
	 * @see persistencia.JdbcDAOs.AreasDAO#anularAsociacionSubarea(model.Area,
	 * model.Area)
	 */
	@Override
	public void anularAsociacionSubarea(Area areaPertenece, Area subarea)
			throws SQLException {
		if (areaPertenece == null || subarea == null)
			throw new IllegalArgumentException(
					"No se ha indicado uno de los areas");
		else if (areaPertenece.getIdArea() == null
				|| subarea.getIdArea() == null)
			throw new IllegalArgumentException(
					"Uno de los areas no dispone de identificador único");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.anularAsociacionSubarea(areaPertenece, subarea);
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
	 * @see persistencia.JdbcDAOs.AreasDAO#crearPais(model.Country)
	 */
	@Override
	public void crearPais(Country pais) throws SQLException {
		if (pais == null)
			throw new IllegalArgumentException("No se ha indicado el país");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.areasJDBC.setConnection(con);
			this.areasJDBC.crearAreaySubAreas(pais);
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
	 * @see persistencia.JdbcDAOs.AreasDAO#leerPais(java.lang.String)
	 */
	@Override
	public Country leerPais(String nombre) throws SQLException {
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Country leida = this.areasJDBC.leerPais(nombre);
		if (leida != null)
			leida = (Country) this.areasJDBC.leerSubAreas(leida);
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
		if (idPais == null)
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Country leida = this.areasJDBC.leerPais(idPais);
		if (leida != null)
			leida = (Country) this.areasJDBC.leerSubAreas(leida);
		con.close();
		return leida;
	}

	@Override
	public Area buscarAreaYPaisPorNombre(String name) throws SQLException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.buscarAreaYPaisPorNombre(name);
		if (a != null)
			a = this.areasJDBC.leerSubAreas(a);
		con.close();
		return a;
	}

	/**
	 * Busca un area o un pais en función de su identificador.
	 * 
	 * @param idArea
	 *            - Identificador único.
	 * @return Area/Pais encontrado
	 * @throws SQLException
	 */
	@Override
	public Area buscarAreaYPaisPorId(Long idArea) throws SQLException {
		if (idArea == null)
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		Area a = this.areasJDBC.buscarAreaYPaisPorId(idArea);
		if (a != null)
			a = this.areasJDBC.leerSubAreas(a);
		con.close();
		return a;
	}

	/**
	 * Genera un listado de áreas.
	 * 
	 * @return Listado de áreas
	 * @throws SQLException
	 */
	@Override
	public List<Area> listadoAreas() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		List<Area> leido = this.areasJDBC.listadoAreas();
		List<Area> listadoFinal = new ArrayList<Area>();

		// Iterar por la lista de leidos
		for (Area area : leido) {
			Area areaYsubareas = this.areasJDBC.leerSubAreas(area);
			listadoFinal.add(areaYsubareas);
		}

		con.close();
		return listadoFinal;
	}

	/**
	 * Genera un listado de países.
	 * 
	 * @return Listado de países.
	 * @throws SQLException
	 */
	@Override
	public List<Country> listadoPaises() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		List<Country> leido = this.areasJDBC.listadoPaises();
		List<Country> listadoFinal = new ArrayList<Country>();

		// Iterar por la lista de leidos
		for (Country area : leido) {
			Country areaYsubareas = (Country) this.areasJDBC.leerSubAreas(area);
			listadoFinal.add(areaYsubareas);
		}

		con.close();
		return listadoFinal;
	}

	/**
	 * Listado de areas y países conjuntamente
	 * 
	 * @return Listado de áreas y paises conjuntamente.
	 * @throws SQLException
	 */
	@Override
	public List<Area> listadoAreasYPaises() throws SQLException
	{
		Connection con = DBConnection.getConnection();
		this.areasJDBC.setConnection(con);
		List<Area> leido = this.areasJDBC.listadoAreasYPaises();
		List<Area> listadoFinal = new ArrayList<Area>();

		// Iterar por la lista de leidos
		for (Area area : leido) {
			Area areaYsubareas = this.areasJDBC.leerSubAreas(area);
			listadoFinal.add(areaYsubareas);
		}

		con.close();
		return listadoFinal;
	}
}
