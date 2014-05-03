package controllers;

import java.util.List;

import model.Indicator;
import persistencia.IndicadoresDAO;
import persistencia.PersistenceFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class IndicadoresAPI extends Controller {

	/**
	 * Busca un indicador en la base de datos en función
	 * a su identificador único.
	 * Devuelve la respuesta en forma JSON.
	 * Responde a la ruta /api/indicator/id/*
	 * @param idIndicador - Identificador del indicador
	 * @return Indicador encontrado
	 */
	public static Result getIndicatorIdJSON(Long idIndicador) {
		Indicator indicadorEncontrado = null;
		try {
			// Búsqueda del indicador
			IndicadoresDAO indicadorDAO = PersistenceFactory
					.createIndicadoresDAO();
			indicadorEncontrado = indicadorDAO.leerIndicador(idIndicador);

			// Si no se ha encontrado, devolver 404
			if (indicadorEncontrado == null)
				return notFound("No se ha encontrado el indicador pedido.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(indicadorEncontrado));
	}
	
	/**
	 * Busca un indicador en la base de datos en función
	 * de su nombre.
	 * Devuelve la respuesta en forma JSON.
	 * Responde a la ruta /api/indicator/name/*
	 * @param nombreIndicador - Nombre del indicador
	 * @return - Indicador encontrado.
	 */
	public static Result getIndicatorNameJSON(String nombreIndicador) {
		Indicator indicadorEncontrado = null;
		try {
			// Búsqueda del indicador
			IndicadoresDAO indicadorDAO = PersistenceFactory.createIndicadoresDAO();
			indicadorEncontrado = indicadorDAO.leerIndicador(nombreIndicador);

			// Si no se ha encontrado, devolver 404
			if (indicadorEncontrado == null)
				return notFound("No se ha encontrado el indicador pedido.");
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(indicadorEncontrado));
	}
	
	/**
	 * Genera un listado con todos los indicadores registrados
	 * en la base de datos en formato JSON.
	 * Responde a la ruta /api/indicator/all
	 * @return Listado de indicadores
	 */
	public static Result getIndicatorsListJSON() {
		List<Indicator> listadoIndicadores = null;
		try {
			// Búsqueda del indicador
			IndicadoresDAO indicadorDAO = PersistenceFactory.createIndicadoresDAO();
			listadoIndicadores = indicadorDAO.listarTodosLosIndicadores();
		} catch (Exception e) {
			// Error en la base de datos
			return internalServerError("No se pudo completar la respuesta.");
		}

		return ok(Json.toJson(listadoIndicadores));
	}
	

}
