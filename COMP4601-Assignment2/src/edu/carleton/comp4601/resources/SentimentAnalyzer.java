package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.Arrays;

public class SentimentAnalyzer extends NaiveBayes {

	public SentimentAnalyzer() {
		super(new ArrayList<String>(Arrays.asList("positive", "negative")));
	}
	
	@Override
	public void analyze() {
		//TODO: use the processScores(text) method to analyze text for sentiment
	}

}
