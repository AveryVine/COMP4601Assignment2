package edu.carleton.comp4601.resources;

import java.util.Set;

public class User {
	
	int docId;
	String name, url;
	Set<String> webpages;
	
	public User(int docId, String name, String url, Set<String> webpages) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.webpages = webpages;
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
	
	public Set<String> getWebPages() {
		return webpages;
	}
	
}
