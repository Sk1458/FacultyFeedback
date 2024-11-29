package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	 
	@PostMapping("/admin/validate")
    public boolean validateAdmin(@RequestBody Admin loginDetails) {
		

        // Fetch admin credentials from the database
        Admin storedAdmin = adminRepo.findById(loginDetails.getUsername()).orElse(null);

        if (storedAdmin != null) {
        	
            // Validate the password
            boolean match = encoder.matches(loginDetails.getPassword(), storedAdmin.getPassword()); 
            if(match) {
            	
            	logger.info("Admin Successfully Logged In");
            	return true;
            }
            else {
            	logger.warn("Admin Entered Incorrect password");
            	return false;
            }
        }
        
        logger.warn("Admin Entered Incorrect Username");
        return false; 
    }
	 
    @PostMapping("/admin/logout")
    public String logOut() {
		
        logger.info("Admin logged out successfully.");
        return "Logout successful";
    }
    
    @PostMapping("/student/validate")
    public boolean validateStudent(@RequestBody StudentCredentials loginDetails) {
    	
    	// Fetch admin credentials from the database
    	StudentCredentials storedStudent= studentRepo.findById(loginDetails.getStudentId()).orElse(null);

        if (storedStudent != null) {
        	
            // Validate the password
            boolean match = encoder.matches(loginDetails.getPassword(), storedStudent.getPassword()); 
            if(match) {
            	
            	logger.info("Student Successfully Logged In");
            	return true;
            }
            else {
            	logger.warn("Student Entered Incorrect password");
            	return false;
            }
        }
        
        logger.warn("Student Entered Incorrect Username");
        return false;	
    }
    
    @PostMapping("/student/logout")
    public String studentLogOut() {
		
        logger.info("Student logged out successfully.");
        return "Logout successful";
    }
    
}
