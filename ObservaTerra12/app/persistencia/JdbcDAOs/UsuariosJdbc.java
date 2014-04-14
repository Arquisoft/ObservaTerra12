package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Organization;
import model.User;

public class UsuariosJdbc {

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
	 * Registra a un nuevo usuario en el sistema. Se encargará de almacenar sus
	 * datos de inicio de sesión y sus datos personales.
	 * 
	 * @param usuario
	 *            - Nuevo usuario a guardar.
	 * @return - Usuario guardado, con su nuevo identificador único añadido.
	 * @throws SQLException
	 */
	public User crearUsuario(User usuario) throws SQLException {
		// Intentar ejecutar la transaccion
		try {
			con.setAutoCommit(false);
			// Computa el siguiente id de usuario
			Long id = leerSiguienteIdUsuario();
			usuario.setIdUsuario(id);
			// Inserta los datos de inicio de sesión
			insertarUsuario(usuario);
			// Inserta los datos de personales
			insertarDatosPersonales(usuario);
			// Ejecuta la transaccion
			con.commit();
			return usuario;
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/**
	 * Guarda los datos personales de un usuario, creándolos desde cero.
	 * 
	 * @param usuario
	 *            - Nuevo usuario.
	 * @throws SQLException
	 */
	private void insertarDatosPersonales(User usuario) throws SQLException {
		String SQL = "INSERT INTO DATOSPERSONALES (ID_USUARIO,NOMBRE,APELLIDOS,EMAIL,ROL) VALUES (?,?,?,?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUsuario());
		pst.setString(2, usuario.getName());
		pst.setString(3, usuario.getSurname());
		pst.setString(4, usuario.getEmail());
		pst.setString(5, usuario.getRol().toUpperCase());

		pst.executeUpdate();
	}

	/**
	 * Calcula el siguiente identificador único del usuario. Atención, este
	 * método está pensado para ejecutarse dentro de una transacción abierta.
	 * 
	 * @return - Siguiente identificador del usuario.
	 * @throws SQLException
	 */
	private Long leerSiguienteIdUsuario() throws SQLException {
		String SQL = "SELECT max(id_usuario) as maximo FROM usuarios";

		Long maxID = null;

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			maxID = rs.getLong("maximo");
		}

		return maxID + 1;
	}

	/**
	 * Guarda los datos de inicio de sesión de un usuario, que va a ser creado
	 * desde cero. ATENCIÓN: Este método está pensado para ejecutarse dentro de
	 * una transacción abierta.
	 * 
	 * @param usuario
	 *            - Nuevo usuario.
	 * @throws SQLException
	 */
	private void insertarUsuario(User usuario) throws SQLException {
		String SQL = "INSERT INTO USUARIOS (NOMBRE_USUARIO,CLAVE,ID_ORGANIZACION) VALUES (?,?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, usuario.getUserName());
		pst.setString(2, usuario.getPassword());
		pst.setLong(3, usuario.getOrganization().getIdOrganizacion());

		pst.executeUpdate();
	}

	/**
	 * Método transaccional que elimina los datos de inicio de sesión y los
	 * datos personales de un usuario determinado.
	 * 
	 * @param usuario
	 *            - Usuario a eliminar.
	 * @throws SQLException
	 */
	public void eliminarUsuario(User usuario) throws SQLException {
		String SQL1 = "DELETE FROM datospersonales WHERE id_usuario = ?";
		String SQL2 = "DELETE FROM usuarios WHERE id_usuario = ?";

		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		try {
			con.setAutoCommit(false);
			pst = con.prepareStatement(SQL1);
			pst.setLong(1, usuario.getIdUsuario());
			pst.executeUpdate();
			pst2 = con.prepareStatement(SQL2);
			pst2.setLong(1, usuario.getIdUsuario());
			pst2.executeUpdate();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			pst.close();
			pst2.close();
			con.close();
		}
	}

	/**
	 * Método que busca y recupera a un usuario en la base de datos en base a
	 * sus datos de inicio de sesión.
	 * 
	 * @param nombreUsuario
	 *            - Nombre de usuario.
	 * @param claveUsuario
	 *            - Contraseña del usuario.
	 * @return - Usuario recuperado / null si no existe.
	 * @throws SQLException
	 */
	public User leerUsuario(String nombreUsuario, String claveUsuario)
			throws SQLException {
		String SQL = "SELECT * FROM USUARIOS natural join DATOSPERSONALES natural join "
				+ "ORGANIZACION WHERE nombre_usuario = ? and clave = ? ";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		User usuario = null;

		while (rs.next()) {
			// Organización
			Organization org = new Organization();
			org.setIdOrganizacion(rs.getLong("id_organizacion"));
			org.setNombre(rs.getString("nombre_organizacion"));
			org.setTipoOrganizacion(rs.getString("tipo"));

			// Usuario
			usuario = new User();
			usuario.setIdUsuario(rs.getLong("id_usuario"));
			usuario.setUserName(nombreUsuario);
			usuario.setPassword(claveUsuario);
			usuario.setName(rs.getString("nombre"));
			usuario.setSurname(rs.getString("apellidos"));
			usuario.setEmail(rs.getString("email"));
			usuario.setRol(rs.getString("rol"));
			usuario.setOrganization(org);
		}

		rs.close();
		pst.close();
		con.close();

		return usuario;
	}

	/**
	 * Método que actualiza los datos (de inicio de sesión y personales) de un
	 * usuario en la base de datos.
	 * 
	 * @param usuario
	 *            - Usuario a actualizar.
	 * @throws SQLException
	 */
	public void actualizarUsuario(User usuario) throws SQLException {
		// Intentar ejecutar la transaccion
		try {
			con.setAutoCommit(false);
			actualizarDatosUsuario(usuario);
			actualizarDatosPersonales(usuario);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/**
	 * Método auxiliar que actualiza los datos personales de un usuario en base
	 * a su identificador único. ATENCIÓN: Este método deberá ejecutarse dentro
	 * de una transacción ya abierta.
	 * 
	 * @param usuario
	 *            - Usuario a actualizar.
	 * @throws SQLException
	 */
	private void actualizarDatosPersonales(User usuario) throws SQLException {
		String SQL = "UPDATE datospersonales SET nombre = ? and apellidos = ? and email = ? and rol = ? WHERE id_usuario = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, usuario.getName());
		pst.setString(2, usuario.getSurname());
		pst.setString(3, usuario.getEmail());
		pst.setString(4, usuario.getRol());
		pst.setLong(5, usuario.getIdUsuario());

		pst.executeUpdate();

		pst.close();
		con.close();
	}

	/**
	 * Método auxiliar que actualiza los datos de inicio de sesión de un usuario
	 * en base a su identificador único. ATENCIÓN: Este método deberá ejecutarse
	 * dentro de una transacción ya abierta.
	 * 
	 * @param usuario - Usuario a actualizar.
	 * @throws SQLException
	 */
	private void actualizarDatosUsuario(User usuario) throws SQLException {
		String SQL = "UPDATE usuarios SET nombre_usuario = ? and clave = ? and id_organizacion = ? WHERE id_usuario = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, usuario.getUserName());
		pst.setString(2, usuario.getPassword());
		pst.setLong(3, usuario.getOrganization().getIdOrganizacion());
		pst.setLong(4, usuario.getIdUsuario());

		pst.executeUpdate();

		pst.close();
		con.close();
	}

}
