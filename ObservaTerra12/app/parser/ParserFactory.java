package parser;

/**
 * Factoria que se encarga de crear el parser para cada determinado formato
 * 
 * @author Pato
 * 
 */
public class ParserFactory {

	private final static String JSON = "json";
	private final static String XML = "xml";

	/**
	 * Este metodo crea y devuelve el objeto parser necesario para tratar el
	 * determinado formato que se pasa como parametro.
	 * 
	 * @param formato
	 *            El formato que se ecesita tratar.
	 * @return Un objeto parser para tratar el determinado formato.
	 */
	public static Parser getParser(String formato) {
		if (formato.equalsIgnoreCase(JSON))
			return new ParserJson();
		if (formato.equalsIgnoreCase(XML))
			return new ParserXLS();
		throw new IllegalArgumentException("Formato no reconocido");

	}
}
