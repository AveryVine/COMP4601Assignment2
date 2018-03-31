package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.Arrays;

public class GenreAnalyzer extends NaiveBayes {

	public GenreAnalyzer() {
		super(new ArrayList<String>(Arrays.asList("comedy", "thriller", "romance", "action", "drama")));
	}

	@Override
	public void analyze() {
		//TODO: use the processScores(text) method to analyze text for genre
	}

}
