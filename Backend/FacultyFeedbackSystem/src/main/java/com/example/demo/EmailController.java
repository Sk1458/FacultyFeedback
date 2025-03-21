package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class EmailController {

    @Autowired
    private StudentCredentialsRepository studentRepo;

    // ✅ OTP Storage with expiration timestamps
    private Map<String, OtpEntry> otpStorage = new HashMap<>();

    private static final long OTP_EXPIRATION_TIME = 5 * 60 * 1000;  // 5 minutes
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${spring.mail.password}")
    private String fromPassword;

    // ✅ 1. Send OTP
    @PostMapping("/email/send-otp")
    public ResponseEntity<String> sendOTP(@RequestBody Map<String, String> request) {
        String rollNumber = request.get("rollNumber");

        // ✅ 1. Check if student exists
        Optional<StudentCredentials> studentOpt = studentRepo.findById(rollNumber);
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Student not found.");
        }

        // ✅ 2. Determine the email domain based on campus code
        String emailDomain;

        if (rollNumber.toLowerCase().contains("mh")) {
            emailDomain = "@acoe.edu.in";
        } else if (rollNumber.toLowerCase().contains("p3")) {
            emailDomain = "@acet.edu.in";
        } else if (rollNumber.toLowerCase().contains("a9")) {
            emailDomain = "@aec.edu.in";
        } else {
            return ResponseEntity.badRequest().body("Invalid campus code in roll number.");
        }

        // ✅ 3. Generate student email (only if the student exists)
        String email = rollNumber.toLowerCase() + emailDomain;

        // ✅ 4. Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // ✅ 5. Store OTP with expiration timestamp
        otpStorage.put(rollNumber, new OtpEntry(otp, System.currentTimeMillis()));

        // ✅ 6. Send OTP email
        boolean emailSent = sendEmail(email, "Password Reset OTP", "Your OTP is: " + otp);

        if (emailSent) {
            return ResponseEntity.ok("OTP sent successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to send OTP.");
        }
    }


    // ✅ 2. Verify OTP with expiration check
    @PostMapping("/email/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> request) {
        String rollNumber = request.get("rollNumber");
        String enteredOtp = request.get("otp");

        // Check if OTP exists
        if (!otpStorage.containsKey(rollNumber)) {
            return ResponseEntity.badRequest().body("OTP not found. Please request again.");
        }

        OtpEntry otpEntry = otpStorage.get(rollNumber);

        // Check for expiration
        if (System.currentTimeMillis() - otpEntry.timestamp > OTP_EXPIRATION_TIME) {
            otpStorage.remove(rollNumber); // Remove expired OTP
            return ResponseEntity.badRequest().body("OTP expired. Please request a new one.");
        }

        // Verify OTP
        if (enteredOtp.equals(otpEntry.otp)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    // ✅ 3. Reset Password with BCrypt hashing
    @PostMapping("/email/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String rollNumber = request.get("rollNumber");
        String newPassword = request.get("newPassword");

        Optional<StudentCredentials> studentOpt = studentRepo.findById(rollNumber);

        if (studentOpt.isPresent()) {
            StudentCredentials student = studentOpt.get();

            // Encrypt the new password before storing
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            student.setPassword(encoder.encode(newPassword));

            studentRepo.save(student);

            // Remove OTP after successful reset
            otpStorage.remove(rollNumber);

            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Student not found.");
        }
    }

    // ✅ Email sending logic with better exception handling
    private boolean sendEmail(String recipient, String subject, String content) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Inner class to store OTP with expiration timestamp
    private static class OtpEntry {
        String otp;
        long timestamp;

        OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }
}
