package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.Area;
import model.Observation;
import model.Submission;

public interface ObservacionesDAO {

	/**
	 * Genera un listado con todas las observaciones registradas
	 * 
	 * @return listaado de observacioens registradas
	 * @throws SQLException
	 */
	public List<Observation> listarTodasObservaciones() throws SQLException;

	/**
	 * Inserta una nueva observación en la base de datos. Este método inserta en
	 * una transacción todo el contenido interno de la observación.
	 * En caso de que no se puedan insertar todas, se cancelará la transacción.
	 * 
	 * @param observacion
	 *            - Observacion a insertar
	 * @return Observacion insertada, mas el identificador único.
	 * @throws SQLException
	 */
	public Observation insertarObservacion(Observation observacion)
			throws SQLException;

	/**
	 * Registra una observacion en la base de datos
	 */
	@Deprecated
	public Observation insertarObservacion(Observation observacion,
			Submission submission) throws SQLException;

	/**
	 * Este método elimina una observación y TODO SU CONTENIDO REGISTRADO
	 * transaccionalmente.
	 * 
	 * @param observacion
	 *            - Observación a eliminar.
	 * @throws SQLException
	 */
	public void eliminarObservacion(Observation observacion)
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
	public Observation buscarObservacionPorIdentificador(Long identificador)
			throws SQLException;

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
	public List<Observation> leerObservacionesDeUnArea(Area area)
			throws SQLException;

	/**
	 * Busca y recupera un listado de observaciones en base al nombre del
	 * indicador que pretenden medir. Recupera tambíen el contenido de
	 * las mismas.
	 * 
	 * @param nombreIndicador
	 *            = Nombre del indicador deseado.
	 * @return Listado de observaciones encontradas.
	 * @throws SQLException
	 */
	public List<Observation> leerObservacionesDeUnIndicador(String nombreIndicador) throws SQLException;

}