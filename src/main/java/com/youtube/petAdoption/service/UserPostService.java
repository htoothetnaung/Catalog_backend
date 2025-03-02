package com.youtube.petAdoption.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//import com.youtube.petAdoption.enums.PetType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.youtube.petAdoption.enums.Type;
import com.youtube.petAdoption.model.UserPost;
import com.youtube.petAdoption.repository.UserPostRepository;


@Service
public class UserPostService {

    @Autowired
    private UserPostRepository userPostRepository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public void savePost(Long userId, String name, String type, String breed, int age, String size, String gender,
                         String description, String color, int price, MultipartFile image,String contactInfo,LocalDateTime createdAt) throws IOException {


        if (image == null || image.isEmpty()) {
            throw new IOException("Uploaded file is empty or missing.");
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        System.out.println("📂 Saving file to: " + filePath.toString());

        // Save the file
        image.transferTo(filePath.toFile());

        String imagePath = "/uploads/" + fileName;
        System.out.println("✅ File saved at: " + imagePath);

        UserPost post = new UserPost(userId, name, type, breed, age, size, gender, description, color, price, imagePath,contactInfo,createdAt);
        userPostRepository.save(post);

    }
    
    private String saveImage(MultipartFile image) throws IOException {

    	String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Path targetLocation = Path.of(UPLOAD_DIR, imageFileName);

                Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return imageFileName;
    }

    public List<UserPost> getAllPosts() {
        return userPostRepository.findAll();
    }
    
    public List<UserPost> getPostsByUserId(Long userId) {
        return userPostRepository.findByUserId(userId);
    }
    public void deletePostById(Long id) {
        userPostRepository.deleteById(id);
    }
    
    public Optional<UserPost> getPostById(Long id) {
        return userPostRepository.findById(id);
    }
    
    public void updatePost(Long id, String name, String type, String breed, int age, String size, 
            String gender, String description, String color, int price, MultipartFile image,String contactInfo) throws IOException {

			Optional<UserPost> existingPostOpt = userPostRepository.findById(id);
			
			if (existingPostOpt.isPresent()) {
			UserPost existingPost = existingPostOpt.get();
			
				existingPost.setName(name);
				existingPost.setType(type);
				existingPost.setBreed(breed);
				existingPost.setAge(age);
				existingPost.setSize(size);
				existingPost.setGender(gender);
				existingPost.setDescription(description);
				existingPost.setColor(color);
				existingPost.setPrice(price);
				existingPost.setContactInfo(contactInfo);;
				
	
				// If a new image is uploaded, save it and update the URL
		        if (image != null && !image.isEmpty()) {
		            String imagePath = saveImage(image);
			        existingPost.setImagePath("/uploads/" + imagePath);
		        }
		        

			
			userPostRepository.save(existingPost);
			} else {
			throw new IllegalArgumentException("Post with ID " + id + " not found.");
			}
}
    
 
    
    //HM
    public List<UserPost> getPetsByType(String type) {
        try {
            return userPostRepository.findByType(type); 
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid pet type: " + type);
        }
    }
    



    
    public List<UserPost> getFilteredPets(String type, String breed, Integer age, String size, String gender, String color) {
        return userPostRepository.searchPets(type, breed, age, size, gender, color);
    }



}
