package com.omnibase.quiz.repository;

import com.omnibase.quiz.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByTakeId(Long takeId);
    void deleteByTakeId(Long takeId);
}