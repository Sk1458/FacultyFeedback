package com.example.demo;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;


@Table(name = "facultyInfo")
@Entity
public class FacultyData {

	@Id
	@Column
	private int id;
	
	@Column
	private String name;
	
	@ElementCollection
    @CollectionTable(name = "facultySubjects", joinColumns = @JoinColumn(name = "faculty_id"))
    @Column(name = "subject")
	private List<String> subjects;
	
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] image;
	
	@Transient
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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public FacultyData(int id, String name, List<String> subjects, byte[] image) {
		super();
		this.id = id;
		this.name = name;
		this.subjects = subjects;
		this.image = image;
	}

	public FacultyData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
