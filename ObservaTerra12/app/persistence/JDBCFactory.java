package persistence;

import java.sql.SQLException;

import persistence.fachada.DocumentosGateway;
import persistence.fachada.ObservacionesGateway;
import persistence.fachada.OrganizacionesGateway;
import persistence.fachada.UsuariosGateway;
import persistence.impl.DocumentosJdbc;
import persistence.impl.ObservacionesJdbc;
import persistence.impl.OrganizacionesJdbc;
import persistence.impl.UsuariosJdbc;

public class JDBCFactory {

	public static DocumentosGateway createDocumentosFactory() {
		return new DocumentosJdbc();
	}

	public static ObservacionesGateway createObservacionesFactory() {
		return new ObservacionesJdbc();
	}

	public static UsuariosGateway createUsuariosFactory() throws SQLException {
		return new UsuariosJdbc();
	}

	public static OrganizacionesGateway createOrganizacionesFactory() {
		return new OrganizacionesJdbc();
	}
}