package parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Parser HTML utilizando JSOUP
 * 
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class PruebaParserHTML {

	public static void rellena2() throws IOException {
		String URL = "http://apps.who.int/gho/athena/data/GHO/WHOSIS_000002,WHOSIS_000001,WHOSIS_000015.html?profile=ztable&filter=COUNTRY:*;REGION:AFR;REGION:AMR;REGION:SEAR;REGION:EUR;REGION:EMR;REGION:WPR;SEX:*";
		// Nos descargamos en memoria la pagina HTML y la almacenamos en un
		// objeto Document
		Document doc = Jsoup.connect(URL).get();
		// Solo nos interesan las tablas del documento (en este caos solo hay
		// una gran tabla).
		Element table = doc.select("table").first();
		// Creamos un arrayList con los headers de la tabla
		List<String> headers = new ArrayList<String>();

		// Recorremos los th
		Iterator<Element> iteratorTH = table.select("th").iterator();
		while (iteratorTH.hasNext()) {
			headers.add(iteratorTH.next().text());
		}

		// Creamos un ArrayList<ArrayList<String>> para los td, de tal forma que
		// coincida cada lista con una tr de la tabla

		ArrayList<ArrayList<String>> contenido = new ArrayList<ArrayList<String>>();

		// Recorremos los tr de la tabla (filas)
		for (Element row : table.select("tr")) {

			ArrayList<String> fila = new ArrayList<String>();

			// Recorremos cada una de las columnas de la fila,
			// almacenando sus datos en una lista que luego se almacena en la
			// lista de listas
			Iterator<Element> iteratorTD = row.select("td").iterator();
			while (iteratorTD.hasNext()) {
				fila.add(iteratorTD.next().text());
			}

			contenido.add(fila);
		}

		// Como prueba final mostramos por pantalla las primeras 10 filas de la
		// tabla (la tabla contiene unas 4000 filas)

		// Mostramos los headers de la tabla
		for (String s : headers)
			System.out.print(s + " ");

		// Mostramos las filas, recorriendo solo las 10 primeras.
		for (int i = 0; i < 10; i++) {

			String fila = "";

			for (String s : contenido.get(i)) {
				fila += s + " - ";
			}

			// Mostramos por pantalla la fila
			System.out.println(fila);
		}
	}

}
