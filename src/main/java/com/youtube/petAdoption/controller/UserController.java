package com.youtube.petAdoption.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.youtube.petAdoption.model.User;
import com.youtube.petAdoption.security.JwtUtil;
import com.youtube.petAdoption.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
    	 User foundUser = userService.findByEmail(user.getEmail());
    	 if (foundUser != null && userService.validateEmail(foundUser.getEmail(), user.getPassword())) {
    	        String token = jwtUtil.generateToken(foundUser.getId(), foundUser.getUsername(), foundUser.getEmail());
    	        return ResponseEntity.ok(Map.of("token", token));
    	    }
    	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.registerUser(user)) {
        	String token = jwtUtil.generateToken(user.getId(),user.getUsername(),user.getEmail());

            // Return token in response
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully",
                "token", token
            ));        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User already exists"));
    }
    
    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }
	 
    
}

