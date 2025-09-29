package com.omnibase.quiz.repository;

import com.omnibase.quiz.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestionId(Long questionId);
    void deleteByQuestionId(Long questionId);
}