package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import model.Time;
import persistencia.TiempoDAO;
import persistencia.implJdbc.TiempoJdbc;
import utils.DBConnection;

public class TiempoJdbcDAO implements TiempoDAO {
	private TiempoJdbc tiempoJDBC;

	public TiempoJdbcDAO() {
		this.tiempoJDBC = new TiempoJdbc();
	}

	public TiempoJdbc getTiempoJDBC() {
		return tiempoJDBC;
	}

	public void setTiempoJDBC(TiempoJdbc tiempoJDBC) {
		this.tiempoJDBC = tiempoJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.TiempoDAO#crearIntervalo(model.Time)
	 */
	@Override
	public Time crearIntervalo(Time intervalo) throws SQLException {
		if (intervalo == null)
			throw new IllegalArgumentException(
					"No se ha indicado el intervalo a crear.");

		Connection con = DBConnection.getConnection();
		Time t = null;
		try {
			con.setAutoCommit(false);
			this.tiempoJDBC.setConnection(con);
			t = this.tiempoJDBC.crearIntervalo(intervalo);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.TiempoDAO#borrarIntervalo(model.Time)
	 */
	@Override
	public void borrarIntervalo(Time intervalo) throws SQLException {
		if (intervalo == null)
			throw new IllegalArgumentException(
					"No se ha indicado el intervalo a crear.");
		else if (intervalo.getIdTime() == null)
			throw new IllegalArgumentException(
					"No se puede borrar el intervalo sin su identificador único.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			this.tiempoJDBC.setConnection(con);
			this.tiempoJDBC.borrarIntervalo(intervalo);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.TiempoDAO#buscarIntervaloTiempo(java.lang.Long)
	 */
	@Override
	public Time buscarIntervaloTiempo(Long idIntervalo) throws SQLException {
		if (idIntervalo == null)
			throw new IllegalArgumentException(
					"No se puede buscar el intervalo sin su identificador único.");

		Connection con = DBConnection.getConnection();
		this.tiempoJDBC.setConnection(con);
		Time t = this.tiempoJDBC.buscarIntervaloTiempo(idIntervalo);
		con.close();
		return t;
	}

	@Override
	public Time buscarIntervaloTiempo(Date startDate, Date endDate)	throws SQLException {
		if(startDate == null)
			throw new IllegalArgumentException("Al menos debe incluirse la fecha de inicio.");
		
		Connection con = DBConnection.getConnection();
		this.tiempoJDBC.setConnection(con);
		Time t = this.tiempoJDBC.buscarIntervaloTiempo(startDate, endDate);
		con.close();
		return t;
	}

}
