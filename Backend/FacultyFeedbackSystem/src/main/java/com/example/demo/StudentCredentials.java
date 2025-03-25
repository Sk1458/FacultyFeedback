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
	
	@Column
	private int semester;
	
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
	
	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}
	
	public StudentCredentials(String studentId, String password, int semester) {
		super();
		this.studentId = studentId;
		this.password = password;
		this.semester = semester;
	}
	
	public StudentCredentials() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "StudentCredentials [studentId=" + studentId + ", password=" + password + ", semester=" + semester + "]";
	}

	
}
