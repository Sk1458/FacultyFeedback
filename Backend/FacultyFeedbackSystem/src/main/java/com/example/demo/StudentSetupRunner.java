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
		
		String prefix = "21MH1A05";
		String commonStudentId = "student001"; // Common Student ID
        String rawPassword = "AdityaStudent"; // Default Password
        String encryptedPassword = encoder.encode(rawPassword);
        
        // Generate roll numbers from 21MH1A0572 to 21MH1A0599
        for (int i = 72; i <= 99; i++) {
            createStudentIfNotExists(prefix + i, encryptedPassword);
        }

        // Generate roll numbers for the A, B, C, D, and E series
        char[] series = {'A', 'B', 'C', 'D', 'E'};
        for (char seriesChar : series) {
            int limit = (seriesChar == 'E') ? 1 : 9; // E series ends at E1
            for (int i = 0; i <= limit; i++) {
                createStudentIfNotExists(prefix + seriesChar + i, encryptedPassword);
            }
        }

	}

	private void createStudentIfNotExists(String studentId, String encryptedPassword) {
		// TODO Auto-generated method stub
		
		if (!studentRepo.existsByStudentId(studentId)) {
			
            StudentCredentials student = new StudentCredentials();
            student.setStudentId(studentId);
            student.setPassword(encryptedPassword);
            studentRepo.save(student);
            System.out.println("Inserted: " + studentId);
        }
		
	}

}
