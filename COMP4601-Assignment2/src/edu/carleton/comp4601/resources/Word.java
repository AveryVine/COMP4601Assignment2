package edu.carleton.comp4601.resources;

public class Word implements Comparable<Word> {
	public final String w;		// Word in the corpus
	public final Integer f;		// Word frequency in the corpus
	public final Integer df;		// Word document frequency
	
	public Word(String w, Integer f, Integer df) {
		this.w = w;
		this.f = f;
		this.df = df;
	}

	public int compareTo(Word word) {
		return word.f - f;
	}

	public String toString() {
		return "[" + w + "," + f + "," + df + "]";
	}
}
