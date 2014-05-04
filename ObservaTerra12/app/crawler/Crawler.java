package crawler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

/**
 * 
 * Lanzador principal. Utiliza un scheduler para lanzar las tareas indicadas
 * cada X tiempo
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class Crawler {

	public static void main(String[] args) {
		Timer timer = new Timer();
		List<String> listURLs = new ArrayList<String>();
		Calendar date = Calendar.getInstance();

		// Lanzamos la tarea todos los sabados
		date.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		timer.schedule(new TareaConectores(), date.getTime(), 1000 * 60 * 60
				* 24 * 7);

		WebCrawlerController crawlerWebsController = new WebCrawlerController(
				5, "public/crawler/temp/");
		listURLs.add("http://api.worldbank.org/v2/en/topic/1?downloadformat=excel/");
		crawlerWebsController.start(listURLs);
	}

}
