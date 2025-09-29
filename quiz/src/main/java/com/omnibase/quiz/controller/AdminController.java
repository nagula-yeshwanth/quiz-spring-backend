package com.omnibase.quiz.controller;

import com.omnibase.quiz.dto.StatsResponseDto;
import com.omnibase.quiz.dto.UserResponseDto;
import com.omnibase.quiz.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        try {
            UserResponseDto user = adminService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<StatsResponseDto> getStats() {
        StatsResponseDto stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }
}