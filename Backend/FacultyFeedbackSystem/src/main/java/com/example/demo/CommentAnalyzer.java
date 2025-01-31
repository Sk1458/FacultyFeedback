package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommentAnalyzer {
	
	public Map<String, Object> analyzeComment(String comment) {
        String pythonApiUrl = "http://localhost:5000/analyze";

        // Set up the request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        // Request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("comment", comment);
        
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Make the API call
        ResponseEntity<Map> response = restTemplate.exchange(
            pythonApiUrl, 
            HttpMethod.POST, 
            request, 
            Map.class
        );

        // Return the response
        return response.getBody();
    }
      
}
