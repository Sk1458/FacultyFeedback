package com.example.demo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
	             
	             credentials.add(new StudentCredentials(studentId, encryptedPassword));
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

	         List<String> filteredRollNumbers = students.stream()
	                 .map(StudentCredentials::getStudentId)
	                 .filter(roll -> {
	                     boolean matches = true;

	                     // Apply filters only if they are provided and not "all"
	                     if (year != null && !year.isEmpty() && !year.equalsIgnoreCase("all")) {
	                         matches &= roll.startsWith(year);
	                     }
	                     if (campus != null && !campus.isEmpty() && !campus.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(2, 4).equals(campus);
	                     }

	                     // Handle entry type correctly when "All" is selected
	                     if (entryType != null && !entryType.isEmpty() && !entryType.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(4, 6).equals(entryType);
	                     }

	                     // If "All" is selected in entryType, include both 1A and 5A
	                     if (entryType != null && entryType.equalsIgnoreCase("all")) {
	                         matches &= (roll.substring(4, 6).equals("1A") || roll.substring(4, 6).equals("5A"));
	                     }

	                     if (branch != null && !branch.isEmpty() && !branch.equalsIgnoreCase("all")) {
	                         matches &= roll.substring(6, 8).equals(branch);
	                     }

	                     return matches;
	                 })
	                 .collect(Collectors.toList());

	         return ResponseEntity.ok(filteredRollNumbers);

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

	
}
