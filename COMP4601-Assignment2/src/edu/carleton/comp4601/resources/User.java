package edu.carleton.comp4601.resources;

import java.util.ArrayList;

public class User {
	
	private int docId;
	private String name, url;
	private ArrayList<String> webpages;
	private ArrayList<Review> reviews;
	
	public User(int docId, String name, String url, ArrayList<String> webpages) {
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
	
	public void setReviewScore(int index, double score) {
		if (index < reviews.size() && index >= 0) {
			reviews.get(index).setScore(score);
		}
	}
	
	public void addReview(Review review) {
		reviews.add(review);
	}
	
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	public ArrayList<String> getWebPages() {
		return webpages;
	}
	
	public String htmlTableData() {
		return "<tr> <td> " + docId + " </td> <td> " + name + " </td> <td> <a href='" + url + "'> " + url + " </a> </td> </tr> ";
	}
	
	public static String htmlTableHeader() {
		return "<tr> <th> ID </th> <th> Name </th> <th> URL </th> </tr> ";
	}
	
}
