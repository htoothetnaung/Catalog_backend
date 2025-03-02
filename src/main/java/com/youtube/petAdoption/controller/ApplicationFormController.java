package com.youtube.petAdoption.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youtube.petAdoption.DTO.ApplicationFormDTO;
import com.youtube.petAdoption.DTO.RejectionRequest;
import com.youtube.petAdoption.model.ApplicationForm;
import com.youtube.petAdoption.repository.ApplicationFormRepository;
import com.youtube.petAdoption.service.ApplicationFormService;

@RestController
@RequestMapping("/api/applicationForm")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class ApplicationFormController {
	

	
    @Autowired
    private ApplicationFormService applicationFormService;
    
    @Autowired
    private ApplicationFormRepository applicationFormRepository;

    @PostMapping("/saveForm")
    public ApplicationForm submitapplicationForm(@RequestBody ApplicationForm form) {
        return applicationFormService.saveForm(form);
    }
    
    @GetMapping("/getAllForms")
    public ResponseEntity<?> getAllForms() {
        try {
            List<ApplicationForm> forms = applicationFormRepository.findAll();
            if (forms.isEmpty()) {
                return ResponseEntity.status(404).body("No application forms found.");
            }
            return ResponseEntity.ok(forms);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching application forms: " + e.getMessage());
        }
    }

    
    @GetMapping("/details/{id}")
    public ResponseEntity<?> getApplicationDetails(@PathVariable Long id) {
        Optional<ApplicationForm> optionalForm = applicationFormRepository.findById(id);

        if (!optionalForm.isPresent()) {
            return ResponseEntity.status(404).body("ApplicationForm with ID " + id + " not found");
        }

        ApplicationForm form = optionalForm.get();

        if (form.getPost() == null) {
            return ResponseEntity.status(404).body("No associated post found for ApplicationForm ID " + id);
        }

        return ResponseEntity.ok(form);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteApplicationForm(@PathVariable Long id) {
        try {
            applicationFormService.deleteApplicationForm(id);
            return ResponseEntity.ok().body("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectApplication(@PathVariable Long id, @RequestBody RejectionRequest request) {
        applicationFormService.rejectApplication(id, request.getApplicantEmail(), request.getRejectionReason());

        return ResponseEntity.ok(Collections.singletonMap("message", "Rejection email sent and application deleted."));
    }
    
    @PostMapping("/adopt/{id}")
    public ResponseEntity<?> adoptApplication(@PathVariable Long id, @RequestBody String adopterEmail) {
        try {
            applicationFormService.adoptApplication(id, adopterEmail);
            return ResponseEntity.ok(Collections.singletonMap("message", "Adoption successful. Email sent and stored in history."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }






}
