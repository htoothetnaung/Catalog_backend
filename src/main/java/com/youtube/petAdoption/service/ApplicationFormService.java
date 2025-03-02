package com.youtube.petAdoption.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.youtube.petAdoption.model.ApplicationForm;
import com.youtube.petAdoption.model.History;
import com.youtube.petAdoption.repository.ApplicationFormRepository;
import com.youtube.petAdoption.repository.HistoryRepository;
import com.youtube.petAdoption.repository.PostRepository;

@Service
public class ApplicationFormService {
    @Autowired
    private ApplicationFormRepository applicationFormRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private HistoryRepository historyRepository; 
    
    @Autowired
    private PostRepository postRepository; 

    public ApplicationForm saveForm(ApplicationForm form) {
        return applicationFormRepository.save(form);
    }
    
    public List<ApplicationForm> getAllForms() {
        return applicationFormRepository.findAll();
    }
    
    public ApplicationForm getFormById(Long id) {
        return applicationFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ApplicationForm with ID " + id + " not found"));
    }
    
    public void deleteApplicationForm(Long id) {
        if (!applicationFormRepository.existsById(id)) {
            throw new RuntimeException("Application Form not found");
        }
        applicationFormRepository.deleteById(id);
    }
    
    @Transactional
    public void rejectApplication(Long id, String applicantEmail, String rejectionReason) {
        if (!applicationFormRepository.existsById(id)) {
            throw new RuntimeException("Application Form not found.");
        }

        try {
            // Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(applicantEmail);
            message.setSubject("Adoption Rejection Notice");
            message.setText("Dear Applicant,\n\nWe regret to inform you that your adoption request has been rejected.\n\nReason: " + rejectionReason + "\n\nBest Regards,\nPawX Team");

            mailSender.send(message);
            System.out.println("Email sent successfully.");

            // Delete form
            applicationFormRepository.deleteById(id);
            System.out.println("Application form deleted.");
        } catch (Exception e) {
            System.out.println("Error in rejectApplication: " + e.getMessage());
            throw new RuntimeException("Failed to reject application: " + e.getMessage());
        }
    }
    
    @Transactional
    public void adoptApplication(Long id, String adopterEmail) {
        ApplicationForm form = applicationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application Form not found"));

        // Save adoption details in history
        History history = new History(
                form.getId(),
                adopterEmail,
                form.getPost().getName(),  
                form.getPost().getType().toString(), 
                LocalDateTime.now()
        );
        historyRepository.save(history);

        // Send adoption confirmation email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adopterEmail);
            message.setSubject("Adoption Confirmation");
            message.setText("Dear Adopter,\n\nCongratulations! Your adoption request for " 
                    + form.getPost().getName() + " has been approved.\n\n"
                    + "Thank you for giving a pet a loving home!\n\nBest Regards,\nPawX Team");

            mailSender.send(message);
            System.out.println("Adoption email sent successfully.");
            
            if (form.getPost() != null) {
                int postId = form.getPost().getId();  // Get post ID
                postRepository.deleteById(postId);
                System.out.println("Post deleted successfully.");
            }

            // Delete the application form
            applicationFormRepository.deleteById(id);
            System.out.println("Application form deleted after adoption.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to process adoption: " + e.getMessage());
        }
    }

    
}
