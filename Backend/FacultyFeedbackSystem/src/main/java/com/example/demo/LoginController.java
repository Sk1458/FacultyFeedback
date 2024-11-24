package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	@Autowired
	AdminRepository adminRepo;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500") 
	@PostMapping("/admin/validate")
    public boolean validateAdmin(@RequestBody Admin loginDetails) {
		
        // Fetch admin credentials from the database
        Admin storedAdmin = adminRepo.findById(loginDetails.getUsername()).orElse(null);

        if (storedAdmin != null) {
            // Validate the password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(loginDetails.getPassword(), storedAdmin.getPassword());
        }
        return false; 
    }
    
	
}
