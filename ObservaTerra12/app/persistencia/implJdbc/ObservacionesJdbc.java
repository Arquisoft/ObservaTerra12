package persistencia.implJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Provider;
import model.Submission;
import model.Time;

public class ObservacionesJdbc {

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
	 * Inserta una nueva observación en la base de datos. Este método cuenta con
	 * que previamente ya se han almacenado las medidas, los tiempos, las areas,
	 * y solo guarda las referencias a las mismas.
	 * 
	 * @param observacion
	 *            - Observacion a insertar
	 * @return Observacion insertada, mas el identificador único.
	 * @throws SQLException
	 */
	public Observation insertarObservacion(Observation observacion)
			throws SQLException {
		String SQL = "INSERT INTO OBSERVACIONES "
				+ "(ID_OBSERVACION,ID_AREA,ID_INDICADOR,ID_MEDIDA,ID_TIEMPO,ID_PROVEEDOR,ID_ENTRADA) "
				+ "VALUES (?,?,?,?,?,?,?)";

		Long proximoID = leerProximoIdentificador();
		observacion.setIdObservation(proximoID);
		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, proximoID);
		pst.setLong(2, observacion.getArea().getIdArea());
		pst.setLong(3, observacion.getIndicator().getIdIndicator());
		pst.setLong(4, observacion.getMeasure().getIdMeasure());
		pst.setLong(5, observacion.getTime().getIdTime());
		pst.setLong(6, observacion.getProvider().getIdOrganization());
		pst.setLong(7, observacion.getSubmission().getIdSubmission());
		pst.executeUpdate();

		pst.close();

