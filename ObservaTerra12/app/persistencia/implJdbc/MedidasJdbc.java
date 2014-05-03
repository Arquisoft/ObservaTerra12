package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.QueryReader;

import model.Measure;

public class MedidasJdbc {
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
	 * Registra una nueva medida en la base de datos.
	 * 
	 * @param measure
	 *            - Medida a registrar.
	 * @return - Medida registrada, con su identificador único.
	 * @throws SQLException
	 */
	public Measure crearMedida(Measure measure) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("CREAR_MEDIDA");

		Long id = leerProximoIdentificador();
		measure.setIdMeasure(id);

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, id);
		pst.setString(2, measure.getValue());
		pst.setString(3, measure.getUnit());
		pst.executeUpdate();

		pst.close();

		return measure;
	}

	/**
	 * Función auxiliar que recupera el próximo identificador único a asignar al
	 * item a guardar.
	 * 
	 * @return - Siguiente identificador único.
	 * @throws SQLException
	 */
	private Long leerProximoIdentificador() throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_MAX_ID_MEDIDA");
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
	 * Borra una medida de la base de datos.
	 * 
	 * @param medida
	 *            - Medida a borrar.
	 * @throws SQLException
	 */
	public void borrarMedida(Measure medida) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BORRAR_MEDIDA");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, medida.getIdMeasure());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Busca una medida en base a su identificador único.
	 * 
	 * @param identificador
	 *            - Identificador de la medida
	 * @return - Medida encontrada
	 * @throws SQLException
	 */
	public Measure buscarMedidaPorIdentificador(Long identificador)
			throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_MEDIDA_POR_ID");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, identificador);
		ResultSet rs = pst.executeQuery();

		Measure medida = null;

		while (rs.next()) {
			medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			medida.setValue(rs.getString("valor_medida"));
			medida.setUnit(rs.getString("unidad_medida"));
		}

		rs.close();
		pst.close();

		return medida;
	}

	/**
	 * Busca una medida en base a su contenido (valor y unidad)
	 * 
	 * @param value
	 *            - Valor de la medida
	 * @param unit
	 *            - Unidad de la medida
	 * @return - Medida encontrada
	 * @throws SQLException
	 */
	public Measure buscarMedidaPorValorYUnidad(String value, String unit)
			throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_MEDIDA_POR_VALOR");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, value);
		pst.setString(2, unit);
		ResultSet rs = pst.executeQuery();

		Measure medida = null;
		while (rs.next()) {
			medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			medida.setValue(value);
			medida.setUnit(unit);
		}

		rs.close();
		pst.close();

		return medida;
	}

}
