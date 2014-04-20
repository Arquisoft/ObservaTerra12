package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Indicator;
import persistencia.IndicadoresDAO;
import persistencia.implJdbc.IndicadoresJdbc;
import utils.DBConnection;

public class IndicadoresJdbcDAO implements IndicadoresDAO {

	private IndicadoresJdbc indicadoresJDBC;

	public IndicadoresJdbcDAO() {
		this.indicadoresJDBC = new IndicadoresJdbc();
	}

	public IndicadoresJdbc getIndicadoresJDBC() {
		return indicadoresJDBC;
	}

	public void setIndicadoresJDBC(IndicadoresJdbc indicadoresJDBC) {
		this.indicadoresJDBC = indicadoresJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.IndicadoresDAO#añadirIndicador(model.Indicator)
	 */
	@Override
	public Indicator añadirIndicador(Indicator indicador) throws SQLException {
		if (indicador == null)
			throw new IllegalArgumentException(
					"No se ha insertado un indicador valido.");

		Connection con = DBConnection.getConnection();

		Indicator indic = null;
		try {
			con.setAutoCommit(false);
			this.indicadoresJDBC.setConnection(con);
			indic = this.indicadoresJDBC.añadirIndicador(indicador);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException("Error completando la transacción.", e);
		} finally {
			con.close();
		}

		return indic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.IndicadoresDAO#leerIndicador(java.lang.Long)
	 */
	@Override
	public Indicator leerIndicador(Long idIndicador) throws SQLException {
		if (idIndicador == null)
			throw new IllegalArgumentException(
					"No se ha insertado un indicador valido.");

		Connection con = DBConnection.getConnection();
		this.indicadoresJDBC.setConnection(con);
		Indicator indicador = this.indicadoresJDBC.leerIndicador(idIndicador);
		con.close();
		return indicador;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.IndicadoresDAO#leerIndicador(java.lang.String)
	 */
	@Override
	public Indicator leerIndicador(String nombreIndicador) throws SQLException {
		if (nombreIndicador == null)
			throw new IllegalArgumentException(
					"No se ha insertado un nombre de indicador valido.");

		Connection con = DBConnection.getConnection();
		this.indicadoresJDBC.setConnection(con);
		Indicator indicador = this.indicadoresJDBC
				.leerIndicador(nombreIndicador);
		con.close();
		return indicador;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.IndicadoresDAO#listarTodosLosIndicadores()
	 */
	@Override
	public List<Indicator> listarTodosLosIndicadores() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.indicadoresJDBC.setConnection(con);
		List<Indicator> indicador = this.indicadoresJDBC
				.listarTodosLosIndicadores();
		con.close();
		return indicador;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.IndicadoresDAO#eliminarIndicador(model.Indicator)
	 */
	@Override
	public void eliminarIndicador(Indicator indicador) throws SQLException {
		if (indicador == null)
			throw new IllegalArgumentException(
					"No se ha indicado el indicador a eliminar.");
		else if (indicador.getIdIndicator() == null)
			throw new IllegalArgumentException(
					"No se puede eliminar sin el identificador único.");

		Connection con = DBConnection.getConnection();
		this.indicadoresJDBC.setConnection(con);
		this.indicadoresJDBC.eliminarIndicador(indicador);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.IndicadoresDAO#actualizarIndicador(model.Indicator)
	 */
	@Override
	public void actualizarIndicador(Indicator indicador) throws SQLException {
		if (indicador == null)
			throw new IllegalArgumentException(
					"No se ha indicado el indicador a actualizar.");
		else if (indicador.getIdIndicator() == null)
			throw new IllegalArgumentException(
					"No se puede actualizar sin el identificador único.");

		Connection con = DBConnection.getConnection();
		this.indicadoresJDBC.setConnection(con);
		this.indicadoresJDBC.actualizarIndicador(indicador);
		con.close();
	}

}
