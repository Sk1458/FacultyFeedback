package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSetupRunner implements CommandLineRunner {
	
	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

//		String username = "admin117";
//        String rawPassword = "AdityaCampus";
//        String encryptedPassword = encoder.encode(rawPassword);
//        String email = "megatronsai147@gmail.com";   // 🔥 Add the email here
//
//        Admin admin = new Admin();
//        admin.setUsername(username);
//        admin.setPassword(encryptedPassword);
//        admin.setEmail(email);               // ✅ Set the email before saving
//
//        adminRepo.save(admin);               // Save with email field
        //System.out.println("Admin credentials saved successfully!");
	}

}
