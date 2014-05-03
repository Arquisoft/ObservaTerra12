package crawler;

import java.io.IOException;
import java.util.TimerTask;

import parser.conectores.Conector;
import parser.conectores.ConectorUnitedNations;
import parser.conectores.ConectorWorldHealthOrganization;

/**
 * 
 * Tarea de actualizacion de los conectores, actualiza todos los datos de cada
 * conector cada vez que se llama
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class CrawlerConectores extends TimerTask {

	Conector conectorUN, conectorWHO;

	public void run() {
		try {
			actualizaDatosUnitedNations();
			actualizaDatosWorldHealthOrganization();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void actualizaDatosUnitedNations() throws IOException {
		conectorUN = ConectorUnitedNations.getInstance("UN");
		conectorUN.preparar();
		conectorUN.start();
	}

	private void actualizaDatosWorldHealthOrganization() throws IOException {
		conectorWHO = ConectorWorldHealthOrganization.getInstance("WHO");
		conectorWHO.preparar();
		conectorWHO.start();
	}

}
