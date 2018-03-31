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
		for (WebPage webPage : Database.getInstance().getWebPages()) {
			HashMap<String, String> reviews = getReviewsFromPage(webPage);
			
			for (Entry<String, String> entry : reviews.entrySet()) {
				ArrayList<BigDecimal> scores = processText(entry.getValue());
				
			}
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
