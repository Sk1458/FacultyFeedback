package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacultyRepository extends JpaRepository<FacultyData, Integer> {
	
	boolean existsById(int id);
	
	
	Optional<FacultyData> findByMobileNumber(String mobileNumber);
    Optional<FacultyData> findByEmail(String email);
    
    @Query("SELECT f FROM FacultyData f JOIN f.subjects s WHERE f.campusCode = :campusCode AND s.semester = :semester")
    List<FacultyData> findByCampusCodeAndSubjectsSemester(
        @Param("campusCode") String campusCode, 
        @Param("semester") Integer semester
    );

}
