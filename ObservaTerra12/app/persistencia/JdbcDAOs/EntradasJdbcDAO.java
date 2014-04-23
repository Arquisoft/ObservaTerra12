package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;

import model.Submission;
import persistencia.EntradasDAO;
import persistencia.implJdbc.EntradasJdbc;
import utils.DBConnection;

public class EntradasJdbcDAO implements EntradasDAO {

	private EntradasJdbc entradasJDBC;

	public EntradasJdbcDAO() {
		this.entradasJDBC = new EntradasJdbc();
	}

	public EntradasJdbc getEntradasJDBC() {
		return entradasJDBC;
	}

	public void setEntradasJDBC(EntradasJdbc entradasJDBC) {
		this.entradasJDBC = entradasJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.EntradasDAO#crearEntrada(model.Submission)
	 */
	@Override
	public Submission crearEntrada(Submission entrada) throws SQLException {
		if (entrada == null)
			throw new IllegalArgumentException("Parámetro malformado");

		Connection con = DBConnection.getConnection();

		Submission registro;
		try {
			con.setAutoCommit(false);
			this.entradasJDBC.setConnection(con);
			registro = this.entradasJDBC.crearEntrada(entrada);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException("Incapaz de completar la transacción",
					e.getMessage());
		} finally {
			con.close();
		}

		return registro;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.EntradasDAO#eliminarEntrada(model.Submission)
	 */
	@Override
	public void eliminarEntrada(Submission entrada) throws SQLException {
		if (entrada == null)
			throw new IllegalArgumentException("Parámetro malformado");
		else if (entrada.getIdSubmission() == null)
			throw new IllegalArgumentException(
					"La entrada no tiene identificador único");

		Connection con = DBConnection.getConnection();
		this.entradasJDBC.setConnection(con);
		this.entradasJDBC.eliminarEntrada(entrada);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.EntradasDAO#leerEntrada(java.lang.Long)
	 */
	@Override
	public Submission leerEntrada(Long idEntrada) throws SQLException {
		if (idEntrada == null)
			throw new IllegalArgumentException(
					"La entrada no tiene identificador único");

		Connection con = DBConnection.getConnection();
		this.entradasJDBC.setConnection(con);
		Submission entrada = this.entradasJDBC.leerEntrada(idEntrada);
		con.close();
		return entrada;
	}

}
