package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
	
	private int docId;
	private String name, url, preferredGenre;
	private ArrayList<String> webpages;
	private ArrayList<Review> reviews;
	
	private HashMap<String, ArrayList<BigDecimal>> sentiments;
	private HashMap<String, BigDecimal> sentimentScores;
	
	public User(int docId, String name, String url, String preferredGenre, ArrayList<String> webpages, HashMap<String, BigDecimal> sentimentScores) {
		this.docId = docId;
		this.name = name;
		this.url = url;
		this.preferredGenre = preferredGenre;
		this.webpages = webpages;
		this.sentimentScores = sentimentScores;
		
		sentiments = new HashMap<String, ArrayList<BigDecimal>>();
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
					genreTotal = genreTotal.add(sentiment);
				}
				genreTotal = genreTotal.divide(BigDecimal.valueOf(genreSentiments.size()), MathContext.DECIMAL128);
				sentimentScores.put(genre, genreTotal);
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
	
	public HashMap<String, BigDecimal> getSentiments() {
		return sentimentScores;
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
		String html = "<tr> <td> " + docId + " </td> <td> " + name + " </td> <td> <a href='" + url + "'> " + url + " </a> </td> <td> " + preferredGenre + " </td> ";
		for (String genre : GenreAnalyzer.GENRES) {
			html += "<td> " + sentimentScores.get(genre) + " </td> ";
		}
		html += "</tr>";
		return html;
	}
	
	public static String htmlTableHeader() {
		String html = "<tr> <th> ID </th> <th> Name </th> <th> URL </th> <th> Preferred Genre </th> ";
		for (String genre : GenreAnalyzer.GENRES) {
			html += "<th> " + genre + " score </th> ";
		}
		html += "</tr>";
		return html;
	}
	
}
