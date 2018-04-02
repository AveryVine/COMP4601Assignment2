package edu.carleton.comp4601.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class GenreAnalyzer extends NaiveBayes {
	
	public static final ArrayList<String> GENRES = new ArrayList<String>(Arrays.asList("comedy", "thriller", "romance", "action", "drama"));

	/*
	 * Description: this class implements the Naive Bayes algorithm with the purpose of calculating movie genres
	 */
	public GenreAnalyzer() {
		super(GENRES);
	}
	
	/*
	 * Description: analyzes the contents of all of the movie webpages to determine their genres
	 * Input: none
	 * Return: none
	 */
	@Override
	public void analyze() {
		ArrayList<WebPage> webpages = Database.getInstance().getWebPages();
				
		for (WebPage webpage : webpages) {
			ArrayList<String> output = new ArrayList<String>();
			ArrayList<BigDecimal> scores = processText(webpage.getContent());
			int indexOfBestScore = -1;
			for (int i = 0; i < scores.size(); i++) {
				if (indexOfBestScore == -1 || scores.get(i).compareTo(scores.get(indexOfBestScore)) == 1) {
					indexOfBestScore = i;
				}
				output.add(getClasses().get(i) + " score for page " + webpage.getName() + " is " + scores.get(i).toEngineeringString());
			}
			webpage.setGenre(getClasses().get(indexOfBestScore));
			System.out.println("Set genre of " + webpage.getName() + ": " + webpage.getGenre());
			Database.getInstance().insert(webpage);			
		}
	}

}
