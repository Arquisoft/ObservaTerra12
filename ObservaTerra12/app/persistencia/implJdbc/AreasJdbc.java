package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.QueryReader;

import model.Area;
import model.Country;

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
		// Calcula el próximo identificador
		Long identificadorArea = calcularProximoIdentificadorArea();
		// Crea el area principal
		crearArea(area, identificadorArea);
		// Si tiene subareas asociadas, las itera creándolas.
		if (area.getAreas() != null && !area.getAreas().isEmpty()) {
			for (Area areas : area.getAreas()) {
				identificadorArea++;
				crearArea(areas, identificadorArea);
				asociarSubarea(area, areas);
			}
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
		String SQL = QueryReader.instanciar().leerPropiedad("CREAR_AREA");

		PreparedStatement pst = null;

		area.setIdArea(idNuevaArea);
		pst = con.prepareStatement(SQL);
		pst.setLong(1, area.getIdArea());
		pst.setString(2, area.getName());

		// Diferenciar si es un país o no
		if (area instanceof Country) {
			pst.setString(3, "SI");
		} else {
			pst.setString(3, "NO");
		}

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
		String SQL = QueryReader.instanciar().leerPropiedad("MAX_ID_AREA");

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
		// Si tiene subareas, las itera borrándolas
		if (area.getAreas() != null && !area.getAreas().isEmpty()) {
			for (Area areas : area.getAreas()) {
				eliminarReferenciaSubarea(areas.getIdArea(), area.getIdArea());
				eliminarSubarea(areas.getIdArea());
			}
		}
		// Si es parte de otra area, eliminar la referencia
		eliminarParteArea(area.getIdArea());

		// Elimina el área principal
		eliminarSubarea(area.getIdArea());
	}

	/**
	 * Método auxiliar que elimina a un area como parte de otras mayores.
	 * 
	 * @param idArea
	 *            - Area seleccionada.
	 * @throws SQLException
	 */
	private void eliminarParteArea(Long idArea) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BORRAR_PERTENECE");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idArea);
		pst.executeUpdate();
		pst.close();
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
		String SQL = QueryReader.instanciar().leerPropiedad("ELIMINAR_AREA");

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
		String SQL = QueryReader.instanciar().leerPropiedad("ELIMINAR_REFERENCIA_SUBAREA");

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
		String SQL = QueryReader.instanciar().leerPropiedad("ACTUALIZAR_AREA");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, area.getName());
		pst.setLong(2, area.getIdArea());
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
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_AREA");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombre);
		ResultSet rs = pst.executeQuery();

		Area area = null;
		while (rs.next()) {
			area = new Area();
			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
		}

		rs.close();
		pst.close();

		return area;
	}

	/**
	 * Recoge los datos de un pais determinado en base a su nombre.
	 * 
	 * @param nombre
	 *            - Nombre del pais.
	 * @return País encontrado (si existe alguno)
	 * @throws SQLException
	 */
	public Country leerPais(String nombre) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_PAIS_POR_NOMBRE");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombre);
		ResultSet rs = pst.executeQuery();

		Country area = null;
		while (rs.next()) {
			area = new Country();
			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
		}

		rs.close();
		pst.close();

		return area;
	}

	/**
	 * Recoge los datos de un pais determinado en base a su identificador único.
	 * 
	 * @param idPais
	 *            - Identificador del país
	 * @return País encontrado (si existe alguno)
	 * @throws SQLException
	 */
	public Country leerPais(Long idPais) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_PAIS_POR_ID");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idPais);
		ResultSet rs = pst.executeQuery();

		Country area = null;
		while (rs.next()) {
			area = new Country();
			area.setIdArea(rs.getLong("id_area"));
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
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_AREA_POR_ID");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idArea);
		ResultSet rs = pst.executeQuery();

		Area area = null;
		while (rs.next()) {
			area = new Area();
			area.setIdArea(rs.getLong("id_area"));
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
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_SUBAREAS");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, area.getIdArea());
		ResultSet rs = pst.executeQuery();

		List<Area> subareas = new ArrayList<Area>();

		while (rs.next()) {
			Area ar = buscarArea(rs.getLong("id_area_referenciada"));
			subareas.add(ar);
		}

		area.setAreas(subareas);

		rs.close();
		pst.close();

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
		String SQL = QueryReader.instanciar().leerPropiedad("ASOCIAR_SUBAREA");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, subarea.getIdArea());
		pst.setLong(2, areaPertenece.getIdArea());
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
		String SQL = QueryReader.instanciar().leerPropiedad("ANULAR_ASOCIACION_SUBAREA");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, subarea.getIdArea());
		pst.setLong(2, areaPertenece.getIdArea());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Busca un area o un pais en función de su nombre.
	 * 
	 * @param name
	 *            - Nombre del area
	 * @return Area/Pais encontrado
	 * @throws SQLException
	 */
	public Area buscarAreaYPaisPorNombre(String name) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_AREA_POR_NOMBRE");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, name);
		ResultSet rs = pst.executeQuery();

		Area area = null;

		while (rs.next()) {
			switch (rs.getString("es_pais").toUpperCase()) {
			case ("SI"):
				area = new Country();
				break;
			case ("NO"):
				area = new Area();
				break;
			}
			area.setIdArea(rs.getLong("id_area"));
			area.setName(name);
		}

		rs.close();
		pst.close();
		return area;
	}

	/**
	 * Busca un area o un pais en función de su identificador.
	 * 
	 * @param idArea
	 *            - Identificador único.
	 * @return Area/Pais encontrado
	 * @throws SQLException
	 */
	public Area buscarAreaYPaisPorId(Long idArea) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_AREA_POR_ID");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idArea);
		ResultSet rs = pst.executeQuery();

		Area area = null;

		while (rs.next()) {
			switch (rs.getString("es_pais").toUpperCase()) {
			case ("SI"):
				area = new Country();
				break;
			case ("NO"):
				area = new Area();
				break;
			}
			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
		}

		rs.close();
		pst.close();
		return area;

	}

	/**
	 * Genera un listado de áreas.
	 * 
	 * @return Listado de áreas
	 * @throws SQLException
	 */
	public List<Area> listadoAreas() throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LISTADO_AREAS");

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Area> listado = new ArrayList<Area>();

		while (rs.next()) {
			Area area = new Area();
			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
			listado.add(area);
		}

		return listado;
	}

	/**
	 * Genera un listado de países.
	 * 
	 * @return Listado de países.
	 * @throws SQLException
	 */
	public List<Country> listadoPaises() throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LISTADO_PAISES");

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Country> listado = new ArrayList<Country>();

		while (rs.next()) {
			Country area = new Country();
			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
			listado.add(area);
		}

		return listado;
	}

	/**
	 * Listado de areas y países conjuntamente
	 * 
	 * @return Listado de áreas y paises conjuntamente.
	 * @throws SQLException 
	 */
	public List<Area> listadoAreasYPaises() throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LISTADO_AREAS_Y_PAISES");

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Area> listado = new ArrayList<Area>();

		while (rs.next()) {
			Area area = null;
			
			//Comprobación es pais
			String esPais = rs.getString("es_pais");
			switch (esPais.toUpperCase()) {
			case ("SI"):
				 area = new Country();
				break;
			case ("NO"):
				 area = new Area();
				break;
			}

			area.setIdArea(rs.getLong("id_area"));
			area.setName(rs.getString("nombre_area"));
			listado.add(area);
		}

		return listado;
	}

}
