package edu.carleton.comp4601.resources;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

import edu.uci.ics.crawler4j.url.WebURL;

public class WebPage extends Document {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1475004018201182320L;
	int docId;
	String name;
	Set<String> reviewers; 
	
	public WebPage(int docId, String name, Set<WebURL> links) {
		this.docId = docId;
		put("docId", docId);
		this.name = name;
		put("name", name);
		this.reviewers = getReviewersFromLinks(links);
		put("reviewers", reviewers);
	}
	
	public WebPage(Document doc) {
		this.docId = doc.getInteger("docId");
		this.name = doc.getString("name");
		this.reviewers = (Set<String>) doc.get("reviewers");
	}
	
	public int getDocId() { 
		return docId;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getReviewers() {
		return reviewers;
	}
	
	//Assuming we add users to database before (we should do that; webpage filtering is dependent on users existing but users are independent)
	public Set<String> getReviewersFromLinks(Set<WebURL> links) {
		Set<String> validReviewers = new HashSet<String>(); 
		for (WebURL link : links) {
			String user = link.getAnchor();
			if (Database.getInstance().userExists(user)) { //if user text is in database then confirm as real user
				validReviewers.add(user);
			}
		}
		
		return validReviewers;
	}

}
