package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class SentimentAnalyzer extends NaiveBayes {

	public SentimentAnalyzer() {
		super(new ArrayList<String>(Arrays.asList("positive", "negative")));
	}
	
	@Override
	public void analyze() {
		System.out.println("Retrieving users from database...");
		ArrayList<User> dbUsers = Database.getInstance().getUsers();
		HashMap<String, User> users = new HashMap<String, User>();
		for (User user : dbUsers) {
			users.put(user.getName(), user);
		}
		
		System.out.println("Retrieving webpages from database...");
		ArrayList<WebPage> webpages = Database.getInstance().getWebPages();
		for (WebPage webpage : webpages) {
			System.out.println("Retrieving reviews from webpage...");
			HashMap<String, String> reviews = getReviewsFromPage(webpage);
			
			for (Entry<String, String> entry : reviews.entrySet()) {
				System.out.println("Processing score for review...");
				ArrayList<BigDecimal> scores = processText(entry.getValue());
				
				BigDecimal positiveScore = scores.get(0);
				BigDecimal negativeScore = scores.get(1);
				BigDecimal finalScore;
				if (positiveScore.compareTo(negativeScore) == 1) {
					finalScore = positiveScore.divide(negativeScore, MathContext.DECIMAL128);
					System.out.println(finalScore);
				}
				else {
					finalScore = negativeScore.divide(positiveScore, MathContext.DECIMAL128).multiply(BigDecimal.valueOf(-1));
					System.out.println(finalScore);
				}
				System.out.println("Adding sentiment to user...");
				User user = users.get(entry.getKey());
				user.addGenreSentiment(webpage.getGenre(), finalScore);
			}
		}
		System.out.println("Updating user preferences...");
		for (User user : users.values()) {
			user.calculatePreferredGenre();
			Database.getInstance().insert(user);
		}
	}
	
	public HashMap<String, String> getReviewsFromPage(WebPage webPage) {
		HashMap<String, String> reviews = new HashMap<String, String>();
		HashSet<String> usernames = webPage.getUsers();
		ArrayList<String> wordsInPage = new ArrayList<String>(Arrays.asList(webPage.getContent().split(" ")));
		String currentReviewer = null;
		String currentReview = null;
		for (String word : wordsInPage) {
			if (usernames.contains(word)) {
				if (currentReviewer != null) {
					reviews.put(currentReviewer, currentReview);
				}
				currentReviewer = word;
				currentReview = "";
			} else {
				currentReview += word + " ";
			}
		}
		if (currentReviewer != null) {
			reviews.put(currentReviewer, currentReview);
		}
		return reviews;
	}

}
