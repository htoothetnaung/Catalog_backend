package com.youtube.petAdoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.youtube.petAdoption.model.ApplicationForm;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {
}
