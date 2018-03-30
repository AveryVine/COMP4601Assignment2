package edu.carleton.comp4601.resources;

public class Review {
	
	private String contents, reviewer, webPage;
	private double score;
	
	public Review(String contents, String reviewer, String webPage) {
		this.contents = contents;
		this.reviewer = reviewer;
		this.webPage = webPage;
	}
	
	public String getWebpage() {
		return webPage;
	}
	
	public String getContents() {
		return contents;
	}
	
	public String getReviewer() {
		return reviewer;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
}
