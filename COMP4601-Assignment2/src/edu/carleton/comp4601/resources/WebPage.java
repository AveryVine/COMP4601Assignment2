package edu.carleton.comp4601.resources;

public class WebPage {

	int docId;
	String name;
	
	public WebPage(int docId, String name) {
		this.docId = docId;
		this.name = name;
	}
	
	public int getDocId() { 
		return docId;
	}
	
	public String getName() {
		return name;
	}
	
}
