package crawler;

import java.util.Calendar;
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
		Calendar date = Calendar.getInstance();

		// Lanzamos la tarea todos los sabados
		date.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		// timer.schedule(new CrawlerConectores(), date.getTime(), 1000 * 60 *
		// 60
		// * 24 * 7);

		WebCrawlerController crawlerWebsController = new WebCrawlerController(
				5, "public/crawler/temp/");
		crawlerWebsController
				.start("http://api.worldbank.org/v2/en/topic/1?downloadformat=excel/");

	}

}
