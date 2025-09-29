package com.omnibase.quiz.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "takes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Take {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "quiz_id", nullable = false)
    private Long quizId;
    
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal score;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", insertable = false, updatable = false)
    private Quiz quiz;
    
    @OneToMany(mappedBy = "take", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Response> responses;
    
    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }
}