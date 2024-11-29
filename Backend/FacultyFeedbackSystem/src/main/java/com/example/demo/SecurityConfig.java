package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@SuppressWarnings({ "removal" })
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
        http.cors()
            .and()
            .csrf().disable(); // Enable CORS and disable CSRF
        
        http.authorizeHttpRequests()
        	.anyRequest()
        	.permitAll(); // Allow all requests for now
        
        return http.build();
    }
}
