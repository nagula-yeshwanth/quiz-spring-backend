package com.omnibase.quiz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    @JsonBackReference
    private Question question;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}