package com.youtube.petAdoption.DTO;

import com.youtube.petAdoption.model.Post;
import com.youtube.petAdoption.model.ApplicationForm;

public class ApplicationFormDTO {
    private Long formId;
    private String applicantName;
    private String phone;
    private String email;
    private String reason;
    private String status;

    private int petId;
    private String petName;
    private String breed;
    private String color;
    private String price;

    public ApplicationFormDTO(ApplicationForm form) {
        this.formId = form.getId();
        this.applicantName = form.getName();
        this.phone = form.getPhone();
        this.email = form.getEmail();
        this.reason = form.getReason();
        this.status = form.getStatus();

        Post pet = form.getPost();  
        if (pet != null) {
            this.petId = pet.getId();
            this.petName = pet.getName();
            this.breed = pet.getBreed().toString();
            this.color = pet.getColor();
            this.price = pet.getPrice().toString();
        }
    }

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
    
}
