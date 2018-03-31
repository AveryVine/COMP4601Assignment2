package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class WebPage {

	private int docId;
	private String name, url, genre, content, html;
	private HashSet<String> users;
	
	public WebPage(int docId, String name, String url, HashSet<String> users, String genre, String content, String html) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.users = users;
		this.genre = genre;
		this.content = content;
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
	
//	public ArrayList<Review> getReviewsFromPageContent() {
//		return null;
//	}
	
	public ArrayList<String> getReviewsFromPageContent() {
		
		
		
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
	
	public HashSet<String> getUsers() {
		return users;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getContent() {
		return content;
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
