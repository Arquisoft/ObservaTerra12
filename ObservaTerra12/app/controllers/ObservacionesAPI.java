package controllers;

import java.util.List;

import model.Area;
import model.Observation;
import persistencia.AreasDAO;
import persistencia.ObservacionesDAO;
import persistencia.PersistenceFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ObservacionesAPI extends Controller {

	/**
	 * Realiza una consulta a la base de datos y devuelve un listado de
	 * observaciones en formato JSON. Responde a la ruta /api/observations/all
	 * 
	 * @return Listado de observaciones
	 */
	public static Result getObservationsListJSON() {
		List<Observation> listadoObservaciones = null;
		try {
			// Búsqueda del indicador
			ObservacionesDAO obvDAO = PersistenceFactory
					.createObservacionesDAO();
			listadoObservaciones = obvDAO.listarTodasObservaciones();
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(listadoObservaciones));
	}

	/**
	 * Busca una observación en base a su identificador único. Devuelve en
	 * formato JSON. Responde a la ruta ruta /api/observation/id/*
	 * 
	 * @param idObservacion
	 * @return
	 */
	public static Result getObservationsIdJSON(Long idObservacion) {
		Observation observacion = null;
		try {
			// Búsqueda del indicador
			ObservacionesDAO obvDAO = PersistenceFactory
					.createObservacionesDAO();
			observacion = obvDAO
					.buscarObservacionPorIdentificador(idObservacion);

			if (observacion == null)
				return notFound();
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(observacion));
	}

	/**
	 * Busca todas las observaciones de un area en base al nombre de la misma,
	 * devolviendo la lista en forma JSON. Responde a la ruta
	 * /api/observation/area/name/*
	 * 
	 * @param areaName
	 *            - Nombre del área.
	 * @return listado de áreas.
	 */
	public static Result getObservationsAreaNameJSON(String areaName) {
		List<Observation> observaciones = null;
		try {
			// Búsqueda del indicador
			ObservacionesDAO obvDAO = PersistenceFactory
					.createObservacionesDAO();
			AreasDAO areaDAO = PersistenceFactory.createAreasDAO();
			// Busca el área por nombre
			Area area = areaDAO.buscarAreaYPaisPorNombre(areaName);

			// Si no se encuentra devuelve un 404
			if (area == null)
				return notFound();

			// Lee las observaciones
			observaciones = obvDAO.leerObservacionesDeUnArea(area);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(observaciones));
	}

	/**
	 * Recupera la lista de observaciones registradas según el nombre del
	 * indicador que mide. Responde a la ruta /api/observation/indicator/name/*
	 * 
	 * @param indicator
	 *            - Indicador medido
	 * @return Listado de indicadores.
	 */
	public static Result getObservationsIndicatorNameJSON(String indicator) {
		List<Observation> observaciones = null;
		try {
			// Búsqueda del indicador
			ObservacionesDAO obvDAO = PersistenceFactory
					.createObservacionesDAO();
			// Lee las observaciones
			observaciones = obvDAO.leerObservacionesDeUnIndicador(indicator);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(observaciones));
	}

	/**
	 * Registra una nueva observacion de la base de datos. Responde a la ruta
	 * /api/observation/new
	 * 
	 * @return Observacion creada
	 */
	public static Result createObservationJSON() {

		Observation ob = null;
		try {
			ob = Json.fromJson(request().body().asJson(), Observation.class);
			// Bajar a la base de datos
			ObservacionesDAO obvDAO = PersistenceFactory
					.createObservacionesDAO();
			ob = obvDAO.insertarObservacion(ob);
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		if(ob == null)
			return noContent();
		
		// Devuelve la observacion creada
		return ok(Json.toJson(ob));
	}
}
