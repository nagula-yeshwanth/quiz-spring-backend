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
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminQuizController {
    
    @Autowired
    private QuizService quizService;
    
    // Quiz Management
    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizResponseDto>> getAllQuizzes() {
        List<QuizResponseDto> quizzes = quizService.getAllQuizzesForAdmin();
        return ResponseEntity.ok(quizzes);
    }
    
    @PostMapping("/quizzes")
    public ResponseEntity<QuizResponseDto> createQuiz(@Valid @RequestBody QuizRequestDto request) {
        QuizResponseDto quiz = quizService.createQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }
    
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuiz(@PathVariable Long quizId) {
        try {
            QuizResponseDto quiz = quizService.getQuizById(quizId);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizResponseDto> updateQuiz(@PathVariable Long quizId, 
                                                      @Valid @RequestBody QuizRequestDto request) {
        try {
            QuizResponseDto quiz = quizService.updateQuiz(quizId, request);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/quizzes/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        try {
            quizService.deleteQuiz(quizId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Question Management
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<QuestionResponseDto>> getQuestions(@PathVariable Long quizId) {
        List<QuestionResponseDto> questions = quizService.getQuestionsByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }
    
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<QuestionResponseDto> addQuestion(@PathVariable Long quizId, 
                                                           @Valid @RequestBody QuestionRequestDto request) {
        QuestionResponseDto question = quizService.addQuestion(quizId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }
    
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Long questionId) {
        try {
            QuestionResponseDto question = quizService.getQuestionById(questionId);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable Long questionId, 
                                                              @Valid @RequestBody QuestionRequestDto request) {
        try {
            QuestionResponseDto question = quizService.updateQuestion(questionId, request);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        try {
            quizService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Option Management
    @PostMapping("/questions/{questionId}/options")
    public ResponseEntity<List<OptionResponseDto>> addOptions(@PathVariable Long questionId, 
                                                              @Valid @RequestBody List<OptionRequestDto> requests) {
        List<OptionResponseDto> options = quizService.addOptions(questionId, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(options);
    }
    
    @PutMapping("/options/{optionId}")
    public ResponseEntity<OptionResponseDto> updateOption(@PathVariable Long optionId, 
                                                          @Valid @RequestBody OptionRequestDto request) {
        try {
            OptionResponseDto option = quizService.updateOption(optionId, request);
            return ResponseEntity.ok(option);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}