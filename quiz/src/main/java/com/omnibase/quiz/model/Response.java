package com.omnibase.quiz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "take_id", nullable = false)
    private Long takeId;
    
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(name = "option_id")
    private Long optionId;
    
    @Column(columnDefinition = "TEXT")
    private String text;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "take_id", insertable = false, updatable = false)
    private Take take;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", insertable = false, updatable = false)
    private Option option;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}