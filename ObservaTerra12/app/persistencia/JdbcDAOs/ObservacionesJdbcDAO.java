package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;
import model.Indicator;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Submission;
import model.Time;
import persistencia.ObservacionesDAO;
import persistencia.implJdbc.AreasJdbc;
import persistencia.implJdbc.EntradasJdbc;
import persistencia.implJdbc.IndicadoresJdbc;
import persistencia.implJdbc.MedidasJdbc;
import persistencia.implJdbc.ObservacionesJdbc;
import persistencia.implJdbc.OrganizacionesJdbc;
import persistencia.implJdbc.TiempoJdbc;
import utils.DBConnection;

public class ObservacionesJdbcDAO implements ObservacionesDAO {

	private ObservacionesJdbc observacionesJDBC;

	public ObservacionesJdbcDAO() {
		this.observacionesJDBC = new ObservacionesJdbc();
	}

	public ObservacionesJdbc getObservacionesJDBC() {
		return observacionesJDBC;
	}

	public void setObservacionesJDBC(ObservacionesJdbc observacionesJDBC) {
		this.observacionesJDBC = observacionesJDBC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#insertarObservacion(model.Observation
	 * )
	 */
	@Override
	public Observation insertarObservacion(Observation observacion)
			throws SQLException {
		if (observacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado una observación.");

		Connection con = DBConnection.getConnection();
		try {
			con.setAutoCommit(false);
			// Busca una observación seguún su contenido
			Observation ret = buscarContenidoObservacion(observacion, con);
			if (ret != null) {
				con.rollback(); // Deshaz posibles cambios
				return ret;
			}
			// No se ha encontrado. Proceder a crearla.
			Observation obv = insertarContenidoObservacion(observacion, con);
			con.commit();
			return obv;
		} catch (NullPointerException e) {
			con.rollback();
			throw new IllegalArgumentException(
					"La observación parece no estar completa. Revisar el contenido.");
		} catch (SQLException e) {
			con.rollback();
			throw new SQLException("Incapaz de completar la transacción", e);
		} finally {
			con.close();
		}
	}

	/**
	 * Función auxiliar que comprueba si ya existe una observacion en base a su
	 * contenido. En caso de que exista, se devuelve.
	 * 
	 * @param observacion
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private Observation buscarContenidoObservacion(Observation observacion,
			Connection con) throws SQLException {
		// Buscar el area o el país
		AreasJdbc areaJDBC = new AreasJdbc();
		areaJDBC.setConnection(con);
		Area leida = areaJDBC.buscarAreaYPaisPorNombre(observacion.getArea()
				.getName());
		if (leida != null) // Si no se encontró el area
			observacion.setArea(leida);
		else
			return null;

		// Buscar el indicador
		IndicadoresJdbc indicadoresJDBC = new IndicadoresJdbc();
		indicadoresJDBC.setConnection(con);
		Indicator inLeido = indicadoresJDBC.leerIndicador(observacion
				.getIndicator().getNombre());
		if (inLeido != null)
			observacion.setIndicator(inLeido);
		else
			return null;

		// Buscar la medida
		MedidasJdbc medidasJDBC = new MedidasJdbc();
		medidasJDBC.setConnection(con);
		Measure mLeido = medidasJDBC.buscarMedidaPorValorYUnidad(observacion
				.getMeasure().getValue(), observacion.getMeasure().getUnit());
		if (mLeido != null)
			observacion.setMeasure(mLeido);
		else
			return null;

		// Buscar el tiempo
		TiempoJdbc tiempoJDBC = new TiempoJdbc();
		tiempoJDBC.setConnection(con);
		Time tLeido = tiempoJDBC.buscarIntervaloTiempo(observacion.getTime()
				.getStartDate(), observacion.getTime().getEndDate());
		if (tLeido != null)
			observacion.setTime(tLeido);
		else
			return null;

		// Buscar la organizacion
		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		Provider orgLeida = orgJDBC.buscarProveedorPorNombre(observacion
				.getProvider().getNombre());
		if (orgLeida != null)
			observacion.setProvider(orgLeida);
		else
			return null;

		// No se busca la entrada: es el dato menos
		// significativo y se lee directamente

		// Llegó hasta aquí: todo es correcto - buscar su identificador
		this.observacionesJDBC.setConnection(con);
		observacion = this.observacionesJDBC
				.leerObservacionPorContenido(observacion);

		// Leer la entrada
		EntradasJdbc entradasJDBC = new EntradasJdbc();
		entradasJDBC.setConnection(con);
		Submission entrada = entradasJDBC.leerEntrada(observacion
				.getSubmission().getIdSubmission());
		observacion.setSubmission(entrada);
		return observacion;
	}

	/**
	 * Función auxiliar que inserta el contenido de una observacion dentro de
	 * una transacción. Comprueba por ID que no existan los datos ya.
	 * 
	 * @param observacion
	 *            - Observacion a insertar
	 * @param con
	 *            - Conexion a utilizar.
	 * @return
	 * @throws SQLException
	 */
	private Observation insertarContenidoObservacion(Observation observacion,
			Connection con) throws SQLException {
		// Insertar la entrada
		EntradasJdbc entradasJDBC = new EntradasJdbc();
		entradasJDBC.setConnection(con);
		// Si no está registrada, crearla
		if (observacion.getSubmission().getIdSubmission() == null)
			observacion.setSubmission(entradasJDBC.crearEntrada(observacion
					.getSubmission()));

		// Insertar el indicador
		IndicadoresJdbc indicadorJDBC = new IndicadoresJdbc();
		indicadorJDBC.setConnection(con);
		if (observacion.getIndicator().getIdIndicator() == null)
			observacion.setIndicator(indicadorJDBC.añadirIndicador(observacion
					.getIndicator()));

		// Insertar el area
		AreasJdbc areasJDBC = new AreasJdbc();
		areasJDBC.setConnection(con);
		if (observacion.getArea().getIdArea() == null)
			areasJDBC.crearAreaySubAreas(observacion.getArea());

		// Insertar el proveedor
		if (!(observacion.getProvider() instanceof Provider))
			throw new IllegalArgumentException(
					"La observación no viene de un proveedor.");

		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		if (observacion.getProvider().getIdOrganization() == null)
			observacion.setProvider((Provider) orgJDBC
					.crearOrganizacion(observacion.getProvider()));

		// Insertar la medida
		MedidasJdbc medidas = new MedidasJdbc();
		medidas.setConnection(con);
		if (observacion.getMeasure().getIdMeasure() == null)
			observacion
					.setMeasure(medidas.crearMedida(observacion.getMeasure()));

		// Insertar el tiempo
		TiempoJdbc tiempo = new TiempoJdbc();
		tiempo.setConnection(con);
		if (observacion.getTime().getIdTime() == null)
			observacion.setTime(tiempo.crearIntervalo(observacion.getTime()));

		// Insertar la observacion
		this.observacionesJDBC.setConnection(con);
		Observation obv = this.observacionesJDBC
				.insertarObservacion(observacion);
		return obv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#eliminarObservacion(model.Observation
	 * )
	 */
	@Override
	public void eliminarObservacion(Observation observacion)
			throws SQLException {
		if (observacion == null)
			throw new IllegalArgumentException(
					"No se ha indicado la observación.");
		else if (observacion.getIdObservation() == null)
			throw new IllegalArgumentException(
					"La observación no contiene su identificador único.");

		Connection con = DBConnection.getConnection();

		try {
			con.setAutoCommit(false);
			// Borrar la observacion
			this.observacionesJDBC.setConnection(con);
			this.observacionesJDBC.eliminarObservacion(observacion);
			// Borrar la informacion
			borrarInformacion(observacion, con);
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/**
	 * Función auxiliar para borrar la información contenida en la observacion
	 * 
	 * @param observacion
	 *            - Observacion a borrar.
	 * @param con
	 *            - Conexión a utilizar.
	 * @throws SQLException
	 */
	private void borrarInformacion(Observation observacion, Connection con)
			throws SQLException {
		// Borrar la entrada
		EntradasJdbc entradasJDBC = new EntradasJdbc();
		entradasJDBC.setConnection(con);
		entradasJDBC.eliminarEntrada(observacion.getSubmission());

		// Borrar el indicador
		IndicadoresJdbc indicadorJDBC = new IndicadoresJdbc();
		indicadorJDBC.setConnection(con);
		indicadorJDBC.eliminarIndicador(observacion.getIndicator());

		// Borrar el area
		AreasJdbc areasJDBC = new AreasJdbc();
		areasJDBC.setConnection(con);
		areasJDBC.eliminarArea(observacion.getArea());

		// Borrar el proveedor
		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		orgJDBC.borrarOrganizacion(observacion.getProvider());

		// Borrar la medida
		MedidasJdbc medidas = new MedidasJdbc();
		medidas.setConnection(con);
		medidas.borrarMedida(observacion.getMeasure());

		// Borrar el tiempo
		TiempoJdbc tiempo = new TiempoJdbc();
		tiempo.setConnection(con);
		tiempo.borrarIntervalo(observacion.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#buscarObservacionPorIdentificador
	 * (java.lang.Long)
	 */
	@Override
	public Observation buscarObservacionPorIdentificador(Long identificador)
			throws SQLException {
		if (identificador == null)
			throw new IllegalArgumentException(
					"No se ha indicado el identificador.");

		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		try {
			con.setAutoCommit(false);
			//Busca una observación según su contenido
			Observation obv = this.observacionesJDBC.buscarObservacionPorIdentificador(identificador);
			if (obv != null) {//Si se encuentra, recuperar toda la información
				obv = construirObservacion(con, obv);
			}
			con.commit();
			return obv;
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			con.close();
		}
	}

	/**
	 * Función auxiliar que ayuda a construir la observacion.
	 * 
	 * @param con
	 *            - Conexión a utilizar.
	 * @param obv
	 *            - Observacion a construir
	 * @throws SQLException
	 */
	private Observation construirObservacion(Connection con, Observation obv)
			throws SQLException {
		// Recuperar la informacion

		// Recuperar la entrada
		EntradasJdbc entradasJDBC = new EntradasJdbc();
		entradasJDBC.setConnection(con);
		obv.setSubmission(entradasJDBC.leerEntrada(obv.getSubmission()
				.getIdSubmission()));

		// Recuperar el indicador
		IndicadoresJdbc indicadorJDBC = new IndicadoresJdbc();
		indicadorJDBC.setConnection(con);
		Indicator indic = indicadorJDBC.leerIndicador(obv.getIndicator()
				.getIdIndicator());
		obv.setIndicator(indic);

		// Recuperar el area
		AreasJdbc areasJDBC = new AreasJdbc();
		areasJDBC.setConnection(con);
		Area area = areasJDBC.buscarAreaYPaisPorId(obv.getArea().getIdArea());
		obv.setArea(area);

		// Recuperar el proveedor
		OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
		orgJDBC.setConnection(con);
		Organization or = orgJDBC.leerProveedor(obv.getProvider()
				.getIdOrganization());
		obv.setProvider((Provider) or);

		// Recuperar la medida
		MedidasJdbc medidas = new MedidasJdbc();
		medidas.setConnection(con);
		Measure md = medidas.buscarMedidaPorIdentificador(obv.getMeasure()
				.getIdMeasure());
		obv.setMeasure(md);

		// Recuperar el tiempo
		TiempoJdbc tiempo = new TiempoJdbc();
		tiempo.setConnection(con);
		Time td = tiempo.buscarIntervaloTiempo(obv.getTime().getIdTime());
		obv.setTime(td);

		return obv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#leerObservacionesDeUnArea(model
	 * .Area)
	 */
	@Override
	public List<Observation> leerObservacionesDeUnArea(Area area)
			throws SQLException {
		if (area == null)
			throw new IllegalArgumentException("El parámetro es null");
		else if (area.getIdArea() == null)
			throw new IllegalArgumentException(
					"El area no lleva su identificador único.");

		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		//Leer todas las observaciones de un area
		List<Observation> a = this.observacionesJDBC.leerObservacionesDeUnArea(area);

		List<Observation> ret = new ArrayList<Observation>();
		con.close();
		
		//Por cada observación, recuperla por identificador.
		for (Observation obv : a) {
			ret.add(buscarObservacionPorIdentificador(obv.getIdObservation()));
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#leerObservacionesDeUnIndicador
	 * (java.lang.String)
	 */
	@Override
	public List<Observation> leerObservacionesDeUnIndicador(
			String nombreIndicador) throws SQLException {
		if (nombreIndicador == null || nombreIndicador.isEmpty())
			throw new IllegalArgumentException("Debe indicarse un nombre.");

		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		//Listar todas las observaciones
		List<Observation> a = this.observacionesJDBC
				.leerObservacionesDeUnIndicador(nombreIndicador);
		List<Observation> ret = new ArrayList<Observation>();

		con.close();
		//Recuperarla una a una
		for (Observation obv : a) {
			ret.add(buscarObservacionPorIdentificador(obv.getIdObservation()));
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.JdbcDAOs.ObservacionesDAO#listarTodasObservaciones
	 * (java.lang.String)
	 */
	@Override
	public List<Observation> listarTodasObservaciones() throws SQLException {
		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		List<Observation> lista = this.observacionesJDBC.listarTodasObservaciones();
		
		//Construir
		for(Observation obv : lista)
		{
			this.construirObservacion(con, obv);
		}
		
		
		con.close();
		return lista;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * persistencia.JdbcDAOs.ObservacionesDAO#insertarObservacion(model.Observation
	 * , model.Submission)
	 */
	@Override
	public Observation insertarObservacion(Observation observacion,
			Submission submission) throws SQLException {
		if (observacion == null || submission == null)
			throw new IllegalArgumentException("Uno de los parámetros es null.");

		observacion.setSubmission(submission);
		return this.insertarObservacion(observacion);
	}

}
