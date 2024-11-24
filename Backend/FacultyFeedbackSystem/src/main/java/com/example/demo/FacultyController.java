package com.example.demo;

import java.util.Base64;
import java.util.List;

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

@RestController
public class FacultyController {
	
	@Autowired
    FacultyRepository facultyRepo;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/admin/viewFaculty")
    public ResponseEntity<List<FacultyData>> viewAllFaculty() {
        try {
            List<FacultyData> facultyList = facultyRepo.findAll(); // Retrieve all faculty data
            for (FacultyData faculty : facultyList) {
                if (faculty.getImage() != null) {
                    // Convert the byte array to a base64 string and set it on the FacultyData object
                    String base64Image = Base64.getEncoder().encodeToString(faculty.getImage());
                    faculty.setBase64Image(base64Image);
                }
            }
            return ResponseEntity.ok(facultyList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // Error handling
        }
    }
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PostMapping("/admin/addFaculty")
    public ResponseEntity<String> addFaculty(@RequestParam int id,
    										 @RequestParam String name,
    										 @RequestParam List<String> subjects,
    										 @RequestParam(required = false) MultipartFile image) {

		try {
            
            if (facultyRepo.existsById(id)) {
                return ResponseEntity.badRequest().body("Faculty with ID " + id + " already exists.");
            }

            
            if (image != null && image.getSize() > 2 * 1024 * 1024) { // Limit to 2 MB
                return ResponseEntity.badRequest().body("Image size exceeds 2 MB.");
            }

            
            byte[] imageData = image != null ? image.getBytes() : null;

            
            FacultyData faculty = new FacultyData(id, name, subjects, imageData);
            facultyRepo.save(faculty);

            return ResponseEntity.ok("Faculty added successfully!");
        }
		catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred: " + e.toString());
        }
    }
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PutMapping("/admin/updateFaculty")
	public ResponseEntity<String> updateFaculty(@RequestParam int id,
	        									@RequestParam(required = false) String name,
	        									@RequestParam(required = false) List<String> addSubjects,
	        									@RequestParam(required = false) List<String> removeSubjects,
	        									@RequestParam(required = false) MultipartFile image) {

	    FacultyData faculty = facultyRepo.findById(id).orElse(null);
	    if (faculty == null) {
	        return ResponseEntity.badRequest().body("Faculty with ID " + id + " not found.");
	    }

	    try {
	        if (name != null && !name.isEmpty()) {
	            faculty.setName(name);
	        }
	        if (addSubjects != null && !addSubjects.isEmpty()) {
	            faculty.getSubjects().addAll(addSubjects);
	        }
	        if (removeSubjects != null && !removeSubjects.isEmpty()) {
	            faculty.getSubjects().removeAll(removeSubjects);
	        }
	        if (image != null && image.getSize() > 0) {
	            faculty.setImage(image.getBytes());
	        }

	        facultyRepo.save(faculty);
	        return ResponseEntity.ok("Faculty updated successfully!");
	    }
	    catch (Exception e) {
	    	
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
	    }
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@DeleteMapping("/admin/deleteFaculty/{id}")
	public ResponseEntity<String> deleteFaculty(@PathVariable int id) {
		
	    if (facultyRepo.existsById(id)) {
	    	
	        facultyRepo.deleteById(id);
	        return ResponseEntity.ok("Faculty deleted successfully!");
	    }
	    else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found with ID: " + id);
	    }
	}
}
