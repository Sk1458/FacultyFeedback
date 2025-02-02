package com.example.demo;

import jakarta.persistence.Embeddable;

@Embeddable
public class FacultySubject {
	
	private String subject;
	private Integer semester;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getSemester() {
		return semester;
	}
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	public FacultySubject(String subject, Integer semester) {
		super();
		this.subject = subject;
		this.semester = semester;
	}
	public FacultySubject() {
		super();
		// TODO Auto-generated constructor stub
	}
		    
}
