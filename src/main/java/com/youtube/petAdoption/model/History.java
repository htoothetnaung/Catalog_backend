package com.youtube.petAdoption.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long applicationId; // ID of the original application form

    @Column(nullable = false)
    private String adopterEmail;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private String petType;

    @Column(nullable = false)
    private LocalDateTime adoptionDate;
    
    public History() {
            }


    public History(Long applicationId, String adopterEmail, String petName, String petType, LocalDateTime adoptionDate) {
        this.applicationId = applicationId;
        this.adopterEmail = adopterEmail;
        this.petName = petName;
        this.petType = petType;
        this.adoptionDate = adoptionDate;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public String getAdopterEmail() {
		return adopterEmail;
	}

	public void setAdopterEmail(String adopterEmail) {
		this.adopterEmail = adopterEmail;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetType() {
		return petType;
	}

	public void setPetType(String petType) {
		this.petType = petType;
	}

	public LocalDateTime getAdoptionDate() {
		return adoptionDate;
	}

	public void setAdoptionDate(LocalDateTime adoptionDate) {
		this.adoptionDate = adoptionDate;
	}

    
}
