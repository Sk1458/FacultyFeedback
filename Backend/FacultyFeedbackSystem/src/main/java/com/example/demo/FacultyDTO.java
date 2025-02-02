package com.example.demo;

import java.util.List;

public class FacultyDTO {
	
	private int id;
	private String name;
	private List<String> subjects;
	private String base64Image;
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
	public List<String> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
	public FacultyDTO(int id, String name, List<String> subjects, String base64Image) {
		super();
		this.id = id;
		this.name = name;
		this.subjects = subjects;
		this.base64Image = base64Image;
	}
	
}
