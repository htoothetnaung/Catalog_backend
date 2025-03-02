package com.youtube.petAdoption.controller;

import com.youtube.petAdoption.enums.Type;
import com.youtube.petAdoption.model.UserPost;
import com.youtube.petAdoption.repository.UserPostRepository;
import com.youtube.petAdoption.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import com.youtube.petAdoption.enums.PetType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
//import java.util.Optional;
@RestController
@RequestMapping("/api/userPosts")  
public class UserPostController {
    @Autowired
    private UserPostService userPostService;
   
    @PostMapping("/addPost")
    public ResponseEntity<Map<String, String>> addPost(
    		 	@RequestParam("userId") long userId,  
    	        @RequestParam("name") String name,
    	        @RequestParam("type") String type,
    	        @RequestParam("breed") String breed,
    	        @RequestParam("age") int age,
    	        @RequestParam("contactInfo") String contactInfo,
    	        
    	        @RequestParam("size") String size,
    	        @RequestParam("gender") String gender,
    	        @RequestParam("description") String description,
    	        @RequestParam("color") String color, 
    	        @RequestParam("price") int price, 
    	        @RequestParam("image") MultipartFile image){
    	
        Map<String, String> response = new HashMap<>();


    	try {
            LocalDateTime createdAt = LocalDateTime.now();
            
            userPostService.savePost(userId,name, type, breed, age, size, gender, description,color,price, image,contactInfo,createdAt);
            response.put("message", "Post added successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Error saving post");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/getPostsByUser/{userId}")
    public ResponseEntity<List<UserPost>> getPostsByUserId(@PathVariable Long userId) {
        try {
            List<UserPost> posts = userPostService.getPostsByUserId(userId);
            if (posts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    		
		
	@GetMapping("/postDetails/{id}")
		public Optional<UserPost> getPostById(@PathVariable Long id) {
		return userPostService.getPostById(id);
	}

	@Autowired
    private UserPostRepository postRepository;
	
	@DeleteMapping("/deletePost/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }

	
	@PutMapping("/updatePost/{id}")
	public ResponseEntity<Map<String, String>> updatePost(
			@PathVariable Long id,
		 	@RequestParam("userId") long userId,  
	        @RequestParam("name") String name,
	        @RequestParam("type") String type,
	        @RequestParam("breed") String breed,
	        @RequestParam("age") int age,
	        @RequestParam("contactInfo") String contactInfo,

	        
	        @RequestParam("size") String size,
	        @RequestParam("gender") String gender,
	        @RequestParam("description") String description,
	        @RequestParam("color") String color,
	        @RequestParam("price") int price,
	        @RequestParam(value = "image", required = false) MultipartFile image) {
	    
	    Map<String, String> response = new HashMap<>();

	    try {
	        userPostService.updatePost(id, name, type, breed, age, size, gender, description, color, price, image,contactInfo);
	        response.put("message", "Post updated successfully");
	        return ResponseEntity.ok(response);
	    } catch (IOException e) {
	        response.put("error", "Error updating post");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    } catch (IllegalArgumentException e) {
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}

	@GetMapping("/getPosts")
    public List<UserPost> getAllPets() {
        return userPostService.getAllPosts();
    }
	
	//Phoo Hay Man Zaw Code Code
	// Get pets by type (Dog or Cat)
	@GetMapping("/getPetsByType")
	public ResponseEntity<?> getPetsByType(@RequestParam String type) {
	    try {
	        List<UserPost> pets = userPostService.getPetsByType(type);
	        return ResponseEntity.ok(pets);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}






    
 // Get pets with filters
    @GetMapping("/search")
    public List<UserPost> getFilteredPets(
            @RequestParam String type,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String color) {
    	System.out.println("Received parameters: Type=" + type + ", Breed=" + breed + 
                ", Age=" + age + ", Size=" + size + ", Gender=" + gender + ", Color=" + color);
        return userPostService.getFilteredPets(type, breed, age, size, gender, color);
    }

}
