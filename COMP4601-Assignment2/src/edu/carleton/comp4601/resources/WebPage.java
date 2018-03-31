package edu.carleton.comp4601.resources;

import java.util.ArrayList;

public class WebPage {

	private int docId;
	private String name, url, content, genre, html;
	private ArrayList<String> users;
	
	public WebPage(int docId, String name, String url, ArrayList<String> users, String content, String html) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.users = users;
		this.content = content;
		this.genre = "";
		this.html = html;
	}
	
	public void setReviews() {
		ArrayList<User> userList = new ArrayList<User>();
		for (String user : users) {
			User currUser = Database.getInstance().getUser(user);
			if (currUser != null) {
				//Database.getInstance().set
			}
		}
	}
	
	public ArrayList<Review> getReviewsFromPageContent() {
		for (String user : users) {
			
		}
		return null;
	}
	
	public int getDocId() { 
		return docId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public ArrayList<String> getUsers() {
		return users;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getHTML() {
		return html;
	}
	
	public String htmlTableData(boolean setPrompts) {
		if (setPrompts) {
			return "<tr> <td> " + docId + " </td> <td> " + name + " </td> <td> <a onclick='parent.promptForUser(\"" + name + "\");' href='javascript:void(0);'> " + url + " </a> </td> </tr> ";
		} else {
			return "<tr> <td> " + docId + " </td> <td> " + name + " </td> <td> <a href='" + url + "'> " + url + " </a> </td> </tr> ";	
		}
	}
	
	public static String htmlTableHeader() {
		return "<tr> <th> ID </th> <th> Name </th> <th> URL </th> </tr> ";
	}
	
}
