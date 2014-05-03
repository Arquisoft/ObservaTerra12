package crawler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * 
 * @author Pablo Garcia Fernandez
 * 
 */
public class WebCrawlerBase extends WebCrawler {

	@Override
	public void visit(Page page) {
		if (page.getWebURL().toString().contains("json")
				|| page.getWebURL().toString().contains("JSON")) {
			System.out.println("Prueba JSON");
			downloadJSON(page);
		} else if (page.getWebURL().toString().contains("excel")
				|| page.getWebURL().toString().contains("EXCEL")) {

			System.out.println("Prueba EXCEL");
			downloadExcel(page);
		} else {
			System.out.println("Web no compatible");
		}
	}

	private void downloadExcel(Page page) {

		String urlSinCortar = page.getWebURL().getURL();

		String url = urlSinCortar.substring(0, urlSinCortar.length() - 3);

		System.out.println("URL: " + url);

		try {
			File file = new File(
					"public/crawler/downloads/pruebaCrawler/PRUEBA.xls");
			FileUtils.copyURLToFile(new URL(url), file);
			
			deleteTempFile(file);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void downloadJSON(Page page) {
		String urlSinCortar = page.getWebURL().getURL();

		String url = urlSinCortar.substring(0, urlSinCortar.length() - 3);

		System.out.println("URL: " + url);

		try {
			File file = new File(
					"public/crawler/downloads/pruebaCrawler/PRUEBA.json");
			FileUtils.copyURLToFile(new URL(url), file);
			
			deleteTempFile(file);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void deleteTempFile(File file) {		
		if(file.delete())
			System.out.println("Operacion de borrado completada cobre el fichero %s. \no", file.getName());
		else
			System.out.printf("Operacion de borrado fallida sobre el fichero %s. \n", file.getName());
	}
}
