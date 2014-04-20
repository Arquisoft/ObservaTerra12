package tests.PersistenceTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AreasTest.class, DocumentosTest.class, EntradasTest.class,
		IndicadoresTest.class, MedidasTest.class, ObservacionesTest.class,
		OrganizacionesTest.class, TiempoTest.class, UsuariosTest.class })
public class AllPersistenceTests {

}
