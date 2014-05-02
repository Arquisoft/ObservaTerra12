package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.session;
import static play.test.Helpers.status;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import persistencia.PersistenceFactory;
import persistencia.UsuariosDAO;
import play.mvc.Result;
import play.test.WithApplication;

import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void authenticateSuccess() {
		Result result = callAction(
				controllers.routes.ref.Application.authenticate(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("userName", "periodista1", "password",
								"periodista1")));
		assertEquals(303, status(result));
		assertEquals("periodista1", session(result).get("userName"));
	}

	@Test
	public void logout() {
		authenticateSuccess();
		Result result = callAction(controllers.routes.ref.Application.logout());
		assertEquals(303, status(result));
		assertNull(session(result).get("userName"));
	}

	@Test
	public void authenticateFailure() {
		Result result = callAction(
				controllers.routes.ref.Application.authenticate(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("userName", "FROOORF", "password",
								"MIPIM")));
		assertEquals(400, status(result));
		assertNull(session(result).get("userName"));
	}

	@Test
    public void registerUser() {
    	Map<String,String> mapaForm = new HashMap<String,String>();
    	mapaForm.put("userName", "_userName");
    	mapaForm.put("password", "_password");
    	mapaForm.put("name", "_name");
    	mapaForm.put("surname", "_surname");
    	mapaForm.put("email", "_em@i.l");
    	mapaForm.put("organization", "");
        Result result = callAction(
            controllers.routes.ref.Application.registerUser(),
            fakeRequest().withFormUrlEncodedBody(ImmutableMap.copyOf(mapaForm))
        );
        assertEquals(303, status(result));
		assertEquals("_userName", session(result).get("userName"));
		UsuariosDAO dao = PersistenceFactory.createUsuariosDAO();
		try {
			dao.eliminarUsuario(dao.buscarUsuario("_userName"));
		} catch (SQLException e) {
			assert false;
		}
    }

	@Test
    public void registerUserBadOrganization() {
    	Map<String,String> mapaForm = new HashMap<String,String>();
    	mapaForm.put("userName", "_userName");
    	mapaForm.put("password", "_password");
    	mapaForm.put("name", "_name");
    	mapaForm.put("surname", "_surname");
    	mapaForm.put("email", "_em@i.l");
    	mapaForm.put("organization", "PINGAS");
        Result result = callAction(
            controllers.routes.ref.Application.registerUser(),
            fakeRequest().withFormUrlEncodedBody(ImmutableMap.copyOf(mapaForm))
        );
		assertEquals(400, status(result));
		assertNull(session(result).get("userName"));
		UsuariosDAO dao = PersistenceFactory.createUsuariosDAO();
		try {
			assertNull(dao.buscarUsuario("_userName"));
		} catch (SQLException e) {
			assert false;
		}
    }
}