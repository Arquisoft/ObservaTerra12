package controllers;

import java.util.List;

import model.Organization;
import model.Provider;
import persistencia.OrganizacionesDAO;
import persistencia.PersistenceFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class OrganizacionesAPI extends Controller {

	/**
	 * Busca una organización en base a su identificador único. Devuelve la
	 * respuesta en forma JSON. Responde a la URI /api/organization/id/*
	 * 
	 * @param idOrganization
	 *            - Identificador único.
	 * @return Organización encontrada.
	 */
	public static Result getOrganizationIdJSON(Long idOrganization) {
		Organization org = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory
					.createOrganizacionesDAO();
			org = orgDAO.leerOrganizacion(idOrganization);

			// Si no se ha encontrado, devolver 404
			if (org == null)
				return notFound("No se ha encontrado la organizacion.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(org));
	}

	/**
	 * Busca una organización en base a su nombre. Devuelve la
	 * respuesta en forma JSON. Responde a la URI /api/organization/name/*
	 * 
	 * @param organizationName - Nombre de la organización
	 * @return Organización encontrada.
	 */
	public static Result getOrganizationNameJSON(String organizationName)
	{
		Organization org = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory.createOrganizacionesDAO();
			org = orgDAO.buscarOrganizacionPorNombre(organizationName);

			// Si no se ha encontrado, devolver 404
			if (org == null)
				return notFound("No se ha encontrado la organizacion.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(org));
	}
	
	/**
	 * Genera un listado con todas las organizaciones registradas en el sistema
	 * en forma JSON. Responde a la URI /api/organization/all
	 * 
	 * @return Listado de organizaciones.
	 */
	public static Result getOrganizationListJSON() {
		List<Organization> orgList = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory
					.createOrganizacionesDAO();
			orgList = orgDAO.listarOrganizaciones();
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(orgList));
	}

	/**
	 * Busca un proveedor según su identificador único. Devuelve el resultado en
	 * JSON Responde a la ruta /api/provider/id/*
	 * 
	 * @param idProveedor
	 *            - Identificador del proveedor
	 * @return Proveedor encontrado
	 */
	public static Result getProviderIdJSON(Long idProveedor) {
		Provider org = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory
					.createOrganizacionesDAO();
			org = orgDAO.leerProvedor(idProveedor);

			// Si no se ha encontrado, devolver 404
			if (org == null)
				return notFound("No se ha encontrado el proveedor.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(org));
	}

	/**
	 * Busca un proveedor en base a su nombre. Responde a la ruta
	 * /api/provider/name/*
	 * 
	 * @param nombreProveedor
	 *            - Nombre proveedor
	 * @return Proveedor encontrado.
	 */
	public static Result getProviderNameJSON(String nombreProveedor) {
		Provider org = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory
					.createOrganizacionesDAO();
			org = orgDAO.leerProvedor(nombreProveedor);

			// Si no se ha encontrado, devolver 404
			if (org == null)
				return notFound("No se ha encontrado el proveedor.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(org));
	}

	/**
	 * Genera un listado con los proveedores registrados en el sistema. Responde
	 * a la ruta /api/provider/all
	 * 
	 * @return Listado de proveedores.
	 */
	public static Result getProviderListJSON() {
		List<Provider> listado = null;
		try {
			// Búsqueda del indicador
			OrganizacionesDAO orgDAO = PersistenceFactory
					.createOrganizacionesDAO();
			listado = orgDAO.listarProveedores();

		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(listado));
	}
}
