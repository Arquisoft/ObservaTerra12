package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.Submission;
import model.User;

public class EntradasJdbc {

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
	 * Registra una nueva entrada en el sistema. Puede incluir (o no) el usuario
	 * que ha introducido la observacion (Aunque debería introducirse)
	 * 
	 * @param entrada
	 *            - Fecha y usuario que hizo la observación.
	 * @return Entrada creada, con su identificador único.
	 * @throws SQLException
	 */
	public Submission crearEntrada(Submission entrada) throws SQLException {
		String SQL = "INSERT INTO ENTRADAS (ID_ENTRADA,ID_USUARIO,FECHA_ENTRADA) VALUES (?,?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		Long idEntrada = leerProximoIdentificador();
		pst.setLong(1, idEntrada);
		
		//Distinguir si trae o no usuario		
		if (entrada.getUser() != null) {
			pst.setLong(2, entrada.getUser().getIdUser());
		} else
			pst.setNull(3, java.sql.Types.INTEGER);
		
		pst.setLong(3, entrada.getDate().getTime());
		pst.executeUpdate();

		pst.close();

		entrada.setIdSubmission(idEntrada);
		return entrada;
	}

	/**
	 * Elimina una entrada en el sistema. No está previsto su uso.
	 * 
	 * @param entrada
	 *            - Entrada a eliminar.
	 * @throws SQLException
	 */
	public void eliminarEntrada(Submission entrada) throws SQLException {
		String SQL = "DELETE FROM entradas WHERE id_entrada=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, entrada.getIdSubmission());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Recoge una entrada del sistema en base a su identificador único
	 * ********************** 
	 * Atención: solo devuelve el identificador del
	 * usuario que hizo la entrada, deberá recogerse a ese usuario en capas
	 * superiores. 
	 * **********************
	 * 
	 * @param idEntrada
	 *            - Identificador de la entrada a recoger
	 * @return - Entrada recogida.
	 * @throws SQLException
	 */
	public Submission leerEntrada(Long idEntrada) throws SQLException {
		String SQL = "SELECT * FROM entradas WHERE id_entrada=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idEntrada);
		ResultSet rs = pst.executeQuery();

		Submission entrada = null;

		while (rs.next()) {
			entrada = new Submission();
			entrada.setIdSubmission(rs.getLong("id_entrada"));
			User usuario = new User();
			Long idUsuario = rs.getLong("id_usuario");
			if (idUsuario != null)
				usuario.setIdUser(idUsuario);
			entrada.setUser(usuario);
			entrada.setDate(new Date(rs.getLong("fecha_entrada")));
		}

		rs.close();
		pst.close();

		return entrada;
	}

	/**
	 * Función auxiliar que recupera el próximo identificador único a asignar al
	 * item a guardar.
	 * 
	 * @return - Siguiente identificador único.
	 * @throws SQLException
	 */
	private Long leerProximoIdentificador() throws SQLException {
		String SQL = "SELECT max(id_entrada) as maximo FROM entradas";
		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();
		Long resultado = null;
		while (rs.next()) {
			resultado = rs.getLong("maximo");
		}

		rs.close();
		pst.close();

		return resultado + 1;
	}

}
