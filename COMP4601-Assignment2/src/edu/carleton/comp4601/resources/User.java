package edu.carleton.comp4601.resources;

public class User {
	
	int docId;
	String name;
	
	public User(int docId, String name) {
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
