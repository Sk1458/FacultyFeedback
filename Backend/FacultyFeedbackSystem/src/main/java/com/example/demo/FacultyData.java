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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
	private List<FacultySubject> subjects;
	
	@ElementCollection
    @CollectionTable(name = "facultyFeedbacks", joinColumns = @JoinColumn(name = "faculty_id"))
    private List<FeedbackEntry> feedbacks; // Collection of feedbacks
	
	@Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;
	
	@Column(unique = true)
    @Email(message = "Invalid email format")
    private String email;
	
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

	public List<FacultySubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<FacultySubject> subjects) {
		this.subjects = subjects;
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
	
	public List<FeedbackEntry> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<FeedbackEntry> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public FacultyData(int id, String name, List<FacultySubject> subjects, List<FeedbackEntry> feedbacks,
			@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits") String mobileNumber,
			@Email(message = "Invalid email format") String email, byte[] image, String base64Image) {
		super();
		this.id = id;
		this.name = name;
		this.subjects = subjects;
		this.mobileNumber = mobileNumber;
		this.email = email;
		this.image = image;
	}

//	public FacultyData(int id, String name, List<FacultySubject> subjects, byte[] image) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.subjects = subjects;
//		this.image = image;
//	}
	
	public FacultyData() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	
	
}
