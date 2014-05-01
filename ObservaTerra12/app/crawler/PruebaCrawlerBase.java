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
public class PruebaCrawlerBase extends WebCrawler {

	@Override
	public void visit(Page page) {
		if (page.getWebURL().toString().contains("json")
				|| page.getWebURL().toString().contains("JSON")) {
			System.out.println("Hol.aaaaaaaaaaaaaaaaaaaaa");
			downloadJSON(page);
		} else {
			String url = page.getWebURL().getURL();

			String url2 = url.substring(0, url.length() - 3);

			System.out.println("URL: " + url2);

			try {
				File file = new File("public/crawler/temp/downloads/PRUEBA.xls");
				FileUtils.copyURLToFile(new URL(url2), file);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void downloadJSON(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		try {
			File file = new File("public/crawler/temp/downloads/1.json");
			FileUtils.copyURLToFile(new URL(url), file);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
