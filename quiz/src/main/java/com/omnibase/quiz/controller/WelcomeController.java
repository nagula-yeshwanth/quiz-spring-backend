package com.omnibase.quiz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Quiz Application API");
        response.put("version", "1.0.0");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("API Documentation", "/swagger-ui/index.html");
        endpoints.put("Register", "POST /auth/register");
        endpoints.put("Login", "POST /auth/login");
        endpoints.put("Get Quizzes", "GET /quizzes");
        endpoints.put("API Docs JSON", "/v3/api-docs");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> credentials = new HashMap<>();
        credentials.put("testUser", "username: testuser, password: password123");
        credentials.put("note", "Register new users via POST /auth/register");
        
        response.put("sampleCredentials", credentials);
        
        return ResponseEntity.ok(response);
    }
}