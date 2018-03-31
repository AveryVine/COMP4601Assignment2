package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
	
	private int docId;
	private String name, url, preferredGenre;
	private ArrayList<String> webpages;
	private ArrayList<Review> reviews;
	
	private HashMap<String, ArrayList<BigDecimal>> sentiments;
	
	public User(int docId, String name, String url, String preferredGenre, ArrayList<String> webpages, HashMap<String, ArrayList<BigDecimal>> sentiments) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.preferredGenre = preferredGenre;
		this.webpages = webpages;
		this.sentiments = sentiments;
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
	
	public void addGenreSentiment(String genre, BigDecimal sentiment) {
		ArrayList<BigDecimal> genreSentiments = sentiments.get(genre);
		if (genreSentiments == null) {
			genreSentiments = new ArrayList<BigDecimal>();
		}
		genreSentiments.add(sentiment);
		sentiments.put(genre, genreSentiments);
	}
	
	public void calculatePreferredGenre() {
		preferredGenre = null;
		BigDecimal preferredGenreSentiment = null;
		for (String genre : sentiments.keySet()) {
			BigDecimal genreTotal = BigDecimal.valueOf(0);
			ArrayList<BigDecimal> genreSentiments = sentiments.get(genre);
			if (genreSentiments.size() > 0) {
				for (BigDecimal sentiment : genreSentiments) {
					genreTotal.add(sentiment);
				}
				genreTotal = genreTotal.divide(BigDecimal.valueOf(genreSentiments.size()));
				if (preferredGenreSentiment == null || genreTotal.compareTo(preferredGenreSentiment) == 1) {
					preferredGenreSentiment = genreTotal;
					preferredGenre = genre;
				}
			}
		}
		if (preferredGenre != null) {
			System.out.println("Set " + name + "'s preferred genre to " + preferredGenre);
		} else {
			System.out.println(name + " has not reviewed any movies and cannot be assigned a preferred genre.");
		}
	}
	
	public HashMap<String, ArrayList<BigDecimal>> getSentiments() {
		return sentiments;
	}
	
	public String getPreferredGenre() {
		return preferredGenre;
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
