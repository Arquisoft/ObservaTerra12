package persistencia;

import java.sql.SQLException;

import model.Measure;

public interface MedidasDAO {

	/**
	 * Registra una nueva medida en la base de datos.
	 * 
	 * @param measure
	 *            - Medida a registrar.
	 * @return - Medida registrada, con su identificador único.
	 * @throws SQLException
	 */
	public Measure crearMedida(Measure measure) throws SQLException;

	/**
	 * Borra una medida de la base de datos.
	 * 
	 * @param medida
	 *            - Medida a borrar.
	 * @throws SQLException
	 */
	public void borrarMedida(Measure medida) throws SQLException;

	/**
	 * Busca una medida en base a su identificador único.
	 * 
	 * @param identificador
	 *            - Identificador de la medida
	 * @return - Medida encontrada
	 * @throws SQLException
	 */
	public Measure buscarMedidaPorIdentificador(Long identificador)
			throws SQLException;

}