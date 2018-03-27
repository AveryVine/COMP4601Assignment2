package edu.carleton.comp4601.resources;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {
	CrawlController controller;
	int numberOfCrawlers = 7;
	String crawlStorageFolder = "data/crawl/root";
	int maxDepthOfCrawling = 10;
	
    public CrawlerController(String dir) throws Exception {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setIncludeBinaryContentInCrawling(true);
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        controller.addSeed("https://sikaman.dyndns.org/courses/4601/assignments/" + dir + "/users/");
        controller.addSeed("https://sikaman.dyndns.org/courses/4601/assignments/" + dir + "/pages/");
        Database.getInstance();
    }
    
    public void crawl() {
    		controller.start(Crawler.class, numberOfCrawlers);
    }
}