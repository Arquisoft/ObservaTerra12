package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;

public class AreasJdbc {

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
	 * Registra un nuevo area, iterando tambien por las subareas para ir
	 * añadiéndolas una a una en caso de que no existan previamente. Solo bajará
	 * un nivel, es decir, solo creará tambien las subareas del parámetro, pero
	 * no bajará mas.
	 * 
	 * @param area
	 *            - Area y subareas a crear.
	 * @throws SQLException
	 */
	public void crearAreaySubAreas(Area area) throws SQLException {
		try {
			con.setAutoCommit(false);
			Long identificadorArea = calcularProximoIdentificadorArea();
			crearArea(area, identificadorArea);
			if (area.getAreas() != null && !area.getAreas().isEmpty()) {
				for (Area areas : area.getAreas()) {
					identificadorArea++;
					crearArea(areas, identificadorArea);
					asociarSubarea(area, areas);
				}
			}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}
	}

	/**
	 * Función auxiliar que registra un nuevo area. ATENCIÓN: Este método deberá
	 * ejecutarse dentro de una transacción abierta.
	 * 
	 * @param area
	 *            - Area a crear.
	 * @return nueva area creada, incluyendo su identificador único.
	 * @throws SQLException
	 */
	private Area crearArea(Area area, Long idNuevaArea) throws SQLException {
		String SQL = "INSERT INTO AREAS (ID_AREA, NOMBRE_AREA) VALUES (?,?)";

		PreparedStatement pst = null;

		area.setId_area(idNuevaArea);
		pst = con.prepareStatement(SQL);
		pst.setLong(1, area.getId_area());
		pst.setString(2, area.getName());
		pst.executeUpdate();
		
		pst.close();

		return area;
	}

	/**
	 * Calcula el próximo identificador del área, en base al último registrado
	 * en la base de datos.
	 * 
	 * @return - Próximo identificador del área.
	 * @throws SQLException
	 */
	private Long calcularProximoIdentificadorArea() throws SQLException {
		String SQL = "SELECT max(id_area) as maximo FROM areas";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		Long id = null;
		while (rs.next()) {
			id = rs.getLong("maximo");
		}

		rs.close();
		pst.close();

		return id + 1;
	}

	/**
	 * Elimina un area determinada, incluyendo también las subareas (en caso de
	 * que no tengan ningún indicador asociado).
	 * 
	 * @param area
	 *            - Area a eliminar.
	 * @throws SQLException
	 */
	public void eliminarArea(Area area) throws SQLException {
		try {
			con.setAutoCommit(false);
			if (area.getAreas() != null && !area.getAreas().isEmpty()) {
				for (Area areas : area.getAreas()) {
					eliminarReferenciaSubarea(areas.getId_area(),
							area.getId_area());
					eliminarSubarea(areas.getId_area());
				}
			}
			eliminarSubarea(area.getId_area());
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} 
	}

	/**
	 * Método auxiliar que elimina un area determinada. ATENCIÓN: Este método
	 * deberá ser ejecutado dentro de una transacción abierta.
	 * 
	 * @param idSubarea
	 *            - Identificador único del área a eliminar.
	 * @throws SQLException
	 */
	private void eliminarSubarea(Long idSubarea) throws SQLException {
		String SQL = "DELETE FROM areas WHERE id_area=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idSubarea);
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * Método auxiliar que elimina la relación entre dos areas. ATENCIÓN: Este
	 * método deberá ser ejecutado dentro de una transacción abierta.
	 * 
	 * @param idAreaReferenciada
	 *            - Identificador del area referenciada.
	 * @param idAreaPertenece
	 *            - Identificador del área a la que pertenece.
	 * @throws SQLException
	 */
	private void eliminarReferenciaSubarea(Long idAreaReferenciada,
			Long idAreaPertenece) throws SQLException {
		String SQL = "DELETE FROM pertenece WHERE id_area_referenciada=? AND id_area_pertenece=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idAreaReferenciada);
		pst.setLong(2, idAreaPertenece);
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * Actualiza los datos de un area determinado, sin incluir las posibles
	 * asociaciones.
	 * 
	 * @param area
	 *            - Area a actualizar.
	 * @throws SQLException
	 */
	public void actualizarArea(Area area) throws SQLException {
		String SQL = "UPDATE AREAS SET nombre_area=? WHERE id_area=? ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, area.getName());
		pst.setLong(2, area.getId_area());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Recoge los datos de un area determinado de la base de datos en base a su
	 * nombre.
	 * 
	 * @param nombre
	 *            - Nombre del area.
	 * @return - Area encontrado.
	 * @throws SQLException
	 */
	public Area leerArea(String nombre) throws SQLException {
		String SQL = "SELECT * FROM areas WHERE nombre_area=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombre);
		ResultSet rs = pst.executeQuery();

		Area area = null;
		while (rs.next()) {
			area = new Area();
			area.setId_area(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
		}

		rs.close();
		pst.close();

		return area;
	}

	/**
	 * Recoge los datos de un area determinado de la base de datos en base a su
	 * identificador único.
	 * 
	 * @param idArea
	 *            - Identificador del área.
	 * @return - Area encontrado.
	 * @throws SQLException
	 */
	public Area buscarArea(Long idArea) throws SQLException {
		String SQL = "SELECT * FROM areas WHERE id_area=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idArea);
		ResultSet rs = pst.executeQuery();

		Area area = null;
		while (rs.next()) {
			area = new Area();
			area.setId_area(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
		}

		rs.close();
		pst.close();

		return area;
	}

	/**
	 * Carga las subareas de asociadas a un area determinado, y las incluye en
	 * el area parámetro.
	 * 
	 * @param area
	 *            Area referenciada.
	 * @return - Area y subareas.
	 * @throws SQLException
	 */
	public Area leerSubAreas(Area area) throws SQLException {
		String SQL = "SELECT * FROM pertenece WHERE id_area_pertenece=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, area.getId_area());
		ResultSet rs = pst.executeQuery();

		List<Area> subareas = new ArrayList<Area>();

		while (rs.next()) {
			Area ar = buscarArea(rs.getLong("id_area_referenciada"));
			subareas.add(ar);
		}

		area.setAreas(subareas);
		
		rs.close();pst.close();
		
		return area;
	}

	/**
	 * Método que asocia dos areas: la referenciada y la subarea. Los cambios
	 * quedan registrados en la base de datos.
	 * 
	 * @param areaPertenece
	 *            - Area a la que pertenece.
	 * @param subarea
	 *            - Subarea asociada al area referenciada.
	 * @throws SQLException
	 */
	public void asociarSubarea(Area areaPertenece, Area subarea)
			throws SQLException {
		String SQL = "INSERT INTO PERTENECE (ID_AREA_REFERENCIADA,ID_AREA_PERTENECE) VALUES (?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, subarea.getId_area());
		pst.setLong(2, areaPertenece.getId_area());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Método que anula la asociación de dos areas: la referenciada y la
	 * subarea. Los cambios quedan registrados en la base de datos.
	 * 
	 * @param areaPertenece
	 *            - Area a la que pertenece.
	 * @param subarea
	 *            - Subarea asociada al area referenciada.
	 * @throws SQLException
	 */
	public void anularAsociacionSubarea(Area areaPertenece, Area subarea)
			throws SQLException {
		String SQL = "DELETE FROM PERTENECE WHERE id_area_referenciada=? and id_area_pertenece=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, subarea.getId_area());
		pst.setLong(2, areaPertenece.getId_area());
		pst.executeUpdate();

		pst.close();
	}
}
