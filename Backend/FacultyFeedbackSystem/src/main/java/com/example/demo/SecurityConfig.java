package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@SuppressWarnings({ "removal", "deprecation" })
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
        http.cors().and().csrf().disable(); // Enable CORS and disable CSRF
        http.authorizeRequests().anyRequest().permitAll(); // Allow all requests for now
        return http.build();
    }
}
