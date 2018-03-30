package edu.carleton.comp4601.resources;

import java.util.ArrayList;
import java.util.Arrays;

public class ContentClass {

	private int currWord;
	private ArrayList<String> words;
	
	public ContentClass(String content) {
		currWord = 0;
		content = clean(content);
		words = new ArrayList<String>(Arrays.asList(content.split(" ")));
	}
	
	private String clean(String content) {
		content = content.replaceAll("\n", " ");
		content.trim();
		return content;
	}
	
	public boolean hasNext() {
		if (currWord >= words.size()) {
			return false;
		}
		return true;
	}
	
	public String next() {
		String word = words.get(currWord);
		currWord++;
		return word;
	}
	
}
