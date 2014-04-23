package persistencia;

import java.sql.SQLException;

import model.Time;

public interface TiempoDAO {

	/**
	 * Registra un nuevo intervalo en la base de datos.
	 * 
	 * @param intervalo
	 *            - Intervalo a registrar
	 * @return - Intervalo registrado, con su identificador único.
	 * @throws SQLException
	 */
	public  Time crearIntervalo(Time intervalo) throws SQLException;

	/**
	 * Borra un intervalo de la base de datos
	 * 
	 * @param intervalo
	 *            - Intervalo a borrar
	 * @throws SQLException
	 */
	public  void borrarIntervalo(Time intervalo) throws SQLException;

	/**
	 * Busca un intervalo en la base de datos en base a su identificador único.
	 * 
	 * @param idIntervalo
	 *            - Identificador del intervalo a buscar.
	 * @return - Intervalo encontrado.
	 * @throws SQLException
	 */
	public  Time buscarIntervaloTiempo(Long idIntervalo)
			throws SQLException;

}