package persistencia;

import java.sql.SQLException;

import model.Area;

public interface AreasDAO {

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
	public  void crearArea(Area area) throws SQLException;

	/**
	 * Elimina un area determinada, incluyendo también las subareas (en caso de
	 * que no tengan ningún indicador asociado).
	 * 
	 * @param area
	 *            - Area a eliminar.
	 * @throws SQLException
	 */
	public  void eliminarArea(Area area) throws SQLException;

	/**
	 * Actualiza los datos de un area determinado, sin incluir las posibles
	 * asociaciones.
	 * 
	 * @param area
	 *            - Area a actualizar.
	 * @throws SQLException
	 */
	public  void actualizarArea(Area area) throws SQLException;

	/**
	 * Recoge los datos de un area determinado de la base de datos en base a su
	 * nombre.
	 * 
	 * @param nombre
	 *            - Nombre del area.
	 * @return - Area encontrado.
	 * @throws SQLException
	 */
	public  Area leerArea(String nombre) throws SQLException;

	/**
	 * Recoge los datos de un area determinado de la base de datos en base a su
	 * identificador único.
	 * 
	 * @param idArea
	 *            - Identificador del área.
	 * @return - Area encontrado.
	 * @throws SQLException
	 */
	public  Area buscarArea(Long idArea) throws SQLException;

	/**
	 * Carga las subareas de asociadas a un area determinado, y las incluye en
	 * el area parámetro.
	 * 
	 * @param area
	 *            Area referenciada.
	 * @return - Area y subareas.
	 * @throws SQLException
	 */
	public  Area leerSubAreas(Area area) throws SQLException;

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
	public  void asociarSubarea(Area areaPertenece, Area subarea)
			throws SQLException;

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
	public  void anularAsociacionSubarea(Area areaPertenece,
			Area subarea) throws SQLException;

}