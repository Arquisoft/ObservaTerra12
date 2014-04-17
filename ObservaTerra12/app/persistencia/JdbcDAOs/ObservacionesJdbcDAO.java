package persistencia.JdbcDAOs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Area;
import model.Measure;
import model.Observation;
import model.Organization;
import model.Provider;
import model.Time;
import persistencia.ObservacionesDAO;
import persistencia.implJdbc.AreasJdbc;
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

		Connection con = DBConnection.getConnection();

		try {
			// Insertar el area
			AreasJdbc areasJDBC = new AreasJdbc();
			areasJDBC.setConnection(con);
			Area a = areasJDBC.buscarArea(observacion.getArea().getId_area());
			if (a == null)
				areasJDBC.crearAreaySubAreas(observacion.getArea());

			// Insertar el proveedor
			OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
			orgJDBC.setConnection(con);
			Organization or = orgJDBC.leerOrganizacion(observacion
					.getProvider().getIdOrganizacion());
			if (or == null)
				orgJDBC.crearOrganizacion(observacion.getProvider());

			// Insertar la medida
			MedidasJdbc medidas = new MedidasJdbc();
			medidas.setConnection(con);
			Measure md = medidas.buscarMedidaPorIdentificador(observacion
					.getMeasure().getIdMedida());
			if (md == null)
				medidas.crearMedida(observacion.getMeasure());

			// Insertar el tiempo
			TiempoJdbc tiempo = new TiempoJdbc();
			tiempo.setConnection(con);
			Time td = tiempo.buscarIntervaloTiempo(observacion.getTime()
					.getIdTiempo());
			if (td == null)
				tiempo.crearIntervalo(observacion.getTime());

			// Insertar la observacion
			Observation obv = this.observacionesJDBC
					.insertarObservacion(observacion);
			con.commit();
			return obv;
		} catch (Exception e) {
			con.rollback();
			throw new SQLException("Incapaz de completar la transacción", e);
		} finally {
			con.close();
		}
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
		Connection con = DBConnection.getConnection();

		try {
			// No se borra la observacion
			// No se borra el proveedor

			// Borrar la medida
			MedidasJdbc medidas = new MedidasJdbc();
			medidas.setConnection(con);
			medidas.borrarMedida(observacion.getMeasure());

			// Borrar el tiempo
			TiempoJdbc tiempo = new TiempoJdbc();
			tiempo.setConnection(con);
			tiempo.borrarIntervalo(observacion.getTime());

			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw new SQLException("Incapaz de completar la transacción", e);
		} finally {
			con.close();
		}
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
		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		try {
			Observation obv = this.observacionesJDBC
					.buscarObservacionPorIdentificador(identificador);

			// Recuperar la informacion
			// Recuperar el area
			AreasJdbc areasJDBC = new AreasJdbc();
			areasJDBC.setConnection(con);
			Area area = areasJDBC.buscarArea(obv.getArea().getId_area());
			obv.setArea(area);

			// Recuperar el proveedor
			OrganizacionesJdbc orgJDBC = new OrganizacionesJdbc();
			orgJDBC.setConnection(con);
			Organization or = orgJDBC.leerOrganizacion(obv.getProvider()
					.getIdOrganizacion());
			obv.setProvider((Provider) or);

			// Recuperar la medida
			MedidasJdbc medidas = new MedidasJdbc();
			medidas.setConnection(con);
			Measure md = medidas.buscarMedidaPorIdentificador(obv.getMeasure()
					.getIdMedida());
			obv.setMeasure(md);

			// Recuperar el tiempo
			TiempoJdbc tiempo = new TiempoJdbc();
			tiempo.setConnection(con);
			Time td = tiempo.buscarIntervaloTiempo(obv.getTime().getIdTiempo());
			obv.setTime(td);

			con.commit();
			return obv;
		} catch (Exception e) {
			con.rollback();
			throw new SQLException("Incapaz de recoger todos los datos", e);
		} finally {
			con.close();
		}
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
		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		List<Observation> a = this.observacionesJDBC
				.leerObservacionesDeUnArea(area);

		List<Observation> ret = new ArrayList<Observation>();

		for (Observation obv : a) {
			ret.add(buscarObservacionPorIdentificador(obv.getIdObservacion()));
		}

		con.close();
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
		Connection con = DBConnection.getConnection();
		this.observacionesJDBC.setConnection(con);
		List<Observation> a = this.observacionesJDBC.leerObservacionesDeUnIndicador(nombreIndicador);
		List<Observation> ret = new ArrayList<Observation>();

		for (Observation obv : a) {
			ret.add(buscarObservacionPorIdentificador(obv.getIdObservacion()));
		}

		con.close();
		return ret;
	}

}
