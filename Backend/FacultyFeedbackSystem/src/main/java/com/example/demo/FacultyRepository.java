package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<FacultyData, Integer> {
	
	boolean existsById(int id);
	
	
	Optional<FacultyData> findByMobileNumber(String mobileNumber);
    Optional<FacultyData> findByEmail(String email);


}
