package com.omnibase.quiz.repository;

import com.omnibase.quiz.model.Take;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {
    List<Take> findByUserId(Long userId);
    List<Take> findByQuizId(Long quizId);
    List<Take> findByUserIdAndQuizId(Long userId, Long quizId);
    
    @Query("SELECT COUNT(t) FROM Take t WHERE t.userId = :userId AND t.quizId = :quizId")
    Long countByUserIdAndQuizId(Long userId, Long quizId);
    
    @Query("SELECT MAX(t.attemptNumber) FROM Take t WHERE t.userId = :userId AND t.quizId = :quizId")
    Integer getMaxAttemptNumber(Long userId, Long quizId);
}