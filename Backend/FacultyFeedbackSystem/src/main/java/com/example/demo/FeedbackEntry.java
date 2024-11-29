package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FeedbackEntry {
	
    @Column
    private String subject;

    @Column
    private int semester;

    @Column
    private String regularity;

    @Column
    private String knowledgeDepth;

    @Column
    private String communication;

    @Column
    private String engagement;

    @Column
    private String explanationQuality;

    @Column
    private String overallPerformance;

    @Column
    private String additionalComments;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public String getRegularity() {
		return regularity;
	}

	public void setRegularity(String regularity) {
		this.regularity = regularity;
	}

	public String getKnowledgeDepth() {
		return knowledgeDepth;
	}

	public void setKnowledgeDepth(String knowledgeDepth) {
		this.knowledgeDepth = knowledgeDepth;
	}

	public String getCommunication() {
		return communication;
	}

	public void setCommunication(String communication) {
		this.communication = communication;
	}

	public String getEngagement() {
		return engagement;
	}

	public void setEngagement(String engagement) {
		this.engagement = engagement;
	}

	public String getExplanationQuality() {
		return explanationQuality;
	}

	public void setExplanationQuality(String explanationQuality) {
		this.explanationQuality = explanationQuality;
	}

	public String getOverallPerformance() {
		return overallPerformance;
	}

	public void setOverallPerformance(String overallPerformance) {
		this.overallPerformance = overallPerformance;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public FeedbackEntry(String subject, int semester, String regularity, String knowledgeDepth, String communication,
			String engagement, String explanationQuality, String overallPerformance, String additionalComments) {
		super();
		this.subject = subject;
		this.semester = semester;
		this.regularity = regularity;
		this.knowledgeDepth = knowledgeDepth;
		this.communication = communication;
		this.engagement = engagement;
		this.explanationQuality = explanationQuality;
		this.overallPerformance = overallPerformance;
		this.additionalComments = additionalComments;
	}

	public FeedbackEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

}
