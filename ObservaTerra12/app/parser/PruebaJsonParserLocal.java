package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import model.Country;
import persistencia.JdbcDAOs.AreasJdbcDAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class PruebaJsonParserLocal {

	public static void test(String url) {

		BufferedReader br;
		AreasJdbcDAO areasDao;
		JsonParser parser;

		try {

			br = new BufferedReader(new FileReader(url));

			parser = new JsonParser();

			JsonArray array = parser.parse(br).getAsJsonObject()
					.get("dimension").getAsJsonArray();

			JsonArray arrayPaises = array.get(0).getAsJsonObject().get("code")
					.getAsJsonArray();

			for (int i = 0; i < arrayPaises.size(); i++) {

				areasDao = new AreasJdbcDAO();

				String code = arrayPaises.get(i).getAsJsonObject().get("label")
						.getAsString();
				String name = arrayPaises.get(i).getAsJsonObject()
						.get("display").getAsString();

				Country c = new Country();
				c.setName(name);

				areasDao.crearArea(c);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
