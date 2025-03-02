package com.youtube.petAdoption.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.youtube.petAdoption.model.Admin;
import com.youtube.petAdoption.security.JwtUtil;
import com.youtube.petAdoption.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class AdminController {
    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public AdminController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        if (adminService.validateAdmin(admin.getUsername(), admin.getPassword())) {
            String token = jwtUtil.generateAdminToken(admin.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }
    
}

