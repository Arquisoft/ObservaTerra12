package parser;

/**
 * Factoria que se encarga de crear el parser para cada determinado formato
 * 
 * @author Pato
 * 
 */
public class ParserFactory {

	private final static String JSON = "json";
	private final static String XLS = "xls";

	/**
	 * Este metodo crea y devuelve el objeto parser necesario para tratar el
	 * determinado formato que se pasa como parametro.
	 * 
	 * @param formato
	 *            El formato que se ecesita tratar.
	 * @param string
	 * @return Un objeto parser para tratar el determinado formato.
	 */
	public static Parser getParser(String key, String formato) {
		if (formato.equalsIgnoreCase(JSON) && key.equalsIgnoreCase("WHO"))
			return new ParserJsonWHO();
		if (formato.equalsIgnoreCase(JSON) && key.equalsIgnoreCase("UN"))
			return new ParserJsonUN();
		if (formato.equalsIgnoreCase(XLS))
			return new ParserXLS();
		throw new IllegalArgumentException("Formato no reconocido");
	}
}
