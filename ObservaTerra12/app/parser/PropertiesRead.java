package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertiesRead {

	public final int URL = 0;
	public final int FORMAT = 1;
	public final int AREA = 2;
	public final int INDICATOR = 3;
	public final int MEASURE = 4;
	public final int TIME = 5;
	public final int PROVIDER = 6;
	public final int SUBMISSION = 7;

	private BufferedReader in;
	private List<String[]> sitesProperties;

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
		this.sitesProperties = new ArrayList<String[]>();
		this.numberOfLines = 0;
	}

	/**
	 * Metodo que extrae la direccion del recurso y las etiquetas de cada una de
	 * las entradas del fichero de propiedades y las almacena en una lista.
	 */
	public void extractData() {
		String line;
		String[] tags;
		try {
			line = in.readLine(); // Se consume la linea de ejemplo
			this.numberOfLines++;
			while ((line = in.readLine()) != null) {
				line = line.replaceAll("\\s+", ""); // Elimina todos los
													// caracteres basura de la
													// entrada.
				tags = line.split("@"); // Separa la direccion del recurso y
										// cada una de las etiquetas.

				if (tags.length != 7)
					throw new IllegalArgumentException("A la entrada "
							+ (numberOfLines + 1)
							+ " le falta alguna etiqueta por especificar.");

				this.sitesProperties.add(line.split("@")); // Almacena la
															// entrada ya
															// troceada
				this.numberOfLines++;
			}
		} catch (IOException e) {
			System.err
					.println("ERROR: Fallo en la entrada de la propiedad de una pagina");
			e.printStackTrace();
		}
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La URL y las etiquetas de la entrada.
	 */
	public String[] getSiteProperties(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex);
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La URL del recurso.
	 */
	public String getURL(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[URL];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Format.
	 */
	public String getFormat(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[FORMAT];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Area.
	 */
	public String getArea(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[AREA];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Indicator.
	 */
	public String getIndicator(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[INDICATOR];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar a la Measure.
	 */
	public String getMeasure(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[MEASURE];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Time.
	 */
	public String getTime(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[TIME];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar al Provider.
	 */
	public String getProvider(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[PROVIDER];
	}

	/**
	 * @param pageIndex
	 *            El numero de linea en el fichero de propiedades de la entrada.
	 * @return La etiqueta que usa el recurso para identificar a la Submission.
	 */
	public String getSubmission(int pageIndex) {
		checkRangeAccess(pageIndex);
		return this.sitesProperties.get(pageIndex)[SUBMISSION];
	}

	private void checkRangeAccess(int index) {
		if (index < 2 || this.numberOfLines < index)
			throw new IndexOutOfBoundsException(
					"El acceso al recurso ha de estar entre la linea 2 y la "
							+ this.numberOfLines + ", ambas inclusive");
	}

}
