package edu.carleton.comp4601.resources;

import java.util.Set;

public class WebPage {

	int docId;
	String name, url, content;
	Set<String> users;
	
	public WebPage(int docId, String name, String url, Set<String> users, String content) {
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
	
	public Set<String> getUsers() {
		return users;
	}
	
	public String getContent() {
		return content;
	}

}
