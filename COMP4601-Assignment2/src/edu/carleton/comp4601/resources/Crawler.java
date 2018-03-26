package edu.carleton.comp4601.resources;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp4|zip|gz))$");
	private long startTime = System.currentTimeMillis(), endTime = System.currentTimeMillis();
	ArrayList<String> saxParserMimeTypes;

	/**
	 * This method receives two parameters. The first parameter is the page in which
	 * we have discovered this new url and the second parameter is the new url. You
	 * should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic). In this example, we are
	 * instructing the crawler to ignore urls that have css, js, git, ... extensions
	 * and to only accept urls that start with "http://www.ics.uci.edu/". In this
	 * case, we didn't need the referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && ((href.startsWith("https://sikaman.dyndns.org/courses/4601/assignments")));
	}
	
	/*
	 * Purpose: we overrode this in order to track the start time of the crawl
	 * Input: the WebUrl of the page to be crawled, the status code of the fetch of the page, the status description
	 * Return: none
	 */
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		startTime = System.currentTimeMillis();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);
		String title = "";

		try {
			InputStream input = TikaInputStream.get(new URL(page.getWebURL().getURL()));
			ContentHandler contentHandler = new BodyContentHandler(-1);
			Metadata metadata = new Metadata();
			ParseContext parseContext = new ParseContext();
			Parser parser = new AutoDetectParser();
			parser.parse(input, contentHandler, metadata, parseContext);
			title = metadata.get(Metadata.TITLE);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (page.getParseData() instanceof HtmlParseData) {
			// HTML parsing
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			int docId = page.getWebURL().getDocid();
			if (page.getWebURL().getURL().contains("pages")) {
				System.out.println("Adding webpage");
				WebPage webPage = new WebPage(docId, title);
				Database.getInstance().insert(webPage);
			} else if (page.getWebURL().getURL().contains("users")) {
				System.out.println("Adding user");
				User user = new User(docId, title);
				Database.getInstance().insert(user);
			} else {
				System.out.println("Failed to classify crawled webpage");
			}

			// Output for debugging purposes
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
	}
}