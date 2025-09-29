package com.omnibase.quiz.service;

import com.omnibase.quiz.dto.StatsResponseDto;
import com.omnibase.quiz.dto.UserResponseDto;
import com.omnibase.quiz.model.User;
import com.omnibase.quiz.repository.QuizRepository;
import com.omnibase.quiz.repository.TakeRepository;
import com.omnibase.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private TakeRepository takeRepository;
    
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponseDto)
                .collect(Collectors.toList());
    }
    
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserResponseDto(user);
    }
    
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
    
    public StatsResponseDto getStats() {
        Long totalUsers = userRepository.count();
        Long totalQuizzes = quizRepository.count();
        Long totalAttempts = takeRepository.count();
        
        // Calculate average score - this is a simple implementation
        // In a real scenario, you might want to use a proper aggregate query
        List<BigDecimal> scores = takeRepository.findAll().stream()
                .map(take -> take.getScore())
                .filter(score -> score != null)
                .collect(Collectors.toList());
        
        BigDecimal averageScore = scores.isEmpty() ? BigDecimal.ZERO :
                scores.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(scores.size()), 2, RoundingMode.HALF_UP);
        
        return StatsResponseDto.builder()
                .totalUsers(totalUsers)
                .totalQuizzes(totalQuizzes)
                .totalAttempts(totalAttempts)
                .averageScore(averageScore)
                .build();
    }
    
    private UserResponseDto convertToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}