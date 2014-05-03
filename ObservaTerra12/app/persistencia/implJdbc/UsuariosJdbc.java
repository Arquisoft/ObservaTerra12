package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		// Computa el siguiente id de usuario
		Long id = leerSiguienteIdUsuario();
		usuario.setIdUser(id);
		// Inserta los datos de inicio de sesión
		insertarUsuario(usuario);
		// Inserta los datos de personales
		insertarDatosPersonales(usuario);
		// Ejecuta la transaccion
		con.commit();
		return usuario;

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
		pst.setLong(1, usuario.getIdUser());
		pst.setString(2, usuario.getName());
		pst.setString(3, usuario.getSurname());
		pst.setString(4, usuario.getEmail());
		pst.setString(5, usuario.getRol().toUpperCase());

		pst.executeUpdate();

		pst.close();
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

		rs.close();
		pst.close();

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
		String SQL = "INSERT INTO USUARIOS (ID_USUARIO, NOMBRE_USUARIO,CLAVE,ID_ORGANIZACION) VALUES (?,?,?,?)";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, usuario.getIdUser());
		pst.setString(2, usuario.getUserName());
		pst.setString(3, usuario.getPassword());

		// Un usuario puede no pertenecer a una organizacion
		if (usuario.getOrganization() != null)
			pst.setLong(4, usuario.getOrganization().getIdOrganization());
		else
			pst.setNull(4, java.sql.Types.INTEGER);

		pst.executeUpdate();

		pst.close();
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

		// aCTUALIZAR DATOS PERSONALES
		pst = con.prepareStatement(SQL1);
		pst.setLong(1, usuario.getIdUser());
		pst.executeUpdate();
		// aCTUALIZAR DATOS INICIO SESIÓN
		pst2 = con.prepareStatement(SQL2);
		pst2.setLong(1, usuario.getIdUser());
		pst2.executeUpdate();
		con.commit();

		pst.close();
		pst2.close();

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
		String SQL = "SELECT * FROM USUARIOS natural join DATOSPERSONALES WHERE nombre_usuario = ? and clave = ? ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreUsuario);
		pst.setString(2, claveUsuario);
		ResultSet rs = pst.executeQuery();

		User usuario = null;

		while (rs.next()) {
			usuario = new User();

			// Organización
			Organization org;
			Long idOrg = rs.getLong("id_organizacion");
			if (idOrg == null) {
				org = new Organization();
				org.setIdOrganization(rs.getLong("id_organizacion"));
				usuario.setOrganization(org);
			}

			// Usuario
			usuario = new User();
			usuario.setIdUser(rs.getLong("id_usuario"));
			usuario.setUserName(nombreUsuario);
			usuario.setPassword(claveUsuario);
			usuario.setName(rs.getString("nombre"));
			usuario.setSurname(rs.getString("apellidos"));
			usuario.setEmail(rs.getString("email"));
			usuario.setRol(rs.getString("rol"));
		}

		rs.close();
		pst.close();

		return usuario;
	}

	public User leerUsuario(Long idUsuario) throws SQLException {
		String SQL = "SELECT * FROM USUARIOS natural join DATOSPERSONALES WHERE id_usuario=? ";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, idUsuario);
		ResultSet rs = pst.executeQuery();

		User usuario = null;

		while (rs.next()) {
			usuario = new User();

			// Organización
			Organization org;
			Long idOrg = rs.getLong("id_organizacion");
			if (idOrg == null) {
				org = new Organization();
				org.setIdOrganization(rs.getLong("id_organizacion"));
				usuario.setOrganization(org);
			}

			// Usuario
			usuario = new User();
			usuario.setIdUser(rs.getLong("id_usuario"));
			usuario.setUserName(rs.getString("nombre_usuario"));
			usuario.setPassword(rs.getString("clave"));
			usuario.setName(rs.getString("nombre"));
			usuario.setSurname(rs.getString("apellidos"));
			usuario.setEmail(rs.getString("email"));
			usuario.setRol(rs.getString("rol"));
		}

		rs.close();
		pst.close();

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
		actualizarDatosUsuario(usuario);
		actualizarDatosPersonales(usuario);
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
		String SQL = "UPDATE datospersonales SET nombre = ?,apellidos = ?,email = ?,rol = ? WHERE id_usuario = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, usuario.getName());
		pst.setString(2, usuario.getSurname());
		pst.setString(3, usuario.getEmail());
		pst.setString(4, usuario.getRol());
		pst.setLong(5, usuario.getIdUser());

		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Método auxiliar que actualiza los datos de inicio de sesión de un usuario
	 * en base a su identificador único. ATENCIÓN: Este método deberá ejecutarse
	 * dentro de una transacción ya abierta.
	 * 
	 * @param usuario
	 *            - Usuario a actualizar.
	 * @throws SQLException
	 */
	private void actualizarDatosUsuario(User usuario) throws SQLException {
		String SQL = "UPDATE usuarios SET nombre_usuario=?,clave=?,id_organizacion=? WHERE id_usuario=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, usuario.getUserName());
		pst.setString(2, usuario.getPassword());
		pst.setLong(3, usuario.getOrganization().getIdOrganization());
		pst.setLong(4, usuario.getIdUser());

		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Genera un listado con todos los usuarios registrados en el sistema, sin
	 * incluir a que organización pertenecen.
	 * 
	 * @return listado de usuarios.
	 * @throws SQLException
	 */
	public List<User> listarUsuarios() throws SQLException {
		String SQL = "SELECT * FROM usuarios natural join datospersonales";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<User> usuarios = new ArrayList<User>();

		while (rs.next()) {
			User usuario = new User();

			usuario.setIdUser(rs.getLong("id_usuario"));
			usuario.setUserName(rs.getString("nombre_usuario"));
			usuario.setPassword(rs.getString("clave"));
			usuario.setName(rs.getString("nombre"));
			usuario.setSurname(rs.getString("apellidos"));
			usuario.setEmail(rs.getString("email"));
			usuario.setRol(rs.getString("rol"));

			usuarios.add(usuario);
		}

		rs.close();
		pst.close();

		return usuarios;
	}

	/**
	 * Busca un usuario en el sistema por su nombre de usuario.
	 * 
	 * @param userName
	 *            el nombre del usuario
	 * @return el usuario
	 * @throws SQLException
	 */
	public User leerUsuario(String nombreUsuario) throws SQLException {
		String SQL = "SELECT * FROM USUARIOS natural join DATOSPERSONALES WHERE nombre_usuario = ?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreUsuario);
		ResultSet rs = pst.executeQuery();

		User usuario = null;

		while (rs.next()) {
			usuario = new User();

			// Organización
			Organization org;
			Long idOrg = rs.getLong("id_organizacion");
			if (idOrg == null) {
				org = new Organization();
				org.setIdOrganization(rs.getLong("id_organizacion"));
				usuario.setOrganization(org);
			}

			// Usuario
			usuario.setIdUser(rs.getLong("id_usuario"));
			usuario.setUserName(nombreUsuario);
			usuario.setPassword(rs.getString("clave"));
			usuario.setName(rs.getString("nombre"));
			usuario.setSurname(rs.getString("apellidos"));
			usuario.setEmail(rs.getString("email"));
			usuario.setRol(rs.getString("rol"));
		}

		rs.close();
		pst.close();

		return usuario;
	}

}
