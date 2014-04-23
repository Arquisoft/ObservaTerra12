package persistencia;

import java.sql.SQLException;

import model.Submission;

public interface EntradasDAO {

	/**
	 * Registra una nueva entrada en el sistema. Puede incluir (o no) el usuario
	 * que ha introducido la observacion (Aunque debería introducirse)
	 * 
	 * @param entrada
	 *            - Fecha y usuario que hizo la observación.
	 * @return Entrada creada, con su identificador único.
	 * @throws SQLException
	 */
	public abstract Submission crearEntrada(Submission entrada)
			throws SQLException;

	/**
	 * Elimina una entrada en el sistema. No está previsto su uso.
	 * 
	 * @param entrada
	 *            - Entrada a eliminar.
	 * @throws SQLException
	 */
	public abstract void eliminarEntrada(Submission entrada)
			throws SQLException;

	/**
	 * Recoge una entrada del sistema en base a su identificador único
	 * ********************** Atención: solo devuelve el identificador del
	 * usuario que hizo la entrada, deberá recogerse a ese usuario en capas
	 * superiores. **********************
	 * 
	 * @param idEntrada
	 *            - Identificador de la entrada a recoger
	 * @return - Entrada recogida.
	 * @throws SQLException
	 */
	public abstract Submission leerEntrada(Long idEntrada) throws SQLException;

}