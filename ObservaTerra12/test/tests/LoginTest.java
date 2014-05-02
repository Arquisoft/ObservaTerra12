package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.session;
import static play.test.Helpers.status;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import play.test.WithApplication;

import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication());
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
}