package edu.carleton.comp4601.resources;

import java.util.ArrayList;

public class WebPage {

	int docId;
	String name, url, content;
	ArrayList<String> users;
	
	public WebPage(int docId, String name, String url, ArrayList<String> users, String content) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.users = users;
		this.content = content;
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

}
