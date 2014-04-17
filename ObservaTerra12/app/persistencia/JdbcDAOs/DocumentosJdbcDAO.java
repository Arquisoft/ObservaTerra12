package persistencia.JdbcDAOs;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.User;
import persistencia.DocumentosDAO;
import persistencia.implJdbc.DocumentosJdbc;
import utils.DBConnection;

public class DocumentosJdbcDAO implements DocumentosDAO {

	private DocumentosJdbc documentosJDBC;

	public DocumentosJdbcDAO() {
		this.documentosJDBC = new DocumentosJdbc();
	}

	public DocumentosJdbc getDocumentosJDBC() {
		return documentosJDBC;
	}

	public void setDocumentosJDBC(DocumentosJdbc documentosJDBC) {
		this.documentosJDBC = documentosJDBC;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#guardarDocumento(model.User, java.io.File)
	 */
	@Override
	public Long guardarDocumento(User usuario, File file) throws SQLException,
			IOException {

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		Long a = this.documentosJDBC.guardarDocumento(usuario, file);
		con.close();
		return a;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#leerDocumento(long)
	 */
	@Override
	public File leerDocumento(long idDocumento) throws SQLException,
			IOException {

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		File a = this.documentosJDBC.leerDocumento(idDocumento);
		con.close();
		return a;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#borrarDocumento(model.User, java.lang.Long)
	 */
	@Override
	public void borrarDocumento(User user, Long idDocumento)
			throws SQLException {

		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		this.documentosJDBC.borrarDocumento(user, idDocumento);
		con.close();
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#listarRepositoriosUsuario(model.User)
	 */
	@Override
	public List<Long> listarRepositoriosUsuario(User usuario)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		List<Long> a = this.documentosJDBC.listarRepositoriosUsuario(usuario);
		con.close();
		return a;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#listarRespositoriosAccesiblesUsuario(model.User)
	 */
	@Override
	public List<Long> listarRespositoriosAccesiblesUsuario(User usuario)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		List<Long> a = this.documentosJDBC
				.listarRespositoriosAccesiblesUsuario(usuario);
		con.close();
		return a;
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#compartirRepositorioConUsuario(java.lang.Long, model.User, model.User)
	 */
	@Override
	public void compartirRepositorioConUsuario(Long idRepositorio,
			User usuarioACompartir, User usuarioPropietario)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		this.documentosJDBC.compartirRepositorioConUsuario(idRepositorio,
				usuarioACompartir, usuarioPropietario);
		con.close();
	}

	/* (non-Javadoc)
	 * @see persistencia.JdbcDAOs.DocumentosDAO#anularCompartirRepositorioConUsuario(java.lang.Long, model.User, model.User)
	 */
	@Override
	public void anularCompartirRepositorioConUsuario(Long idRepositorio,
			User usuarioACompartir, User usuarioPropietario)
			throws SQLException {
		Connection con = DBConnection.getConnection();
		this.documentosJDBC.setConnection(con);
		this.documentosJDBC.anularCompartirRepositorioConUsuario(idRepositorio,
				usuarioACompartir, usuarioPropietario);
		con.close();
	}

}
