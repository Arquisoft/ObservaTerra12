package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.User;
import persistencia.UsuariosDAO;
import persistencia.implJdbc.UsuariosJdbc;
import utils.DBConnection;

public class UsuariosJdbcDAO implements UsuariosDAO {

	private UsuariosJdbc usuariosJDBC;

	public UsuariosJdbcDAO() {
		this.usuariosJDBC = new UsuariosJdbc();
	}

	public UsuariosJdbc getUsuariosJDBC() {
		return usuariosJDBC;
	}

	public void setUsuariosJDBC(UsuariosJdbc usuariosJDBC) {
		this.usuariosJDBC = usuariosJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.UsuariosDAO#crearUsuario(model.User)
	 */
	@Override
	public User crearUsuario(User usuario) throws SQLException {

		if (usuario == null)
			throw new IllegalArgumentException(
					"No se ha indicado el usuario a crear.");

		Connection con = DBConnection.getConnection();
		this.usuariosJDBC.setConnection(con);
		User us = this.usuariosJDBC.crearUsuario(usuario);
		con.close();
		return us;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.UsuariosDAO#eliminarUsuario(model.User)
	 */
	@Override
	public void eliminarUsuario(User usuario) throws SQLException {

		if (usuario == null)
			throw new IllegalArgumentException(
					"No se ha indicado el usuario a crear.");
		else if (usuario.getIdUser() == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador del usuario.");

		Connection con = DBConnection.getConnection();
		this.usuariosJDBC.setConnection(con);
		this.usuariosJDBC.eliminarUsuario(usuario);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.UsuariosDAO#leerUsuario(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User leerUsuario(String nombreUsuario, String claveUsuario)
			throws SQLException {
		if (nombreUsuario == null || nombreUsuario.isEmpty())
			throw new IllegalArgumentException("Nombre de usuario malformado.");
		else if (claveUsuario == null || claveUsuario.isEmpty())
			throw new IllegalArgumentException("Clave malformada.");

		Connection con = DBConnection.getConnection();
		this.usuariosJDBC.setConnection(con);
		User us = this.usuariosJDBC.leerUsuario(nombreUsuario, claveUsuario);
		con.close();
		return us;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.UsuariosDAO#actualizarUsuario(model.User)
	 */
	@Override
	public void actualizarUsuario(User usuario) throws SQLException {
		if (usuario == null)
			throw new IllegalArgumentException(
					"No se ha indicado el usuario a actualizar.");
		else if (usuario.getIdUser() == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador del usuario.");

		Connection con = DBConnection.getConnection();
		this.usuariosJDBC.setConnection(con);
		this.usuariosJDBC.actualizarUsuario(usuario);
		con.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.UsuariosDAO#listarUsuarios()
	 */
	@Override
	public List<User> listarUsuarios() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.usuariosJDBC.setConnection(con);
		List<User> us = this.usuariosJDBC.listarUsuarios();
		con.close();
		return us;
	}
}
