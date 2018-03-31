package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
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
		//TODO: use the processText(text) method to analyze text for sentiment
		
		ArrayList<User> dbUsers = Database.getInstance().getUsers();
		HashMap<String, User> users = new HashMap<String, User>();
		for (User user : dbUsers) {
			users.put(user.getName(), user);
		}
		
		for (WebPage webPage : Database.getInstance().getWebPages()) {
			HashMap<String, String> reviews = getReviewsFromPage(webPage);
			
			for (Entry<String, String> entry : reviews.entrySet()) {
				ArrayList<BigDecimal> scores = processText(entry.getValue());
				
				BigDecimal positiveScore = scores.get(0);
				BigDecimal negativeScore = scores.get(1);
				BigDecimal finalScore;
				if (positiveScore.compareTo(negativeScore) == 1) {
					finalScore = positiveScore.divide(negativeScore);
				}
				else {
					finalScore = negativeScore.divide(positiveScore).multiply(BigDecimal.valueOf(-1));
				}
								
				User user = users.get(entry.getKey());
				user.addGenreSentiment(webPage.getGenre(), finalScore);
			}
		}
		for (User user : users.values()) {
			user.calculatePreferredGenre();
			Database.getInstance().insert(user);
		}
	}
	
	public HashMap<String, String> getReviewsFromPage(WebPage webPage) {
		HashMap<String, String> reviews = new HashMap<String, String>();
		HashSet<String> usernames = webPage.getUsers();
		ArrayList<String> wordsInPage = new ArrayList<String>(Arrays.asList(webPage.getContent().split(" ")));
		int startIndex = -1;
		for (int i = 0; i < wordsInPage.size(); i++) {
			if (usernames.contains(wordsInPage.get(i))) {
				if (startIndex == -1) {
					startIndex = i;
				}
				else {
					String reviewContent = "";
					for (int x = startIndex + 1; x < i; x++) {
						reviewContent += wordsInPage.get(i);
					}
					reviews.put(wordsInPage.get(startIndex), reviewContent);
					startIndex = -1;
				}
			}
		}
		return reviews;
	}

}
