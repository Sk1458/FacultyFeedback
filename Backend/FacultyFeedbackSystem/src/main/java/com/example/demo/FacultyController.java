package com.example.demo;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class FacultyController {
	
	Logger logger = LoggerFactory.getLogger(FacultyController.class);
	
	@Autowired
    FacultyRepository facultyRepo;
	
	@GetMapping("/admin/viewFaculty")
    public ResponseEntity<List<FacultyDTO>> viewAllFaculty() {
		
		logger.info("View Faculty Request Received from Admin");
		
		try {
	        List<FacultyData> facultyList = facultyRepo.findAll();
	        List<FacultyDTO> facultyDTOList = facultyList.stream().map(faculty -> {
	            List<String> subjectNames = faculty.getSubjects().stream()
	                .map(FacultySubject::getSubject)
	                .collect(Collectors.toList());

	            String base64Image = faculty.getImage() != null ? Base64.getEncoder().encodeToString(faculty.getImage()) : null;
	            //return new FacultyDTO(faculty.getId(), faculty.getName(), subjectNames, base64Image);
	            return new FacultyDTO(faculty.getId(), faculty.getName(), subjectNames, base64Image, faculty.getMobileNumber(), faculty.getEmail());
	        }).collect(Collectors.toList());

	        return ResponseEntity.ok(facultyDTOList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(null);
	    }
    }
	
	@PostMapping("/admin/addFaculty")
    public ResponseEntity<String> addFaculty(@RequestParam(value = "id") int id,
    										 @RequestParam(value = "name") String name,
    										 @RequestParam(value = "subjects") String subjectsJson,
    										 @Valid @RequestParam(value = "mobileNumber") String mobileNumber,
    										 @Valid @RequestParam(value = "email") String email,
    										 @RequestParam(value = "image", required = false) MultipartFile image) {
		
		logger.info("Add Faculty Request Received from Admin");

		try {
			
			if (facultyRepo.existsById(id)) {
	            return ResponseEntity.badRequest().body("Faculty with ID " + id + " already exists.");
	        }

	        // Convert JSON string to List<FacultySubject>
	        ObjectMapper objectMapper = new ObjectMapper();
	        List<FacultySubject> subjects = objectMapper.readValue(subjectsJson, new TypeReference<List<FacultySubject>>() {});

	        byte[] imageData = (image != null && !image.isEmpty()) ? image.getBytes() : null;

	        //FacultyData faculty = new FacultyData(id, name, subjects, imageData);
	        FacultyData faculty = new FacultyData(id, name, subjects, new ArrayList<>(), mobileNumber, email, imageData, null);
	        facultyRepo.save(faculty);

	        logger.info("Admin Successfully Added Faculty with ID = {}", id);
	        return ResponseEntity.ok("Faculty added successfully!");
        }
		catch (Exception e) {
			logger.error("An error occurred while adding faculty with ID = {}, Error = {}", id, e.toString());
			
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred: " + e.toString());
        }
    }
	
	@PutMapping("/admin/updateFaculty")
	public ResponseEntity<String> updateFaculty(@RequestParam int id,
	        									@RequestParam(required = false) String name,
	        									@RequestParam(required = false) String subjectsJson,
	                                            @RequestParam(required = false) String mobileNumber,
	                                            @RequestParam(required = false) String email,
	        									@RequestParam(required = false) MultipartFile image) {

		logger.warn("Update Faculty Request Received from Admin");
		
	    FacultyData faculty = facultyRepo.findById(id).orElse(null);
	    if (faculty == null) {
	        return ResponseEntity.badRequest().body("Faculty with ID " + id + " not found.");
	    }

	    try {
	        if (name != null && !name.isEmpty()) {
	            faculty.setName(name);
	        }
	        if (mobileNumber != null && !mobileNumber.isEmpty()) {
	            faculty.setMobileNumber(mobileNumber);
	        }
	        if (email != null && !email.isEmpty()) {
	            faculty.setEmail(email);
	        }
	        if (subjectsJson != null && !subjectsJson.isEmpty()) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            List<FacultySubject> updatedSubjects = objectMapper.readValue(subjectsJson, new TypeReference<List<FacultySubject>>() {});
	            faculty.setSubjects(updatedSubjects);
	        }
	        if (image != null && image.getSize() > 0) {
	            faculty.setImage(image.getBytes());
	        }

	        facultyRepo.save(faculty);
	        logger.warn("Admin Successfully Updated Faculty with ID = {}", id);
	        return ResponseEntity.ok("Faculty updated successfully!");
	    }
	    catch (Exception e) {
	    	
	    	logger.warn("An error occurred while updating faculty with ID = {}, Error = {}", id, e.toString());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}

	@DeleteMapping("/admin/deleteFaculty/{id}")
	public ResponseEntity<String> deleteFaculty(@PathVariable int id) {
		
		logger.warn("Delete Faculty Request Received from Admin");
		
		try {
		    if (facultyRepo.existsById(id)) {
		    	
		        facultyRepo.deleteById(id);
		        logger.warn("Admin Successfully deleted Faculty with ID = {}", id);
		        return ResponseEntity.ok("Faculty deleted successfully!");
		    }
		    else {

		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found with ID: " + id);
		    }
		}
		catch(Exception e) {
			
			logger.warn("An error occurred while Deleting faculty with ID = {}, Error = {}", id, e.toString());
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
		}
		
	}
	
	
	@GetMapping("/admin/viewFacultyPerformance")
	public ResponseEntity<List<FacultyPerformanceDTO>> getFacultyPerformance() {
	    logger.info("Fetching Faculty Performance Data");

	    try {
	        List<FacultyData> facultyList = facultyRepo.findAll(); // Get all faculty

	        List<FacultyPerformanceDTO> performanceList = facultyList.stream().map(faculty -> {
	            Map<String, List<Integer>> subjectScores = new HashMap<>();

	            // Assuming `faculty.getFeedbacks()` fetches all feedback entries for the faculty
	            List<FeedbackEntry> feedbacks = faculty.getFeedbacks();

	            for (FeedbackEntry feedback : feedbacks) {
	                String subject = feedback.getSubject();
	                int overallScore = calculateOverallScore(feedback);

	                subjectScores.putIfAbsent(subject, new ArrayList<>());
	                subjectScores.get(subject).add(overallScore);
	            }

	            // Calculate average performance per subject
	            Map<String, Double> subjectPerformance = subjectScores.entrySet().stream()
	                    .collect(Collectors.toMap(
	                            Map.Entry::getKey,
	                            entry -> entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0)
	                    ));

	            // Convert faculty image from byte[] to Base64 string
	            String base64Image = faculty.getImage() != null ? 
	                                 Base64.getEncoder().encodeToString(faculty.getImage()) : null;
	            
	            double overallScore = subjectPerformance.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

	            // Create DTO for this faculty
	            return new FacultyPerformanceDTO(faculty.getId(), faculty.getName(), subjectPerformance, base64Image, overallScore);
	        }).collect(Collectors.toList());

	        logger.info("Successfully calculated faculty performance data");
	        return ResponseEntity.ok(performanceList);
	    } catch (Exception e) {
	        logger.error("Error calculating faculty performance: {}", e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(null);
	    }
	}

    private int calculateOverallScore(FeedbackEntry feedback) {
        Map<String, Integer> scoreMap = Map.of(
                "very bad", 1,
                "bad", 2,
                "good", 3,
                "very good", 4
        );

        return scoreMap.get(feedback.getRegularity()) +
               scoreMap.get(feedback.getKnowledgeDepth()) +
               scoreMap.get(feedback.getCommunication()) +
               scoreMap.get(feedback.getEngagement()) +
               scoreMap.get(feedback.getExplanationQuality()) +
               scoreMap.get(feedback.getOverallPerformance());
    }
}
