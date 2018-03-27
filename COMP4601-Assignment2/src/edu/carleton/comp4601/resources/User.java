package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

import edu.uci.ics.crawler4j.url.WebURL;

public class User extends Document {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9022027290860912051L;
	int docId;
	String name;
	Set<String> pagesVisited;
	
	public User(int docId, String name, Set<WebURL> pagesVisitedAsWebURLs) {
		this.docId = docId;
		put("docId", docId);
		this.name = name;
		put("name", name);
		pagesVisited = new HashSet<String>();
		for (WebURL url : pagesVisitedAsWebURLs) {
			pagesVisited.add(url.getAnchor());
		}
		put("pagesVisited", pagesVisited);
	}

	@SuppressWarnings("unchecked")
	public User(Document doc) {
		this.docId = doc.getInteger("docId");
		this.name = doc.getString("name");
		//this.pagesVisited = new HashSet<String>((ArrayList<String>) doc.get("pagesVisited"));
	}
	
	public int getDocId() { 
		return docId;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getPagesVisited() {
		return pagesVisited;
	}
	
}
