package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Indicator;

public class IndicadoresJdbc {

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
	 * Añade un nuevo indicador a la base de datos.
	 * 
	 * @param indicador
	 *            - Indicador a guardar.
	 * @return - Indicador guardado, con su nuevo identificador único.
	 * @throws SQLException
	 */
	public Indicator añadirIndicador(Indicator indicador) throws SQLException {
		String SQL = "INSERT INTO INDICADORES (ID_INDICADOR,NOMBRE) VALUES (?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		Long proximoID = leerProximoIdentificador();
		pst.setLong(1, proximoID);
		pst.setString(2, indicador.getNombre());
		pst.executeUpdate();

		pst.close();

		indicador.setIdIndicator(proximoID);
		return indicador;
	}

	/**
	 * Función auxiliar que recupera el próximo identificador único a asignar al
	 * item a guardar.
	 * 
	 * @return - Siguiente identificador único.
	 * @throws SQLException
	 */
	private Long leerProximoIdentificador() throws SQLException {
		String SQL = "SELECT max(id_indicador) as maximo FROM indicadores";
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

	/**
	 * Recupera un indicador de la base de datos en función de su identificador
	 * único.
	 * 
	 * @param idIndicador
	 *            - Identificador único del indicador.
	 * @return Identificador encontrado (si se encontró)
	 * @throws SQLException
	 */
	public Indicator leerIndicador(Long idIndicador) throws SQLException {
		String SQL = "SELECT * FROM indicadores WHERE id_indicador=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idIndicador);
		ResultSet rs = pst.executeQuery();

		Indicator indicador = null;

		while (rs.next()) {
			indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			indicador.setNombre(rs.getString("nombre"));
		}

		rs.close();
		pst.close();
		return indicador;
	}

	/**
	 * Recoge un indicador de la base de datos en función de su nombre.
	 * 
	 * @param nombreIndicador
	 *            - Nombre del indicador a recoger
	 * @return Indicador encontrado.
	 * @throws SQLException
	 */
	public Indicator leerIndicador(String nombreIndicador) throws SQLException {
		String SQL = "SELECT * FROM indicadores WHERE nombre=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreIndicador);
		ResultSet rs = pst.executeQuery();

		Indicator indicador = null;

		while (rs.next()) {
			indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			indicador.setNombre(rs.getString("nombre"));
		}

		rs.close();
		pst.close();
		return indicador;
	}

	/**
	 * Elabora un listado con todos los indicadores registrados en el sistema.
	 * 
	 * @return Listado de indicadores registrados en el sistema
	 * @throws SQLException
	 */
	public List<Indicator> listarTodosLosIndicadores() throws SQLException {
		String SQL = "SELECT * FROM indicadores";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Indicator> indicadores = new ArrayList<Indicator>();

		while (rs.next()) {
			Indicator indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			indicador.setNombre(rs.getString("nombre"));
			indicadores.add(indicador);
		}

		rs.close();
		pst.close();
		return indicadores;
	}

	/**
	 * Elimina un indicador del sistema, en base a su identificador único.
	 * 
	 * @param indicador
	 *            - Identificador a eliminar.
	 * @throws SQLException
	 */
	public void eliminarIndicador(Indicator indicador) throws SQLException {
		String SQL = "DELETE FROM indicadores WHERE id_indicador=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, indicador.getIdIndicator());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Actualiza un indicador de la base de datos.
	 * 
	 * @param indicador
	 *            - Identificador a actualizar.
	 * @throws SQLException
	 */
	public void actualizarIndicador(Indicator indicador) throws SQLException {
		String SQL = "UPDATE INDICADORES SET nombre = ? WHERE id_indicador =?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, indicador.getNombre());
		pst.setLong(2, indicador.getIdIndicator());
		pst.executeUpdate();

		pst.close();
	}

}
