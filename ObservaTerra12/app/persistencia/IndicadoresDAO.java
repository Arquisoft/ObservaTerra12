package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.Indicator;

public interface IndicadoresDAO {

	/**
	 * Añade un nuevo indicador a la base de datos.
	 * 
	 * @param indicador
	 *            - Indicador a guardar.
	 * @return - Indicador guardado, con su nuevo identificador único.
	 * @throws SQLException
	 */
	public abstract Indicator añadirIndicador(Indicator indicador)
			throws SQLException;

	/**
	 * Recupera un indicador de la base de datos en función de su identificador
	 * único.
	 * 
	 * @param idIndicador
	 *            - Identificador único del indicador.
	 * @return Identificador encontrado (si se encontró)
	 * @throws SQLException
	 */
	public abstract Indicator leerIndicador(Long idIndicador)
			throws SQLException;

	/**
	 * Recoge un indicador de la base de datos en función de su nombre.
	 * 
	 * @param nombreIndicador
	 *            - Nombre del indicador a recoger
	 * @return Indicador encontrado.
	 * @throws SQLException
	 */
	public abstract Indicator leerIndicador(String nombreIndicador)
			throws SQLException;

	/**
	 * Elabora un listado con todos los indicadores registrados en el sistema.
	 * 
	 * @return Listado de indicadores registrados en el sistema
	 * @throws SQLException
	 */
	public abstract List<Indicator> listarTodosLosIndicadores()
			throws SQLException;

	/**
	 * Elimina un indicador del sistema, en base a su identificador único.
	 * 
	 * @param indicador
	 *            - Identificador a eliminar.
	 * @throws SQLException
	 */
	public abstract void eliminarIndicador(Indicator indicador)
			throws SQLException;

	/**
	 * Actualiza un indicador de la base de datos.
	 * 
	 * @param indicador
	 *            - Identificador a actualizar.
	 * @throws SQLException
	 */
	public abstract void actualizarIndicador(Indicator indicador)
			throws SQLException;

}