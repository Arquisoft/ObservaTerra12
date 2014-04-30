package persistencia;

import java.sql.SQLException;
import java.util.List;

import model.Organization;
import model.Provider;

public interface OrganizacionesDAO {

	/**
	 * Recoge un proveedor del sistema en base a su identificador único.
	 * 
	 * @param idProveedor
	 *            - Identificador único del sistema.
	 * @return Proveedor encontrado
	 * @throws SQLException 
	 */
	public Provider leerProvedor(Long idProveedor) throws SQLException;
	
	
	/**
	 * Recoge un proveedor del sistema en base a su nombre
	 * 
	 * @param nombreProveedor - Nombre del proveedor.
	 * @return Proveedor encontrado
	 * @throws SQLException 
	 */
	public Provider leerProvedor(String nombreProveedor) throws SQLException;

	/**
	 * Registra una organización como proveedor de datos para el sistema.
	 * 
	 * @param proveedor
	 *            - Nuevo proveedor
	 * @return Objeto proveedor del sistema.
	 * @throws SQLException 
	 */
	public Provider crearProveedor(Provider proveedor) throws SQLException;

	/**
	 * Genera un listado con todos los proveedores del sistema.
	 * 
	 * @return listado de proveedores del sistema.
	 * @throws SQLException 
	 */
	public List<Provider> listarProveedores() throws SQLException;

	/**
	 * Registra una nueva organización en la base de datos.
	 * 
	 * @param organizacion
	 *            - Nueva organización a crear.
	 * @return - Objeto organización con su nuevo identificador único.
	 * @throws SQLException
	 */
	public Organization crearOrganizacion(Organization organizacion)
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
	public Organization leerOrganizacion(Long idOrganizacion)
			throws SQLException;

	/**
	 * Borra una organización determinada del sistema solo si no hay usuarios
	 * registrados que pertenezcan a ella.
	 * 
	 * @param organizacion
	 *            - Organización a borrar.
	 * @throws SQLException
	 */
	public void borrarOrganizacion(Organization organizacion)
			throws SQLException;

	/**
	 * Actualiza los datos de una organización determinada.
	 * 
	 * @param organizacion
	 *            - Organización a actualizar.
	 * @throws SQLException
	 */
	public void actualizarOrganizacion(Organization organizacion)
			throws SQLException;

	/**
	 * Genera un listado con todas las organizaciones registradas en el sistema.
	 * 
	 * @return - Listado de organizaciones registradas.
	 * @throws SQLException
	 */
	public List<Organization> listarOrganizaciones() throws SQLException;

	/**
	 * Recupera una organización de la base de datos en base a su nombre.
	 * 
	 * @param nombreOrganizacion
	 *            - Nombre de la organización a buscar.
	 * @return - Organización encontrada.
	 * @throws SQLException
	 */
	public Organization buscarOrganizacionPorNombre(String nombreOrganizacion);
}