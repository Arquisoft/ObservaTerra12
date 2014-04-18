package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.Provider;

public interface OrganizacionesDAO {
	
	//Leer proveedor
	public Provider leerProvedor(Long idProveedor);
	//Registra a una organizacion como proveedor de datos del sistema
	public Provider crearProveedor(Provider proveedor);	
	//Genera un listado con todos los proveedores del sistema
	public List<Provider> listarProveedores();

	/**
	 * Registra una nueva organización en la base de datos.
	 * 
	 * @param organizacion
	 *            - Nueva organización a crear.
	 * @return - Objeto organización con su nuevo identificador único.
	 * @throws SQLException
	 */
	public  Organization crearOrganizacion(Organization organizacion)
			throws SQLException;

	/**
	 * Recupera una organización de la base de datos en base a su identificador
	 * único.
	 * 
	 * @param idOrganizacion
	 *            - Identificador de la organización a buscar.
	 * @return - Organización encontrada.
	 * @throws SQLException
	 */
	public  Organization leerOrganizacion(Long idOrganizacion)
			throws SQLException;

	/**
	 * Borra una organización determinada del sistema solo si no hay usuarios
	 * registrados que pertenezcan a ella.
	 * 
	 * @param organizacion
	 *            - Organización a borrar.
	 * @throws SQLException
	 */
	public  void borrarOrganizacion(Organization organizacion)
			throws SQLException;

	/**
	 * Actualiza los datos de una organización determinada.
	 * 
	 * @param organizacion
	 *            - Organización a actualizar.
	 * @throws SQLException
	 */
	public  void actualizarOrganizacion(Organization organizacion)
			throws SQLException;

	/**
	 * Genera un listado con todas las organizaciones registradas en el sistema.
	 * 
	 * @return - Listado de organizaciones registradas.
	 * @throws SQLException
	 */
	public  List<Organization> listarOrganizaciones()
			throws SQLException;

}