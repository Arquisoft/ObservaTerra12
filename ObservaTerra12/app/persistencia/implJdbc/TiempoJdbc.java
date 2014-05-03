package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import utils.QueryReader;

import model.Time;

public class TiempoJdbc {
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
	 * Registra un nuevo intervalo en la base de datos.
	 * 
	 * @param intervalo
	 *            - Intervalo a registrar
	 * @return - Intervalo registrado, con su identificador único.
	 * @throws SQLException
	 */
	public Time crearIntervalo(Time intervalo) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("CREAR_INTERVALO");

		Long id = leerProximoIdentificador();
		intervalo.setIdTime(id);

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, id);
		pst.setLong(2, intervalo.getStartDate().getTime());
		// Si tiene fecha de fin de intervalo
		if (intervalo.getEndDate() != null) {
			pst.setLong(3, intervalo.getEndDate().getTime());
		} else {
			pst.setNull(3, java.sql.Types.BIGINT);
		}
		pst.executeUpdate();

		pst.close();

		return intervalo;
	}

	/**
	 * Función auxiliar que recupera el próximo identificador único a asignar al
	 * item a guardar.
	 * 
	 * @return - Siguiente identificador único.
	 * @throws SQLException
	 */
	private Long leerProximoIdentificador() throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("LEER_MAX_ID_INTERVALO");
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
	 * Borra un intervalo de la base de datos
	 * 
	 * @param intervalo
	 *            - Intervalo a borrar
	 * @throws SQLException
	 */
	public void borrarIntervalo(Time intervalo) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BORRAR_INTERVALO");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, intervalo.getIdTime());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Busca un intervalo en la base de datos en base a su identificador único.
	 * 
	 * @param idIntervalo
	 *            - Identificador del intervalo a buscar.
	 * @return - Intervalo encontrado.
	 * @throws SQLException
	 */
	public Time buscarIntervaloTiempo(Long idIntervalo) throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_INTERVALO_TIEMPO_ID");

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idIntervalo);
		ResultSet rs = pst.executeQuery();

		Time tiempo = null;

		while (rs.next()) {
			tiempo = new Time();
			tiempo.setIdTime(rs.getLong("id_intervalo"));
			tiempo.setStartDate(new Date(rs.getLong("fecha_principio")));
			tiempo.setEndDate(new Date(rs.getLong("fecha_fin")));
		}

		rs.close();
		pst.close();

		return tiempo;
	}

	/**
	 * Busca un intervalo de tiempo según su contenido.
	 * 
	 * @param startDate
	 *            - Fecha de inicio
	 * @param endDate
	 *            - Fecha de fin
	 * @return Tiempo encontrado
	 * @throws SQLException
	 */
	public Time buscarIntervaloTiempo(Date startDate, Date endDate)
			throws SQLException {
		String SQL = QueryReader.instanciar().leerPropiedad("BUSCAR_INTERVALO_FECHAS");
		String SQL_SOLO_START_DATE = QueryReader.instanciar().leerPropiedad("BUSCAR_INTERVALO_UNA_FECHA");

		ResultSet rs = null;
		PreparedStatement pst = null;

		if (endDate == null) { // Si solo tiene fecha de principio
			pst = con.prepareStatement(SQL_SOLO_START_DATE);
			pst.setLong(1, startDate.getTime());
			rs = pst.executeQuery();
		} else { // Si también tiene fecha de fin
			pst = con.prepareStatement(SQL);
			pst.setLong(1, startDate.getTime());
			pst.setLong(2, endDate.getTime());
			rs = pst.executeQuery();
		}

		Time intervalo = null;
		while (rs.next()) {
			intervalo = new Time();
			intervalo.setIdTime(rs.getLong("id_intervalo"));
			intervalo.setStartDate(startDate);
			intervalo.setEndDate(endDate);
		}

		rs.close();
		pst.close();

		return intervalo;
	}

}
