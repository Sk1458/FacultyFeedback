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
    private String regularitySentiment;

    @Column
    private String knowledgeDepth;
    @Column
    private String knowledgeDepthSentiment;

    @Column
    private String communication;
    @Column
    private String communicationSentiment;

    @Column
    private String engagement;
    @Column
    private String engagementSentiment;

    @Column
    private String explanationQuality;
    @Column
    private String explanationQualitySentiment;

    @Column
    private String overallPerformance;
    @Column
    private String overallPerformanceSentiment;

    @Column
    private String additionalComments;
    @Column
    private String additionalCommentsSentiment;

    
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
	
	
	public String getRegularitySentiment() {
		return regularitySentiment;
	}

	public void setRegularitySentiment(String regularitySentiment) {
		this.regularitySentiment = regularitySentiment;
	}

	public String getKnowledgeDepthSentiment() {
		return knowledgeDepthSentiment;
	}

	public void setKnowledgeDepthSentiment(String knowledgeDepthSentiment) {
		this.knowledgeDepthSentiment = knowledgeDepthSentiment;
	}

	public String getCommunicationSentiment() {
		return communicationSentiment;
	}

	public void setCommunicationSentiment(String communicationSentiment) {
		this.communicationSentiment = communicationSentiment;
	}

	public String getEngagementSentiment() {
		return engagementSentiment;
	}

	public void setEngagementSentiment(String engagementSentiment) {
		this.engagementSentiment = engagementSentiment;
	}

	public String getExplanationQualitySentiment() {
		return explanationQualitySentiment;
	}

	public void setExplanationQualitySentiment(String explanationQualitySentiment) {
		this.explanationQualitySentiment = explanationQualitySentiment;
	}

	public String getOverallPerformanceSentiment() {
		return overallPerformanceSentiment;
	}

	public void setOverallPerformanceSentiment(String overallPerformanceSentiment) {
		this.overallPerformanceSentiment = overallPerformanceSentiment;
	}

	public String getAdditionalCommentsSentiment() {
		return additionalCommentsSentiment;
	}

	public void setAdditionalCommentsSentiment(String additionalCommentsSentiment) {
		this.additionalCommentsSentiment = additionalCommentsSentiment;
	}

	public FeedbackEntry(String subject, int semester, String regularity, String regularitySentiment,
			String knowledgeDepth, String knowledgeDepthSentiment, String communication, String communicationSentiment,
			String engagement, String engagementSentiment, String explanationQuality,
			String explanationQualitySentiment, String overallPerformance, String overallPerformanceSentiment,
			String additionalComments, String additionalCommentsSentiment) {
		super();
		this.subject = subject;
		this.semester = semester;
		this.regularity = regularity;
		this.regularitySentiment = regularitySentiment;
		this.knowledgeDepth = knowledgeDepth;
		this.knowledgeDepthSentiment = knowledgeDepthSentiment;
		this.communication = communication;
		this.communicationSentiment = communicationSentiment;
		this.engagement = engagement;
		this.engagementSentiment = engagementSentiment;
		this.explanationQuality = explanationQuality;
		this.explanationQualitySentiment = explanationQualitySentiment;
		this.overallPerformance = overallPerformance;
		this.overallPerformanceSentiment = overallPerformanceSentiment;
		this.additionalComments = additionalComments;
		this.additionalCommentsSentiment = additionalCommentsSentiment;
	}
	
	public FeedbackEntry() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
