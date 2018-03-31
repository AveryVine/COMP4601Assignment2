package edu.carleton.comp4601.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rs")
public class Recommender {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	String name, authorName1, authorName2;
	CrawlerController controller;
	NaiveBayes sentimentAnalyzer, genreAnalyzer;
//	Clustering clustering;

	public Recommender() {
		authorName1 = "Avery Vine";
		authorName2 = "Maxim Kuzmenko";
		name = "COMP4601 Assignment 2 Recommender System: " + authorName1 + " and " + authorName2;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getName() {
		System.out.println("name");
		String res = "<h1> " + name + " </h1>";
		return wrapHTML(name, res);
	}

	@GET
	@Path("reset/{dir}")
	public Response reset(@PathParam("dir") String dir) {
		System.out.println("reset -> " + dir);
		Response res = Response.ok().build();
		Database.getInstance().clear();
		try {
			controller = new CrawlerController(dir);
//			controller.crawl();
			genreAnalyzer = new GenreAnalyzer();
			genreAnalyzer.analyze();
//			clustering = new Clustering();
//			clustering.run();
		} catch (Exception e) {
			System.err.println("Error crawling data in dir: " + dir);
			e.printStackTrace();
			res = Response.serverError().build();
		}
		return res;
	}
	
	@GET
	@Path("context")
	@Produces(MediaType.TEXT_HTML)
	public String context() {
		System.out.println("context");
		sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.analyze();
				
		String res = "<table border='1px'> ";
		res += User.htmlTableHeader();
		for (User user : Database.getInstance().getUsers()) {
			res += user.htmlTableData();
		}
		res += "</table>";
		return wrapHTML("Context", res);
	}
	
	@GET
	@Path("community")
	@Produces(MediaType.TEXT_HTML)
	public String community() {
		System.out.println("community");
		String res = "Community";
		return wrapHTML("Community", res);
	}
	
	@GET
	@Path("fetch")
	@Produces(MediaType.TEXT_HTML)
	public String fetch() {
		System.out.println("fetch");
		boolean setPrompts = true;
		String res = "<table border> ";
		res += WebPage.htmlTableHeader();
		for (WebPage webpage: Database.getInstance().getWebPages()) {
			res += webpage.htmlTableData(setPrompts);
		}
		res += " </table>";
		return wrapHTML("Fetch", res);
	}
	
	@GET
	@Path("fetch/{user}/{page}")
	@Produces(MediaType.TEXT_HTML)
	public String fetch(@PathParam("user") String user, @PathParam("page") String page) {
		System.out.println("fetch -> " + user + ", " + page);
		String res = Database.getInstance().getWebPage(page).getHTML();
		System.out.println(res);
		res = Advertiser.augment(res);
		return wrapHTML("Fetch", res);
	}
	
	@GET
	@Path("advertising/{category}")
	@Produces(MediaType.TEXT_HTML)
	public String advertising(@PathParam("category") String category) {
		System.out.println("advertising -> " + category);
		String res = "Advertising";
		return wrapHTML("Advertising", res);
	}
	
	public String wrapHTML(String title, String body) {
		return "<html> <head> <title> " + title + " </title> </head> <body> " + body + " </body> </html>";
	}
}
