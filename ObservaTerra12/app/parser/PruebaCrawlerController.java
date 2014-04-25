package parser;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class PruebaCrawlerController {

	public static void main(String[] args) throws Exception {
		PruebaCrawlerController p = new PruebaCrawlerController(5,
				"public/crawler/temp/");
		p.start();
	}

	private int numberOfCrawlers;
	private String crawlerFolder;

	/**
	 * 
	 * @param numberOfCrawlers
	 * @param crawlerFolder
	 */
	public PruebaCrawlerController(int numberOfCrawlers, String crawlerFolder) {
		this.crawlerFolder = crawlerFolder;
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public void start() throws Exception {
		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlerFolder);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		// config.setMaxDepthOfCrawling(52);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(1000);

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */

		controller.addSeed("http://www.ics.uci.edu/");
		controller.addSeed("http://www.ics.uci.edu/~lopes/");
		controller.addSeed("http://www.ics.uci.edu/~welling/");
		// controller
		// .addSeed("http://apps.who.int/gho/athena/api/COUNTRY?format=json/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(PruebaCrawlerBase.class, numberOfCrawlers);
	}

}
