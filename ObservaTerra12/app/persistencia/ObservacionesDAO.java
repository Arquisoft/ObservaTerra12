package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.Area;
import model.Observation;

public interface ObservacionesDAO {

	/**
	 * Inserta una nueva observación en la base de datos. Este método cuenta con
	 * que previamente ya se han almacenado las medidas, los tiempos, las areas,
	 * y solo guarda las referencias a las mismas.
	 * 
	 * @param observacion
	 *            - Observacion a insertar
	 * @return Observacion insertada, mas el identificador único.
	 * @throws SQLException
	 */
	public  Observation insertarObservacion(Observation observacion)
			throws SQLException;

	/**
	 * Este método elimina una observación, contando con que después alguien
	 * eliminará las medidas, areas, etc que tenía asignadas.
	 * 
	 * @param observacion
	 *            - Observación a eliminar.
	 * @throws SQLException
	 */
	public  void eliminarObservacion(Observation observacion)
			throws SQLException;

	/**
	 * Busca y recupera una observación en base a su identificador único.
	 * Recupera tambíen los identificadores únicos de los elementos que componen
	 * la observación, que deberán ser leídos uno a uno posteriormente.
	 * 
	 * @param identificador
	 *            - Identificador de la observación a buscar.
	 * @return Observación encontrada.
	 * @throws SQLException
	 */
	public  Observation buscarObservacionPorIdentificador(
			Long identificador) throws SQLException;

	/**
	 * Busca y recupera un listado de observaciones en base al área al que
	 * pertenecen. Recupera tambíen los identificadores únicos de los elementos
	 * que componen la observación, que deberán ser leídos uno a uno
	 * posteriormente.
	 * 
	 * @param area
	 *            - Area determinada.
	 * @return Listado de observaciones encontradas
	 * @throws SQLException
	 */
	public  List<Observation> leerObservacionesDeUnArea(Area area)
			throws SQLException;

	/**
	 * Busca y recupera un listado de observaciones en base al nombre del
	 * indicador que pretenden medir. Recupera tambíen los identificadores
	 * únicos de los elementos que componen la observación, que deberán ser
	 * leídos uno a uno posteriormente.
	 * 
	 * @param nombreIndicador
	 *            = Nombre del indicador deseado.
	 * @return Listado de observaciones encontradas.
	 * @throws SQLException
	 */
	public  List<Observation> leerObservacionesDeUnIndicador(
			String nombreIndicador) throws SQLException;

}