package parser;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.sql.SQLException;

import model.Country;
import persistencia.JdbcDAOs.AreasJdbcDAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * Parser JSON utilizando GSON
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class PruebaParserJson {

	public static void test(String url) {

		InputStream is;
		BufferedReader br;
		AreasJdbcDAO areasDao;
		JsonParser parser;

		try {
			is = new URL(url).openStream();

			File file = new File("public/crawler/temp/information.json");
			org.apache.commons.io.FileUtils.copyURLToFile(new URL(url), file);

			br = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));

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
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
