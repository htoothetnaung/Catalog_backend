package com.youtube.petAdoption.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.youtube.petAdoption.model.Student;
@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
	
}
