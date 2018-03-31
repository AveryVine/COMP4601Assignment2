package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.Arrays;

public class GenreAnalyzer extends NaiveBayes {

	public GenreAnalyzer() {
		super(new ArrayList<String>(Arrays.asList("comedy", "thriller", "romance", "action", "drama")));
	}

	@Override
	public void analyze() {
		ArrayList<WebPage> webpages = Database.getInstance().getWebPages();
		
		for (WebPage webpage : webpages) {
			ArrayList<Double> scores = processText(webpage.getContent());
			int indexOfBestScore = -1;
			for (int i = 0; i < scores.size(); i++) {
				if (indexOfBestScore == -1 || scores.get(i) > scores.get(indexOfBestScore)) {
					indexOfBestScore = i;
				}
			}
			webpage.setGenre(getClasses().get(indexOfBestScore));
		}
	}

}
