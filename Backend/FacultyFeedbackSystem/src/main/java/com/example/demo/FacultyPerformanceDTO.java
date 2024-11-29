package com.example.demo;

import java.util.Map;

public class FacultyPerformanceDTO {
    private int id;
    private String name;
    private Map<String, Double> performance;
    private String base64Image;
    private Double overallScore;

    public Double getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(Double overallScore) {
		this.overallScore = overallScore;
	}

	public FacultyPerformanceDTO(int id, String name, Map<String, Double> performance, String base64Image, Double overallScore) {
        this.id = id;
        this.name = name;
        this.performance = performance;
        this.base64Image = base64Image;
        this.overallScore = overallScore;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getPerformance() {
        return performance;
    }

    public void setPerformance(Map<String, Double> performance) {
        this.performance = performance;
    }
    
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
