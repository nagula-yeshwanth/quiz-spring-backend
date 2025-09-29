package com.omnibase.quiz.controller;

import com.omnibase.quiz.dto.*;
import com.omnibase.quiz.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @GetMapping
    public ResponseEntity<List<QuizResponseDto>> getActiveQuizzes() {
        List<QuizResponseDto> quizzes = quizService.getActiveQuizzes();
        return ResponseEntity.ok(quizzes);
    }
    
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuiz(@PathVariable Long quizId) {
        try {
            QuizResponseDto quiz = quizService.getPublicQuizById(quizId);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{quizId}/attempts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUserAttempts(@PathVariable Long quizId) {
        Long attempts = quizService.getUserAttempts(quizId);
        return ResponseEntity.ok(attempts);
    }
    
    @PostMapping("/{quizId}/start")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TakeResponseDto> startQuiz(@PathVariable Long quizId) {
        try {
            TakeResponseDto take = quizService.startQuiz(quizId);
            return ResponseEntity.status(HttpStatus.CREATED).body(take);
        } catch (Exception e) {
            if (e.getMessage().contains("Maximum attempts")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{quizId}/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TakeResponseDto> submitQuiz(@PathVariable Long quizId, 
                                                      @Valid @RequestBody SubmitAnswersRequestDto request) {
        try {
            TakeResponseDto result = quizService.submitQuiz(quizId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/users/{userId}/results")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TakeResponseDto>> getUserResults(@PathVariable Long userId) {
        List<TakeResponseDto> results = quizService.getUserResults(userId);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/takes/{takeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TakeResponseDto> getTake(@PathVariable Long takeId) {
        try {
            TakeResponseDto take = quizService.getTakeById(takeId);
            return ResponseEntity.ok(take);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}