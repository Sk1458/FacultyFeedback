package com.example.demo;

public class SentimentResult {
	
	private String processedText;
	private String sentiment;
	
	public String getProcessedText() {
		return processedText;
	}
	public void setProcessedText(String processedText) {
		this.processedText = processedText;
	}
	public String getSentiment() {
		return sentiment;
	}
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	public SentimentResult(String processedText, String sentiment) {
		super();
		this.processedText = processedText;
		this.sentiment = sentiment;
	}
	
	
	 
	 
}
