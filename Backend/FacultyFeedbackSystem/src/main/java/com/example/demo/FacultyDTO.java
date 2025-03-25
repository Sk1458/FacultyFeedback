package com.example.demo;

import java.util.List;

public class FacultyDTO {
	
	private int id;
	private String name;
	private List<FacultySubject> subjects;
	private String base64Image;
	private String mobileNumber;
	private String email;
	private String campusCode;
	
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
	public List<FacultySubject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<FacultySubject> subjects) {
		this.subjects = subjects;
	}
	public String getBase64Image() {
		return base64Image;
	}
	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCampusCode() {
		return campusCode;
	}
	public void setCampusCode(String campusCode) {
		this.campusCode = campusCode;
	}
	public FacultyDTO(int id, String name, List<FacultySubject> subjects, String base64Image, String mobileNumber,
			String email, String campusCode) {
		super();
		this.id = id;
		this.name = name;
		this.subjects = subjects;
		this.base64Image = base64Image;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.campusCode = campusCode;
	}
	
	
}
