package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;

import model.Measure;
import persistencia.MedidasDAO;
import persistencia.implJdbc.MedidasJdbc;
import utils.DBConnection;

public class MedidasJdbcDAO implements MedidasDAO {

	private MedidasJdbc medidasJDBC;

	public MedidasJdbcDAO() {
		this.medidasJDBC = new MedidasJdbc();
	}

	public MedidasJdbc getMedidasJDBC() {
		return medidasJDBC;
	}

	public void setMedidasJDBC(MedidasJdbc medidasJDBC) {
		this.medidasJDBC = medidasJDBC;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.MedidasDAO#crearMedida(model.Measure)
	 */
	@Override
	public Measure crearMedida(Measure measure) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.medidasJDBC.setConnection(con);
		Measure m = this.medidasJDBC.crearMedida(measure);
		con.close();
		return m;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.MedidasDAO#borrarMedida(model.Measure)
	 */
	@Override
	public void borrarMedida(Measure medida) throws SQLException {
		Connection con = DBConnection.getConnection();
		this.medidasJDBC.setConnection(con);
		this.medidasJDBC.borrarMedida(medida);
		con.close();
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.MedidasDAO#buscarMedidaPorIdentificador(java.lang.Long)
	 */
	@Override
	public Measure buscarMedidaPorIdentificador(Long identificador)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.medidasJDBC.setConnection(con);
		Measure m = this.medidasJDBC
				.buscarMedidaPorIdentificador(identificador);
		con.close();
		return m;
	}
}