		return observacion;
	}

	/**
	 * Función auxiliar que recupera el próximo identificador único a asignar al
	 * item a guardar.
	 * 
	 * @return - Siguiente identificador único.
	 * @throws SQLException
	 */
	private Long leerProximoIdentificador() throws SQLException {
		String SQL = "SELECT max(id_observacion) as maximo FROM observaciones";
		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();
		Long resultado = null;
		while (rs.next()) {
			resultado = rs.getLong("maximo");
		}

		rs.close();
		pst.close();

		return resultado + 1;
	}

	/**
	 * Este método elimina una observación, contando con que después alguien
	 * eliminará las medidas, areas, etc que tenía asignadas.
	 * 
	 * @param observacion
	 *            - Observación a eliminar.
	 * @throws SQLException
	 */
	public void eliminarObservacion(Observation observacion)
			throws SQLException {
		String SQL = "DELETE FROM observaciones WHERE id_observacion=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, observacion.getIdObservation());
		pst.executeUpdate();

		pst.close();
	}

	/**
	 * Busca y recupera una observación en base a su identificador único.
	 * Recupera tambíen los identificadores únicos de los elementos que componen
	 * la observación, que deberán ser leídos uno a uno posteriormente.
	 * 
	 * @param identificador
	 *            - Identificador de la observación a buscar.
	 * @return Observación encontrada.
	 * @throws SQLException
	 */
	public Observation buscarObservacionPorIdentificador(Long identificador)
			throws SQLException {
		String SQL = "SELECT * FROM observaciones WHERE id_observacion=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, identificador);
		ResultSet rs = pst.executeQuery();
		Observation observacion = null;

		while (rs.next()) {
			observacion = new Observation();
			observacion.setIdObservation(rs.getLong("id_observacion"));

			// Creacion del indicador
			Indicator indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			observacion.setIndicator(indicador);

			// Creacion del area
			Area area = new Area();
			area.setIdArea(rs.getLong("id_area"));
			observacion.setArea(area);

			// Proveedor
			Provider org = new Provider();
			org.setIdOrganization(rs.getLong("id_proveedor"));
			observacion.setProvider(org);

			// Medida
			Measure medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			observacion.setMeasure(medida);

			// Tiempo
			Time tiempo = new Time();
			tiempo.setIdTime(rs.getLong("id_tiempo"));
			observacion.setTime(tiempo);
			
			//Entrada
			Submission entrada = new Submission();
			entrada.setIdSubmission(rs.getLong("id_entrada"));
			observacion.setSubmission(entrada);
		}

		rs.close();
		pst.close();

		return observacion;
	}

	/**
	 * Busca y recupera un listado de observaciones en base al área al que
	 * pertenecen. Recupera tambíen los identificadores únicos de los elementos
	 * que componen la observación, que deberán ser leídos uno a uno
	 * posteriormente.
	 * 
	 * @param area
	 *            - Area determinada.
	 * @return Listado de observaciones encontradas
	 * @throws SQLException
	 */
	public List<Observation> leerObservacionesDeUnArea(Area area)
			throws SQLException {
		String SQL = "SELECT * FROM observaciones WHERE id_area=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setLong(1, area.getIdArea());
		ResultSet rs = pst.executeQuery();

		List<Observation> observaciones = new ArrayList<Observation>();

		while (rs.next()) {
			Observation observacion = new Observation();
			observacion.setIdObservation(rs.getLong("id_observacion"));

			// Creacion del indicador
			Indicator indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			observacion.setIndicator(indicador);

			// Creacion del area
			observacion.setArea(area);

			// Proveedor
			Provider org = new Provider();
			org.setIdOrganization(rs.getLong("id_proveedor"));
			observacion.setProvider(org);

			// Medida
			Measure medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			observacion.setMeasure(medida);

			// Tiempo
			Time tiempo = new Time();
			tiempo.setIdTime(rs.getLong("id_tiempo"));
			observacion.setTime(tiempo);

			//Entrada
			Submission entrada = new Submission();
			entrada.setIdSubmission(rs.getLong("id_entrada"));
			observacion.setSubmission(entrada);
			
			observaciones.add(observacion);
		}

		rs.close();
		pst.close();

		return observaciones;
	}

	/**
	 * Busca y recupera un listado de observaciones en base al nombre del
	 * indicador que pretenden medir. Recupera tambíen los identificadores
	 * únicos de los elementos que componen la observación, que deberán ser
	 * leídos uno a uno posteriormente.
	 * 
	 * @param nombreIndicador
	 *            = Nombre del indicador deseado.
	 * @return Listado de observaciones encontradas.
	 * @throws SQLException
	 */
	public List<Observation> leerObservacionesDeUnIndicador(
			String nombreIndicador) throws SQLException {
		String SQL = "SELECT * FROM observaciones NATURAL JOIN indicadores WHERE nombre=?";

		PreparedStatement pst = con.prepareStatement(SQL);
		pst.setString(1, nombreIndicador);
		ResultSet rs = pst.executeQuery();

		List<Observation> observaciones = new ArrayList<Observation>();

		while (rs.next()) {
			Observation observacion = new Observation();
			observacion.setIdObservation(rs.getLong("id_observacion"));

			// Creacion del indicador
			Indicator indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			observacion.setIndicator(indicador);

			// Creacion del area
			Area area = new Area();
			area.setIdArea(rs.getLong("id_area"));
			observacion.setArea(area);

			// Proveedor
			Provider org = new Provider();
			org.setIdOrganization(rs.getLong("id_proveedor"));
			observacion.setProvider(org);

			// Medida
			Measure medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			observacion.setMeasure(medida);

			// Tiempo
			Time tiempo = new Time();
			tiempo.setIdTime(rs.getLong("id_tiempo"));
			observacion.setTime(tiempo);
			
			//Entrada
			Submission entrada = new Submission();
			entrada.setIdSubmission(rs.getLong("id_entrada"));
			observacion.setSubmission(entrada);

			observaciones.add(observacion);
		}

		rs.close();
		pst.close();

		return observaciones;
	}

	public List<Observation> listarTodasObservaciones() throws SQLException {
		String SQL = "SELECT * FROM observaciones";

		PreparedStatement pst = con.prepareStatement(SQL);
		ResultSet rs = pst.executeQuery();

		List<Observation> observaciones = new ArrayList<Observation>();

		while (rs.next()) {
			Observation observacion = new Observation();
			observacion.setIdObservation(rs.getLong("id_observacion"));

			// Creacion del indicador
			Indicator indicador = new Indicator();
			indicador.setIdIndicator(rs.getLong("id_indicador"));
			observacion.setIndicator(indicador);

			// Creacion del area
			Area area = new Area();
			area.setIdArea(rs.getLong("id_area"));
			observacion.setArea(area);

			// Proveedor
			Provider org = new Provider();
			org.setIdOrganization(rs.getLong("id_proveedor"));
			observacion.setProvider(org);

			// Medida
			Measure medida = new Measure();
			medida.setIdMeasure(rs.getLong("id_medida"));
			observacion.setMeasure(medida);

			// Tiempo
			Time tiempo = new Time();
			tiempo.setIdTime(rs.getLong("id_tiempo"));
			observacion.setTime(tiempo);
			
			//Entrada
			Submission entrada = new Submission();
			entrada.setIdSubmission(rs.getLong("id_entrada"));
			observacion.setSubmission(entrada);

			observaciones.add(observacion);
		}

		rs.close();
		pst.close();

		return observaciones;
	}

}
