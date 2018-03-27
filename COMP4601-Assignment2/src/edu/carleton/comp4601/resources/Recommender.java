package edu.carleton.comp4601.resources;

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

	public Recommender() {
		authorName1 = "Avery Vine";
		authorName2 = "Maxim Kuzmenko";
		name = "COMP4601 Assignment 2 Recommender System: " + authorName1 + " and " + authorName2;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getName() {
		System.out.println("name");
		return "<html> <title> " + name + " </title> <body> <h1> " + name + " </h1> </body> </html>";
	}

	@GET
	@Path("reset/{dir}")
	public Response reset(@PathParam("dir") String dir) {
		System.out.println("reset -> " + dir);
		Response res = Response.ok().build();
		Database.getInstance().clear();
		try {
			controller = new CrawlerController(dir);
			controller.crawl();
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
		String res = "<html> <title> Context </title> <body> Context </body> </html>";
		return res;
	}
	
	@GET
	@Path("community")
	@Produces(MediaType.TEXT_HTML)
	public String community() {
		System.out.println("community");
		String res = "<html> <title> Community </title> <body> Community </body> </html>";
		return res;
	}
	
	@GET
	@Path("fetch/{user}/{page}")
	@Produces(MediaType.TEXT_HTML)
	public String context(@PathParam("user") String user, @PathParam("page") String page) {
		System.out.println("fetch -> " + user + ", " + page);
		String res = "<html> <title> Fetch </title> <body> Fetch </body> </html>";
		return res;
	}
	
	@GET
	@Path("advertising/{category}")
	@Produces(MediaType.TEXT_HTML)
	public String advertising(@PathParam("category") String category) {
		System.out.println("advertising -> " + category);
		String res = "<html> <title> Advertising </title> <body> Adversiting </body> </html>";
		return res;
	}
	
	@GET
	@Path("users")
	@Produces(MediaType.TEXT_HTML)
	public String userList() {
		String res = "<html> <title> User List </title> <body> User List <table border='1'> <tr> <th> docId </th> <th> Name </th> <th> # pages visited </th> </tr> ";//</body> </html>";
		
		for (User user : Database.getInstance().getUsers()) {
			res += "<tr> <th> " + user.getDocId() + "</th> <th> " + user.getName() + "</th> <th> " + user.getWebPages().size() + "</th> </tr>";
 		}
		res += "</table> </body> </html>";
		return res;
	}

	@GET
	@Path("pages")
	@Produces(MediaType.TEXT_HTML)
	public String pageList() {
		String res = "<html> <title> Page List </title> <body> Page List <table border='1'> <tr> <th> docId </th> <th> Name </th> <th> # reviews </th> </tr> ";//</body> </html>";
		
		for (WebPage page : Database.getInstance().getWebPages()) {
			res += "<tr> <th> " + page.getDocId() + "</th> <th> " + page.getName() + "</th> <th> " + page.getUsers().size() + "</th> </tr>";
 		}
		res += "</table> </body> </html>";
		return res;
	}
}
