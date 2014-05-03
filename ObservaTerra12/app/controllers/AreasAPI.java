package controllers;

import model.Area;
import persistencia.AreasDAO;
import persistencia.PersistenceFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class AreasAPI extends Controller {

	/**
	 * Busca un area en base a su nombre. Sigue la ruta /api/area/name/*
	 * 
	 * @param areaName
	 *            - Nombre del area introducido.
	 * @return Area encontrada
	 */
	public static Result getAreaNameJSON(String areaName) {
		Area areaEncontrada = null;
		try {
			// Búsqueda del area
			AreasDAO areaDAO = PersistenceFactory.createAreasDAO();
			areaEncontrada = areaDAO.leerArea(areaName);

			// Si no se ha encontrado, devolver 404
			if (areaEncontrada == null)
				return notFound("No se ha encontrado el area pedida.");

			areaEncontrada = areaDAO.leerSubAreas(areaEncontrada);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(areaEncontrada));
	}

	/**
	 * Busca un area en base a su identificador único. Responde a la ruta
	 * /api/area/id/*
	 * 
	 * @param idArea
	 *            - Identificador del area
	 * @return resultado de la búsqueda.
	 */
	public static Result getAreaIdJSON(Long idArea) {
		Area areaEncontrada = null;
		try {
			// Búsqueda del area
			AreasDAO areaDAO = PersistenceFactory.createAreasDAO();
			areaEncontrada = areaDAO.buscarArea(idArea);

			// Si no se ha encontrado, devolver 404
			if (areaEncontrada == null)
				return notFound("No se ha encontrado el area pedida.");

			areaEncontrada = areaDAO.leerSubAreas(areaEncontrada);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(areaEncontrada));
	}

	/**
	 * Busca un pais por nombre, devolviendo el resultado en forma JSON.
	 * Responde a la uri /api/country/name/*
	 * 
	 * @param countryName
	 *            - Nombre del país
	 * @return - resultado de la búsqueda.
	 */
	public static Result getCountryNameJSON(String countryName) {
		Area areaEncontrada = null;
		try {
			// Búsqueda del area
			AreasDAO areaDAO = PersistenceFactory.createAreasDAO();
			areaEncontrada = areaDAO.leerPais(countryName);

			// Si no se ha encontrado, devolver 404
			if (areaEncontrada == null)
				return notFound("No se ha encontrado el país pedido.");

			areaEncontrada = areaDAO.leerSubAreas(areaEncontrada);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(areaEncontrada));
	}

	/**
	 * Busca un pais por su identificador, devolviendo el resultado en forma
	 * JSON. Responde a la uri /api/country/id/*
	 * 
	 * @param idPais
	 *            - Identificador único del país.
	 * @return - resultado de la búsqueda.
	 */
	public static Result getCountryIdJSON(Long idPais) {
		Area areaEncontrada = null;
		try {
			// Búsqueda del area
			AreasDAO areaDAO = PersistenceFactory.createAreasDAO();
			areaEncontrada = areaDAO.leerPais(idPais);

			// Si no se ha encontrado, devolver 404
			if (areaEncontrada == null)
				return notFound("No se ha encontrado el país pedido.");

			areaEncontrada = areaDAO.leerSubAreas(areaEncontrada);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(areaEncontrada));
	}

}
