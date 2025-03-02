package com.youtube.petAdoption.controller;

import com.youtube.petAdoption.model.Post;
import com.youtube.petAdoption.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class PostController {

    @Autowired
    private PostService postService;
    

    @PostMapping("addPost")
    public ResponseEntity<Map<String, String>> addPost(
    	@RequestParam("name") String name,
        @RequestParam("type") String type,
        @RequestParam("breed") String breed,
        @RequestParam("age") int age,
        @RequestParam("size") String size,
        @RequestParam("gender") String gender,
        @RequestParam("description") String description,
        @RequestParam("color") String color,
        @RequestParam("price") BigDecimal price,
        @RequestParam("image") MultipartFile image) {
    	
        Map<String, String> response = new HashMap<>();


    	try {
            postService.savePost(name, type, breed, age, size, gender, description, image,color,price);
            response.put("message", "Post added successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Error saving post");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	    @GetMapping("/getPosts")
	    public ResponseEntity<List<Post>> getPosts() {
	        try {
	            List<Post> posts = postService.getAllPosts();
	            return ResponseEntity.ok(posts);
	        } catch (Exception e) {
	            // Log the exception details
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
		
		
		    @GetMapping("/postDetails/{id}")
		    public Optional<Post> getPostById(@PathVariable int id) {
		        return postService.getPostById(id);
		    }


		    @DeleteMapping("/deletePost/{id}")
		    public ResponseEntity<Map<String, String>> deletePost(@PathVariable int id) {
		        postService.deletePost(id); 
		        Map<String, String> response = new HashMap<>();
		        response.put("message", "Post deleted successfully");
		        return ResponseEntity.ok(response);
		    }
		    
		    @PutMapping("/updatePost/{id}")
		    public ResponseEntity<Map<String, String>> updatePost(
		        @PathVariable int id,
		        @RequestParam("name") String name,
		        @RequestParam("type") String type,
		        @RequestParam("breed") String breed,
		        @RequestParam("age") int age,
		        @RequestParam("size") String size,
		        @RequestParam("gender") String gender,
		        @RequestParam(value = "description", required = false) String description,
		        @RequestParam("color") String color,
		        @RequestParam("price") BigDecimal price,
		        @RequestParam(value = "image", required = false) MultipartFile image) {

		        Map<String, String> response = new HashMap<>();
		        
		        try {
		            postService.updatePost(id, name, type, breed, age, size, gender, description, image,color,price);
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
		    
		    //HayMan Code without Modified
		    
		    @GetMapping("/getPetsByType")
		    public List<Post> getPetsByType(@RequestParam String type) {
		        return postService.getPetsByType(type);
		    }
		    
		    
		    @GetMapping("/search")
		    public List<Post> getFilteredPets(
		            @RequestParam String type,
		            @RequestParam(required = false) String breed,
		            @RequestParam(required = false) Integer age,
		            @RequestParam(required = false) String size,
		            @RequestParam(required = false) String gender,
		            @RequestParam(required = false) String color) {
		    	 System.out.println("Received parameters: Type=" + type + ", Breed=" + breed + 
	                       ", Age=" + age + ", Size=" + size + ", Gender=" + gender + ", Color=" + color);
		        return postService.getFilteredPets(type, breed, age, size, gender, color);
		    }
}
