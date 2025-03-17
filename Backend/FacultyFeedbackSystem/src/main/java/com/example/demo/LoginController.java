package com.example.demo;


import java.util.Collections;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	StudentCredentialsRepository studentRepo;
	
	@Autowired
	FacultyRepository facultyRepo;
	
	@Autowired
	JwtUtil jwtUtil;
	 
	@PostMapping("/admin/validate")
    public Map<String, String> validateAdmin(@RequestBody Admin loginDetails) {
        Map<String, String> response = new HashMap<>();
        Admin storedAdmin = adminRepo.findById(loginDetails.getUsername()).orElse(null);

        if (storedAdmin != null && encoder.matches(loginDetails.getPassword(), storedAdmin.getPassword())) {
            logger.info("Admin Successfully Logged In");
            String token = jwtUtil.generateToken(loginDetails.getUsername());
            response.put("token", token);
            return response;
        }

        logger.warn("Invalid Admin Credentials");
        response.put("error", "Invalid credentials");
        return response;
    }
	 
    @PostMapping("/admin/logout")
    public String logOut() {
		
        logger.info("Admin logged out successfully.");
        return "Logout successful";
    }
    
    @PostMapping("/student/validate")
    public Map<String, String> validateStudent(@RequestBody StudentCredentials loginDetails) {
        Map<String, String> response = new HashMap<>();
        StudentCredentials storedStudent = studentRepo.findById(loginDetails.getStudentId()).orElse(null);

        if (storedStudent != null && encoder.matches(loginDetails.getPassword(), storedStudent.getPassword())) {
            logger.info("Student Successfully Logged In");
            String token = jwtUtil.generateToken(loginDetails.getStudentId());
            response.put("token", token);
            return response;
        }

        logger.warn("Invalid Student Credentials");
        response.put("error", "Invalid credentials");
        return response;
    }
    
    @PostMapping("/student/logout")
    public String studentLogOut() {
		
        logger.info("Student logged out successfully.");
        return "Logout successful";
    }
    
    @PostMapping("/faculty/validate")

    public ResponseEntity<Map<String, Object>> validateFaculty(@RequestBody FacultyLoginDTO loginDetails) {

        String username = loginDetails.getUsername();
        FacultyData storedFaculty = null;

        if (username.matches("\\d+")) {
            storedFaculty = facultyRepo.findById(Integer.parseInt(username)).orElse(null);
        }
        if (storedFaculty == null) {
            storedFaculty = facultyRepo.findByMobileNumber(username).orElse(null);
        }
        if (storedFaculty == null) {
            storedFaculty = facultyRepo.findByEmail(username).orElse(null);
        }

        if (storedFaculty != null) {
            boolean match = encoder.matches(loginDetails.getPassword(), storedFaculty.getPassword());
            if (match) {
                logger.info("Faculty Successfully Logged In");

                // Send facultyId in response
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("facultyId", storedFaculty.getId());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Faculty Entered Incorrect password");
                return ResponseEntity.ok(Collections.singletonMap("success", false));
            }
        }

        logger.warn("Faculty Not Found");
        return ResponseEntity.ok(Collections.singletonMap("success", false));
    }
   
}
