package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Pato
 * 
 */
public class GestorParser {

	private final String FICH_PROP = "public/crawler/parser.properties";

	private BufferedReader in;

	public GestorParser() {
		try {
			in = new BufferedReader(new FileReader(FICH_PROP));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: No se encuentra el archivo de entrada");
			e.printStackTrace();
		}
	}

	public void execute() {
		String line;
		try {
			line = in.readLine(); // Se consume la linea de ejemplo
			while ((line = in.readLine()) != null) {
				String[] flags = line.trim().split("@");
				Parser p = ParserFactory.getParser(flags[1]);
				// p.setUrl(flags[0]);
				// p.setTags(flags[2], flags[3], flags[4], flags[5], flags[6],
				// flags[7]);
				// p.execute();// Aqui se le diria al parser que meta los datos
				// en la base
			}
		} catch (IOException e) {
			System.err.println("ERROR: Fallo en la entrada de texto");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
