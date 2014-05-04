package parser.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PropertiesReader {

	private final static int URL = 0;
	private final static int FORMAT = 1;
	private final static int AREA = 2;
	private final static int INDICATOR = 3;
	private final static int MEASURE = 4;
	private final static int TIME = 5;
	private final static int PROVIDER = 6;
	private final static int SUBMISSION = 7;
	private final static int NUMBER_OF_TAGS = 8;

	private static BufferedReader in;

	/**
	 * @param propFilePath
	 *            La ruta del fichero de propiedades.
	 * @return La URL del archivo a descargar.
	 */
	public static String getURL(String propFilePath) {
		setIn(propFilePath);
		try {
			readTrush();
			return extractURLFrom(in.readLine());
		} catch (IOException e) {
			System.out
					.println("ERROR: Fallo en la entrada de la propiedad de una pagina");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out
						.println("ERROR: Fallo al cerrar el lector de propiedades.");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param propFilePath
	 *            La ruta del fichero de propiedades.
	 * @return Un objeto FileEntry con la URL y las etiquetas de la entrada.
	 */
	public static FileEntry getEntry(String propFilePath) {
		setIn(propFilePath);
		try {
			readTrush();
			return extractEntryFrom(in.readLine());
		} catch (IOException e) {
			System.err
					.println("ERROR: Fallo en la entrada de la propiedad de una pagina");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out
						.println("ERROR: Fallo al cerrar el lector de propiedades.");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Metodo que inicializa el lector con la ruta especificada.
	 * 
	 * @param propFilePath
	 *            La nueva ruta del fichero de propiedades.
	 */
	private static void setIn(String propFilePath) {
		try {
			in = new BufferedReader(new FileReader(propFilePath));
		} catch (FileNotFoundException e) {
			System.out
					.println("ERROR: No se encuentra el archivo de propiedades");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consume la primera linea de ejemplo.
	 * 
	 * @throws IOException
	 */
	private static void readTrush() throws IOException {
		while (in.ready()
				&& (in.readLine().replaceAll("\\s+", "").charAt(0) == '#'))
			in.readLine();
	}

	/**
	 * Metodo que devuelve una cadena con la URL obteniendola de la cadena
	 * especificada.
	 * 
	 * @param line
	 *            Linea desde la que se intentara obtener la URL.
	 * @return Si la cadena se obtuvo devolvera una cadena con ella o null en
	 *         caso contrario.
	 */
	private static String extractURLFrom(String line) {
		String[] tags;

		tags = line.replaceAll("\\s+", "").split("@"); // Elimina todos los
														// caracteres basura de
														// la entrada y separa
														// la direccion del
														// recurso y cada una de
														// las etiquetas.
		if (tags.length != NUMBER_OF_TAGS)
			return null;
		return tags[URL];
	}

	/**
	 * Metodo que extrae una URL y unas etiquetas desde una cadena y devuelve un
	 * objeto FileEntry.
	 * 
	 * @param line
	 *            Cadena desde donde se van a extraer los datos.
	 * @return Un objeto FileEntry con la URL y las etiquetas de la entrada.
	 */
	private static FileEntry extractEntryFrom(String line) {
		String[] tags;

		tags = line.replaceAll("\\s+", "").split("@"); // Elimina todos los
														// caracteres basura de
														// la entrada y separa
														// la direccion del
														// recurso y cada una de
														// las etiquetas.
		if (tags.length != NUMBER_OF_TAGS)
			return null;
		return new FileEntry(tags[URL], tags[FORMAT], tags[AREA],
				tags[INDICATOR], tags[MEASURE], tags[TIME], tags[PROVIDER],
				tags[SUBMISSION]);

	}
}