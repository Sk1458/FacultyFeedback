package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StudentSetupRunner implements CommandLineRunner {

	@Autowired
    StudentCredentialsRepository studentRepo;

    @Autowired
    BCryptPasswordEncoder encoder;
    
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		String commonStudentId = "student001"; // Common Student ID
        String rawPassword = "AdityaStudent"; // Default Password
        String encryptedPassword = encoder.encode(rawPassword);

        StudentCredentials student = new StudentCredentials();
        student.setStudentId(commonStudentId);
        student.setPassword(encryptedPassword);

        studentRepo.save(student);

	}

}
