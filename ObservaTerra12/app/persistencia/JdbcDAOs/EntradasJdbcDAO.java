package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;

import model.Submission;
import persistencia.EntradasDAO;
import persistencia.implJdbc.EntradasJdbc;
import persistencia.implJdbc.UsuariosJdbc;
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
		try {
			con.setAutoCommit(false);
			this.entradasJDBC.setConnection(con);
			this.entradasJDBC.eliminarEntrada(entrada);
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
	 * @see persistencia.JdbcDAOs.EntradasDAO#leerEntrada(java.lang.Long)
	 */
	@Override
	public Submission leerEntrada(Long idEntrada) throws SQLException {
		if (idEntrada == null)
			throw new IllegalArgumentException(
					"La entrada no tiene identificador único");

		Connection con = DBConnection.getConnection();
		this.entradasJDBC.setConnection(con);
		//Leer la entrada
		Submission entrada = this.entradasJDBC.leerEntrada(idEntrada);

		// Leer el usuario si lo trae
		if (entrada != null && entrada.getUser().getIdUser() != null) {
			UsuariosJdbc usuariosJDBC = new UsuariosJdbc();
			usuariosJDBC.setConnection(con);
			entrada.setUser(usuariosJDBC.leerUsuario(entrada.getUser()
					.getIdUser()));
		} else if(entrada != null) {
			entrada.setUser(null);
		}
		
		//Cerrar la conexión
		con.close();

		return entrada;
	}

}
