package persistencia;

import persistencia.JdbcDAOs.AreasJdbcDAO;
import persistencia.JdbcDAOs.DocumentosJdbcDAO;
import persistencia.JdbcDAOs.IndicadoresJdbcDAO;
import persistencia.JdbcDAOs.MedidasJdbcDAO;
import persistencia.JdbcDAOs.ObservacionesJdbcDAO;
import persistencia.JdbcDAOs.OrganizacionesJdbcDAO;
import persistencia.JdbcDAOs.TiempoJdbcDAO;
import persistencia.JdbcDAOs.UsuariosJdbcDAO;

public class PersistenceFactory 
{
	
	public static AreasDAO createAreasDAO()
	{
		return new AreasJdbcDAO();
	}
	
	public static DocumentosDAO createDocumentosDAO()
	{
		return new DocumentosJdbcDAO();
	}
	
	public static MedidasDAO createMedidasDAO()
	{
		return new MedidasJdbcDAO();
	}
	
	public static ObservacionesDAO createObservacionesDAO()
	{
		return new ObservacionesJdbcDAO();
	}
	
	public static OrganizacionesDAO createOrganizacionesDAO()
	{
		return new OrganizacionesJdbcDAO();
	}
	
	public static TiempoDAO createTiempoDAO()
	{
		return new TiempoJdbcDAO();
	}
	
	public static UsuariosDAO createUsuariosDAO()
	{
		return new UsuariosJdbcDAO();
	}
	
	public static IndicadoresDAO createIndicadoresDAO()
	{
		return new IndicadoresJdbcDAO();
	}
	
	

}
