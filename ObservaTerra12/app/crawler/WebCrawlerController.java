package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class WebCrawlerController {

	private int numberOfCrawlers;
	private String crawlerFolder;
	private CrawlConfig config;

	/**
	 * Constructor de la clase. Determina el numero de crawlers a 1 por defecto.
	 * La carpeta de destino de las descargas la establece por defecto en
	 * "public/crawler/temp/".
	 */
	public WebCrawlerController() {
		this.numberOfCrawlers = 1;
		this.crawlerFolder = "public/crawler/temp/";
		this.config = new CrawlConfig();
	}

	/**
	 * Constructor de la clase. Determina el numero de crawlers a 1 por defecto.
	 * La carpeta de destino de las descargas la establece por defecto en
	 * "public/crawler/temp/".
	 * 
	 * @param config
	 *            Determina la configuracion del crawler.
	 */
	public WebCrawlerController(CrawlConfig config) {
		this.numberOfCrawlers = 1;
		this.crawlerFolder = "public/crawler/temp/";
		this.config = config;
	}

	/**
	 * Constructor de la clase.
	 * 
	 * @param numberOfCrawlers
	 *            Numero de crawler a usar.
	 * @param crawlerFolder
	 *            Carpeta de destino de las descargas.
	 */
	public WebCrawlerController(int numberOfCrawlers, String crawlerFolder) {
		this.crawlerFolder = crawlerFolder;
		this.numberOfCrawlers = numberOfCrawlers;
		this.config = new CrawlConfig();
	}

	/**
	 * @param numberOfCrawlers
	 *            Numero de crawler a usar.
	 * @param crawlerFolder
	 *            Carpeta de destino de las descargas.
	 * @param config
	 *            Determina la configuracion del crawler.
	 */
	public WebCrawlerController(int numberOfCrawlers, String crawlerFolder,
			CrawlConfig config) {
		this.numberOfCrawlers = numberOfCrawlers;
		this.crawlerFolder = crawlerFolder;
		this.config = config;
	}

	/**
	 * @return El numero de crawlers que se estan usando.
	 */
	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	/**
	 * @param numberOfCrawlers
	 *            Determina el nuevo numero de crawlers.
	 */
	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	/**
	 * @return La carpeta de destino de las descargas.
	 */
	public String getCrawlerFolder() {
		return crawlerFolder;
	}

	/**
	 * @param crawlerFolder
	 *            Determina la nueva carpeta de destino de las descargas.
	 */
	public void setCrawlerFolder(String crawlerFolder) {
		this.crawlerFolder = crawlerFolder;
	}

	/**
	 * @return La configuracion del crawler.
	 */
	public CrawlConfig getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            Determina la nueva configuracion del crawler.
	 */
	public void setConfig(CrawlConfig config) {
		this.config = config;
	}

	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.config.setCrawlStorageFolder(crawlStorageFolder);
	}

	public void setPolitenessDelay(int politenessDelay) {
		this.config.setPolitenessDelay(politenessDelay);
	}

	public void setMaxPagesToFetch(int maxPagesToFetch) {
		this.config.setMaxPagesToFetch(maxPagesToFetch);
	}

	public void setResumableCrawling(boolean resumableCrawling) {
		this.config.setResumableCrawling(resumableCrawling);
	}

	/**
	 * Metodo que pone en ejecucion el crawler
	 * 
	 * @param url
	 *            Direccion de la pagina a descargar
	 * @throws Exception
	 */
	public void start(String url) {
		try {
			setCrawlStorageFolder(crawlerFolder);

			/*
			 * Be polite: Make sure that we don't send more than 1 request per
			 * second (1000 milliseconds between requests).
			 */
			setPolitenessDelay(1000);

			/*
			 * You can set the maximum crawl depth here. The default value is -1
			 * for unlimited depth
			 */
			// config.setMaxDepthOfCrawling(52);

			/*
			 * You can set the maximum number of pages to crawl. The default
			 * value is -1 for unlimited number of pages
			 */
			setMaxPagesToFetch(1000);

			/*
			 * This config parameter can be used to set your crawl to be
			 * resumable (meaning that you can resume the crawl from a
			 * previously interrupted/crashed crawl). Note: if you enable
			 * resuming feature and want to start a fresh crawl, you need to
			 * delete the contents of rootFolder manually.
			 */
			setResumableCrawling(false);

			/*
			 * Instantiate the controller for this crawl.
			 */
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(
					robotstxtConfig, pageFetcher);
			CrawlController controller;

			controller = new CrawlController(config, pageFetcher,
					robotstxtServer);

			/*
			 * For each crawl, you need to add some seed urls. These are the
			 * first URLs that are fetched and then the crawler starts following
			 * links which are found in these pages
			 */
			/*
			 * controller.addSeed(
			 * "http://apps.who.int/gho/athena/api/COUNTRY?format=json/");
			 * controller .addSeed(
			 * "http://api.worldbank.org/v2/en/topic/1?downloadformat=excel/" );
			 */
			controller.addSeed(url);

			/*
			 * Start the crawl. This is a blocking operation, meaning that your
			 * code will reach the line after this only when crawling is
			 * finished.
			 */
			controller.start(WebCrawlerBase.class, numberOfCrawlers);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
