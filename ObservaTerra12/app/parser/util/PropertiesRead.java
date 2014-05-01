package parser.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertiesRead {

	private final int URL = 0;
	private final int FORMAT = 1;
	private final int AREA = 2;
	private final int INDICATOR = 3;
	private final int MEASURE = 4;
	private final int TIME = 5;
	private final int PROVIDER = 6;
	private final int SUBMISSION = 7;

	private BufferedReader in;
	private List<FileEntry> entries;

	private int numberOfLines;

	/**
	 * Constructor de la clase que recibe la ruta del fichero de propiedades.
	 * 
	 * @param propFilePath
	 *            La ruta del fichero de propiedades desde el que se leera.
	 */
	public PropertiesRead(String propFilePath) {
		try {
			this.in = new BufferedReader(new FileReader(propFilePath));
		} catch (FileNotFoundException e) {
			System.err
					.println("ERROR: No se encuentra el archivo de propiedades");
			e.printStackTrace();
		}
		this.entries = new ArrayList<FileEntry>();
		this.numberOfLines = 0;
	}

	/**
	 * Metodo que extrae la direccion del recurso y las etiquetas de cada una de
	 * las entradas del fichero de propiedades y las almacena en una lista.
	 */
	public void extractData() {
		try {
			readTrush();
			while (in.ready()) {
				insertEntry(in.readLine());
				this.numberOfLines++;
			}
		} catch (IOException e) {
			System.err
					.println("ERROR: Fallo en la entrada de la propiedad de una pagina");
			e.printStackTrace();
		}
	}

	private void insertEntry(String line) {
		String[] tags;
		
		tags = line.replaceAll("\\s+", "").split("@"); // Elimina todos los
														// caracteres basura de
														// la entrada y separa
														// la direccion del
														// recurso y cada una de
														// las etiquetas.
		if (tags.length != 8)
			this.entries.add(null);
		else
			this.entries.add(new FileEntry(tags[URL], tags[FORMAT], tags[AREA],
					tags[INDICATOR], tags[MEASURE], tags[TIME], tags[PROVIDER],
					tags[SUBMISSION])); // Almacena la entrada ya troceada
	}

	/**
	 * Metodo que consume la primera linea de ejemplo.
	 * 
	 * @throws IOException
	 */
	private void readTrush() throws IOException {
		in.readLine();
		this.numberOfLines++;
	}

	public List<FileEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return Un objeto FileEntry con la URL y las etiquetas de la entrada.
	 */
	public FileEntry getSiteProperties(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex);
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La URL del recurso.
	 */
	public String getURL(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getUrl();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Format.
	 */
	public String getFormat(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getFormatTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Area.
	 */
	public String getArea(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getAreaTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Indicator.
	 */
	public String getIndicator(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getIndicatorTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar a la Measure.
	 */
	public String getMeasure(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getMeasureTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Time.
	 */
	public String getTime(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getTimeTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Provider.
	 */
	public String getProvider(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getProviderTag();
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar a la Submission.
	 */
	public String getSubmission(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.entries.get(pageIndex).getSubmissionTag();
	}

	private void checkRangeAccess(int index) {
		if (index < 2 || this.numberOfLines < index)
			throw new IndexOutOfBoundsException(
					"El acceso al recurso ha de estar entre la linea 2 y la "
							+ this.numberOfLines + ", ambas inclusive");
	}

}
