package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "studentCredentials")
public class StudentCredentials {
	
	@Id
	@Column
    private String studentId;
	
	@Column
    private String password;
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public StudentCredentials(String studentId, String password) {
		super();
		this.studentId = studentId;
		this.password = password;
	}
	
	public StudentCredentials() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
