package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.User;

public interface UsuariosDAO {

	/**
	 * Registra a un nuevo usuario en el sistema. Se encargará de almacenar sus
	 * datos de inicio de sesión y sus datos personales.
	 * 
	 * @param usuario
	 *            - Nuevo usuario a guardar.
	 * @return - Usuario guardado, con su nuevo identificador único añadido.
	 * @throws SQLException
	 */
	public User crearUsuario(User usuario) throws SQLException;

	/**
	 * Método transaccional que elimina los datos de inicio de sesión y los
	 * datos personales de un usuario determinado.
	 * 
	 * @param usuario
	 *            - Usuario a eliminar.
	 * @throws SQLException
	 */
	public void eliminarUsuario(User usuario) throws SQLException;

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
			throws SQLException;

	/**
	 * Método que actualiza los datos (de inicio de sesión y personales) de un
	 * usuario en la base de datos.
	 * 
	 * @param usuario
	 *            - Usuario a actualizar.
	 * @throws SQLException
	 */
	public void actualizarUsuario(User usuario) throws SQLException;

	/**
	 * Genera un listado con todos los usuarios registrados en el sistema, sin
	 * incluir a que organización pertenecen.
	 * 
	 * @return listado de usuarios.
	 * @throws SQLException
	 */
	public List<User> listarUsuarios() throws SQLException;

}