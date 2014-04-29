package tests.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Collection;

import model.Country;

import org.junit.Before;
import org.junit.Test;

import parser.PruebaParserJson;
import persistencia.JdbcDAOs.AreasJdbcDAO;

public class ParserTest {

	private Country country1;
	private static String url = "http://apps.who.int/gho/athena/api/COUNTRY?format=json";

	@Before
	public void before() {
		this.country1 = new Country();
		country1.setName("Afghanistan");
	}

	@Test
	public void testCountryParser() {

		PruebaParserJson.test(url);
		try {
			AreasJdbcDAO areasDao = new AreasJdbcDAO();

			assertTrue(areasDao.leerArea("Afghanistan").getName()
					.equals(country1.getName()));

			assertTrue(areasDao.leerArea("Spain").getName().equals("Spain"));

			for (int i = 1; i < 500; i++) {

				System.out.println("Area "
						+ i
						+ " - "
						+ areasDao.buscarArea(Integer.toUnsignedLong(i))
								.getName());

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
