package com.youtube.petAdoption.service;

import com.youtube.petAdoption.enums.Breed;
import com.youtube.petAdoption.enums.Gender;
import com.youtube.petAdoption.enums.Size;
import com.youtube.petAdoption.enums.Type;
import com.youtube.petAdoption.model.Post;
import com.youtube.petAdoption.model.UserPost;
import com.youtube.petAdoption.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    
    private static final String UPLOAD_DIR = "uploads/";

    
    
    public void savePost(String name, String type, String breed, int age, String size, String gender, String description, MultipartFile image ,String color,BigDecimal price) throws IOException {
      
        Type petType = Type.valueOf(type);  
        Breed petBreed = Breed.valueOf(breed);  
        Size petSize = Size.valueOf(size);  
        Gender petGender = Gender.valueOf(gender);  

        
        String imageFileName = saveImage(image);
        
        // Create a new Post object
        Post post = new Post();
        post.setName(name);
        post.setType(petType); 
        post.setBreed(petBreed);  
        post.setAge(age);
        post.setSize(petSize);  
        post.setGender(petGender); 
        post.setDescription(description);
        post.setImageUrl(imageFileName);  
        post.setColor(color);
        post.setPrice(price);


        postRepository.save(post);
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


	    public List<Post> getAllPosts() {
	        try {
	            return (List<Post>) postRepository.findAll();  // Fetches all posts from the database
	        } catch (Exception e) {
	            // Log the exception in case of failure
	            e.printStackTrace();
	            throw new RuntimeException("Error fetching posts from the database", e);
	        }
	    }

		 public Optional<Post> getPostById(int id) {
		       return postRepository.findById(id);
		 }
		 public List<Post> getPetsByType(String type) {
			    try {
			        Type petType = Type.valueOf(type.toUpperCase()); // Ensure it's case-insensitive
			        return postRepository.findByType(petType); 
			    } catch (IllegalArgumentException e) {
			        throw new RuntimeException("Invalid pet type: " + type);
			    }
			}


		 public List<Post> getFilteredPets(String type, String breed, Integer age, String size, String gender, String color) {
			    Type petType = Type.valueOf(type.toUpperCase());

			    Breed petBreed = (breed != null && !breed.isEmpty()) ? Breed.valueOf(breed.toUpperCase()) : null;
			    Size petSize = (size != null && !size.isEmpty()) ? Size.valueOf(size.toUpperCase()) : null;
			    Gender petGender = (gender != null && !gender.isEmpty()) ? Gender.valueOf(gender.toUpperCase()) : null;

			    return postRepository.findByFilters(petType, petBreed, age, petSize, petGender, color);
			}

		 
		 public void updatePost(int id, String name, String type, String breed, int age, String size, String gender, String description, MultipartFile image,String color,BigDecimal price) throws IOException {
			    Optional<Post> optionalPost = postRepository.findById(id);

			    if (optionalPost.isPresent()) {
			        Post post = optionalPost.get();
			        
			        post.setName(name);
			        post.setType(Type.valueOf(type));
			        post.setBreed(Breed.valueOf(breed));
			        post.setAge(age);
			        post.setSize(Size.valueOf(size));
			        post.setGender(Gender.valueOf(gender));
			        post.setDescription(description);

			        // If a new image is uploaded, save it and update the URL
			        if (image != null && !image.isEmpty()) {
			            String imagePath = saveImage(image);
			            post.setImageUrl(imagePath);
			        }
			        
			        post.setColor(color);
			        post.setPrice(price);

			        postRepository.save(post);
			    } else {
			        throw new IllegalArgumentException("Post with ID " + id + " not found.");
			    }
			}

		 
		 public void deletePost(int id) {
			    Optional<Post> postOptional = postRepository.findById(id);
			    if (postOptional.isPresent()) {
			        Post post = postOptional.get(	);
			        
			        // Delete the image file
			        String imagePath = "uploads/" + post.getImageUrl();
			        File imageFile = new File(imagePath);
			        if (imageFile.exists()) {
			            imageFile.delete();  // Delete the image
			        }
			        
			        // Delete the post from the database
			        postRepository.deleteById(id);
			    } else {
			        throw new RuntimeException("Post not found with id: " + id);
			    }
			}

}
