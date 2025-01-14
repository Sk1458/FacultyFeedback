package com.example.demo;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
//	@Autowired
//	StanfordNLPProcessor nlpProcessor;
	
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
		        
		        // Analyzing the comments in the feedback
	            String comments = feedback.getAdditionalComments();
	            if (comments != null && !comments.isEmpty()) {
	                logger.info("Analyzing comments for feedback: {}", comments);
	                String analysisResult = StanfordNLPProcessor.processText(comments);
	                feedback.setAnalysisResult(analysisResult); // Assuming FeedbackEntry has this field
	                logger.info("NLP Analysis Result: {}", analysisResult);
	            }

		        // Added the new feedback entry to the faculty's feedback list
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
	
}
