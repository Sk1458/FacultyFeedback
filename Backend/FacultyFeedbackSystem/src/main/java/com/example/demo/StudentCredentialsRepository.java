package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCredentialsRepository extends JpaRepository<StudentCredentials, String> {

	boolean existsByStudentId(String commonStudentId);

}
