package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;

public class UserGenreSentiment {
	
	private HashMap<String, BigDecimal> sentiments;

	public UserGenreSentiment() {
		sentiments = new HashMap<String, BigDecimal>();
	}
	
	public void add(String webpage, BigDecimal sentiment) {
		sentiments.put(webpage, sentiment);
	}
	
	public boolean contains(String webpage) {
		return sentiments.containsKey(webpage);
	}
	
	public BigDecimal get(String webpage) {
		BigDecimal sentiment = sentiments.get(webpage);
		if (sentiment != null) {
			return sentiment;
		}
		return null;
	}
	
	public BigDecimal calculateGenreScore() {
		BigDecimal total = BigDecimal.valueOf(0);
		for (BigDecimal sentiment : sentiments.values()) {
			total = total.add(sentiment);
		}
		total = total.divide(BigDecimal.valueOf(sentiments.size()), MathContext.DECIMAL128);
		return total;
	}
	
}
