package parser;

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

	static int i = 0;

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		try {
			File file = new File("public/crawler/temp/" + i + ".json");
			FileUtils.copyURLToFile(new URL(url), file);
			i++;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
