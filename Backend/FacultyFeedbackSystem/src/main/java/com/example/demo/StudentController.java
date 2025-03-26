package com.example.demo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class StudentController {
	
	@Autowired
	FacultyRepository facultyRepo;
	
	@Autowired
	StudentCredentialsRepository studentCredentialsRepo;
	
	Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	//For the feedback form
	@GetMapping("/student/viewFaculty/{facultyId}")
	public ResponseEntity<FacultyData> getFacultyById(@PathVariable("facultyId") int facultyId) {
    
		logger.info("View Faculty Request Received from Student for Faculty ID = {}", facultyId);
    
		try {
    	
			FacultyData faculty = facultyRepo.findById(facultyId).orElse(null);
			if (faculty != null) {
            // Convert image to base64 if it exists
	
				if (faculty.getImage() != null) {
				String base64Image = Base64.getEncoder().encodeToString(faculty.getImage());
				faculty.setBase64Image(base64Image);  // Assuming you have this method in your FacultyData model
				}
	    
				logger.info("Faculty details successfully retrieved for ID = {}", facultyId);
				System.out.println("Faculty Subjects: {}" + faculty.getSubjects());
				return ResponseEntity.ok(faculty);
			}
			else {
    	
				logger.warn("Faculty with ID = {} not found", facultyId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		}
		catch (Exception e) {
    	
	       logger.error("An error occurred while viewing faculty details for ID = {}, Error = {}", facultyId, e.toString());
	       e.printStackTrace();
	       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	 }
	 
	@GetMapping("/student/faculty-list")
	public ResponseEntity<?> getFacultyList(@RequestParam String rollNumber) {

	    if (rollNumber == null || rollNumber.isEmpty()) {
	        return ResponseEntity.badRequest().body("Roll number is required.");
	    }

	    // ✅ Fetch student credentials by roll number
	    Optional<StudentCredentials> studentOpt = studentCredentialsRepo.findById(rollNumber);
	    if (studentOpt.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
	    }

	    StudentCredentials student = studentOpt.get();
	    Integer semester = student.getSemester();

	    // ✅ Extract campus code from roll number
	    String campusCode = rollNumber.substring(2, 4).toUpperCase();  // "MH", "P3", "A9"

	    // ✅ Fetch faculty by campus code and semester
	    List<FacultyData> facultyList = facultyRepo.findByCampusCodeAndSubjectsSemester(campusCode, semester);

	    if (facultyList.isEmpty()) {
	        return ResponseEntity.ok(Collections.singletonMap("message", "No faculty found for this campus and semester."));
	    }

	    // ✅ Prepare the response
	    List<Map<String, Object>> facultyResponse = new ArrayList<>();
	    for (FacultyData faculty : facultyList) {
	        Map<String, Object> facultyMap = new HashMap<>();
	        facultyMap.put("id", faculty.getId());
	        facultyMap.put("name", faculty.getName());
	        facultyMap.put("email", faculty.getEmail());
	        facultyMap.put("mobile", faculty.getMobileNumber());
	        facultyMap.put("campusCode", faculty.getCampusCode());

	        // ✅ Include the Base64 image in the response
	        if (faculty.getImage() != null) {
	            facultyMap.put("image", faculty.getImage()); 
	        } else {
	            facultyMap.put("image", null); 
	        }

	        // ✅ Include subject-semester pairs
	        List<Map<String, Object>> subjectSemesterPairs = faculty.getSubjects().stream()
	            .filter(sub -> sub.getSemester().equals(semester))
	            .map(sub -> {
	                Map<String, Object> map = new HashMap<>();
	                map.put("subject", sub.getSubject());
	                map.put("semester", sub.getSemester());
	                return map;
	            })
	            .collect(Collectors.toList());

	        facultyMap.put("subjects", subjectSemesterPairs);
	        facultyResponse.add(facultyMap);
	    }

	    return ResponseEntity.ok(facultyResponse);

	}
	
	// ✅ New endpoint to get student details by roll number
	@GetMapping("/student/getStudentDetails")
	public ResponseEntity<?> getStudentDetails(@RequestParam String rollNumber) {
	    try {
	        Optional<StudentCredentials> studentOpt = studentCredentialsRepo.findById(rollNumber);

	        if (studentOpt.isPresent()) {
	            StudentCredentials student = studentOpt.get();

	            // ✅ Extract the campus code from the roll number itself
	            String campusCode = rollNumber.substring(2, 4);  

	            // ✅ Prepare the response with rollNumber, semester, and extracted campus code
	            Map<String, Object> response = new HashMap<>();
	            response.put("rollNumber", student.getStudentId());
	            response.put("semester", student.getSemester());
	            response.put("campusCode", campusCode);  // Extracted from roll number

	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error fetching student details: " + e.getMessage());
	    }
	}


	
	 @PostMapping("/student/submitFeedback")
	 public ResponseEntity<String> submitFeedback(@RequestBody FeedbackEntry feedback, @RequestParam int facultyId) {
		 logger.info("Feedback submission request received for Faculty ID = {}", facultyId);

		    try {
		        FacultyData faculty = facultyRepo.findById(facultyId).orElse(null);

		        if (faculty == null) {
		            logger.warn("Faculty with ID = {} not found", facultyId);
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found");
		        }

		        // Process feedback with NLP
		        SentimentResult regularity = StanfordNLPProcessor.processText(feedback.getRegularity());
		        SentimentResult knowledgeDepth = StanfordNLPProcessor.processText(feedback.getKnowledgeDepth());
		        SentimentResult communication = StanfordNLPProcessor.processText(feedback.getCommunication());
		        SentimentResult engagement = StanfordNLPProcessor.processText(feedback.getEngagement());
		        SentimentResult explanationQuality = StanfordNLPProcessor.processText(feedback.getExplanationQuality());
		        SentimentResult overallPerformance = StanfordNLPProcessor.processText(feedback.getOverallPerformance());
		        SentimentResult additionalComments = StanfordNLPProcessor.processText(feedback.getAdditionalComments());

		        // Store updated values in feedback entry
		        feedback.setRegularity(regularity.getProcessedText());
		        feedback.setRegularitySentiment(regularity.getSentiment());

		        feedback.setKnowledgeDepth(knowledgeDepth.getProcessedText());
		        feedback.setKnowledgeDepthSentiment(knowledgeDepth.getSentiment());

		        feedback.setCommunication(communication.getProcessedText());
		        feedback.setCommunicationSentiment(communication.getSentiment());

		        feedback.setEngagement(engagement.getProcessedText());
		        feedback.setEngagementSentiment(engagement.getSentiment());

		        feedback.setExplanationQuality(explanationQuality.getProcessedText());
		        feedback.setExplanationQualitySentiment(explanationQuality.getSentiment());

		        feedback.setOverallPerformance(overallPerformance.getProcessedText());
		        feedback.setOverallPerformanceSentiment(overallPerformance.getSentiment());

		        feedback.setAdditionalComments(additionalComments.getProcessedText());
		        feedback.setAdditionalCommentsSentiment(additionalComments.getSentiment());

		        // Save the feedback
		        faculty.getFeedbacks().add(feedback);
		        facultyRepo.save(faculty);

		        logger.info("Feedback successfully added for Faculty ID = {}", facultyId);
		        return ResponseEntity.ok("Feedback submitted successfully!");
		    } 
		    catch (Exception e) {
		        logger.error("An error occurred while submitting feedback: {}", e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit feedback");
		    }
	 }
	 
	 @PostMapping("/admin/generateStudentCredentials")
	 public ResponseEntity<String> generateStudentCredentials(
	         @RequestParam String year,
	         @RequestParam String campus,
	         @RequestParam String entryType,
	         @RequestParam String branch,
	         @RequestParam int count) {

	     List<StudentCredentials> credentials = new ArrayList<>();
	     int addedCount = 0;
	     int skippedCount = 0;

	     // BCrypt encoder for hashing the password
	     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	     String plainPassword = "AdityaStudent";

	     for (int i = 0; i < count; i++) {
	         String rollNumber = year + campus + entryType + branch;

	         String suffix;
	         if (i < 99) {
	             suffix = String.format("%02d", i + 1);  // 01 to 99
	         } else {
	             char letter = (char) ('A' + (i - 99) / 10);  // A-Z series
	             suffix = letter + String.valueOf((i - 99) % 10);
	         }

	         String studentId = rollNumber + suffix;

	         // Check if the roll number already exists
	         if (!studentCredentialsRepo.existsById(studentId)) {
	             // Encrypt the password before saving
	             String encryptedPassword = passwordEncoder.encode(plainPassword);
	             
	             // ✅ Use the new constructor with the semester field
	             int defaultSemester = 1;  // Assigning Semester 1 to new students by default
	             credentials.add(new StudentCredentials(studentId, encryptedPassword, defaultSemester));
	             
	             addedCount++;
	         } else {
	             skippedCount++;
	         }
	     }

	     // Save only new credentials
	     if (!credentials.isEmpty()) {
	         studentCredentialsRepo.saveAll(credentials);
	     }

	     // Return the summary
	     String message = String.format("Total: %d, Added: %d, Skipped: %d (duplicates)", count, addedCount, skippedCount);
	     return ResponseEntity.ok(message);
	 }

	 
	 @GetMapping("/admin/viewStudentCredentials")
	 public ResponseEntity<List<String>> viewStudentCredentials(
	         @RequestParam(required = false) String year,
	         @RequestParam(required = false) String campus,
	         @RequestParam(required = false) String entryType,
	         @RequestParam(required = false) String branch) {

	     try {
	         List<StudentCredentials> students = studentCredentialsRepo.findAll();

	         // ✅ Filter students based on the given filters
	         List<String> filteredResults = students.stream()
	                 .filter(student -> {
	                     String roll = student.getStudentId();
	                     boolean matches = true;

	                     if (year != null && !year.isEmpty() && !year.equalsIgnoreCase("all")) {
	                         matches &= roll.startsWith(year);
	                     }
	                     if (campus != null && !campus.isEmpty() && !campus.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(2, 4).equals(campus);
	                     }
	                     if (entryType != null && !entryType.isEmpty() && !entryType.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(4, 6).equals(entryType);
	                     }
	                     if (entryType != null && entryType.equalsIgnoreCase("all")) {
	                         matches &= (roll.substring(4, 6).equals("1A") || roll.substring(4, 6).equals("5A"));
	                     }
	                     if (branch != null && !branch.isEmpty() && !branch.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(6, 8).equals(branch);
	                     }

	                     return matches;
	                 })
	                 .map(student -> student.getStudentId() + " (Semester: " + student.getSemester() + ")")  // 🛑 Append semester
	                 .collect(Collectors.toList());

	         return ResponseEntity.ok(filteredResults);

	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	     }
	 }

 
	 // Delete a single student credential
	 @DeleteMapping("/admin/deleteStudentCredential/{studentId}")
	 public ResponseEntity<String> deleteStudentCredential(@PathVariable String studentId) {
	     try {
	         if (studentCredentialsRepo.existsById(studentId)) {
	             studentCredentialsRepo.deleteById(studentId);
	             return ResponseEntity.ok("Student credential deleted successfully: " + studentId);
	         } else {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student credential not found.");
	         }
	     } catch (Exception e) {
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting student credential.");
	     }
	 }

	// Delete all filtered student credentials
	 @DeleteMapping("/admin/deleteAllStudentCredentials")
	 public ResponseEntity<String> deleteAllFilteredCredentials(
	         @RequestParam(required = false) String year,
	         @RequestParam(required = false) String campus,
	         @RequestParam(required = false) String entryType,
	         @RequestParam(required = false) String branch) {

	     try {
	         // Log the received filter parameters for debugging
	         System.out.println("Filter Params - Year: " + year + ", Campus: " + campus + ", Entry: " + entryType + ", Branch: " + branch);

	         // Fetch all credentials
	         List<StudentCredentials> students = studentCredentialsRepo.findAll();

	         // Apply filters with null-checks
	         List<StudentCredentials> filtered = students.stream()
	                 .filter(s -> (year == null || year.isEmpty() || s.getStudentId().startsWith(year)) &&
	                              (campus == null || campus.isEmpty() || s.getStudentId().substring(2, 4).equals(campus)) &&
	                              (entryType == null || entryType.isEmpty() || s.getStudentId().substring(4, 6).equals(entryType)) &&
	                              (branch == null || branch.isEmpty() || s.getStudentId().substring(6, 8).equals(branch)))
	                 .collect(Collectors.toList());

	         // Log the number of credentials to be deleted
	         System.out.println("Filtered Credentials Count: " + filtered.size());

	         if (filtered.isEmpty()) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching credentials found to delete.");
	         }

	         // Delete all filtered credentials
	         studentCredentialsRepo.deleteAll(filtered);

	         return ResponseEntity.ok("Deleted " + filtered.size() + " filtered student credentials.");

	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting filtered credentials.");
	     }
	 }
	 
	// ✅ Bulk semester update endpoint
	 @PutMapping("/admin/updateStudentSemester")
	 public ResponseEntity<String> updateStudentSemester(
	         @RequestParam String year,      // Batch year code (e.g., "21")
	         @RequestParam int semester) {   // New semester value

	     try {
	         // Fetch all students with the specified year code
	         List<StudentCredentials> students = studentCredentialsRepo.findAll().stream()
	                 .filter(s -> s.getStudentId().startsWith(year))
	                 .collect(Collectors.toList());

	         // Check if any students match the filter
	         if (students.isEmpty()) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                     .body("No students found for year: " + year);
	         }

	         // Update the semester for all matching students
	         for (StudentCredentials student : students) {
	             student.setSemester(semester);
	         }

	         // Save the updated students
	         studentCredentialsRepo.saveAll(students);

	         // Return success message
	         return ResponseEntity.ok("Updated " + students.size() + " students to semester " + semester);

	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .body("Error updating student semesters.");
	     }
	 }
	 
	 @PutMapping("/admin/updateStudentSemesterByRollNumber")
	 public ResponseEntity<String> updateStudentSemesterByRollNumber(
	         @RequestParam String rollNumber,
	         @RequestParam int semester) {

	     try {
	         // Check if the student with the given roll number exists
	         Optional<StudentCredentials> studentOpt = studentCredentialsRepo.findById(rollNumber);

	         if (studentOpt.isPresent()) {
	             StudentCredentials student = studentOpt.get();
	             
	             // Update the semester
	             student.setSemester(semester);
	             studentCredentialsRepo.save(student);

	             return ResponseEntity.ok("Semester updated to " + semester + " for student: " + rollNumber);
	         } else {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with roll number " + rollNumber + " not found.");
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .body("Failed to update semester for student: " + rollNumber);
	     }
	 }

	 
}
