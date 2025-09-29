package com.omnibase.quiz.service;

import com.omnibase.quiz.dto.*;
import com.omnibase.quiz.model.*;
import com.omnibase.quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private OptionRepository optionRepository;
    
    @Autowired
    private TakeRepository takeRepository;
    
    @Autowired
    private ResponseRepository responseRepository;
    
    // Admin methods
    public List<QuizResponseDto> getAllQuizzesForAdmin() {
        return quizRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public QuizResponseDto createQuiz(QuizRequestDto request) {
        User currentUser = getCurrentUser();
        
        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .maxAttempts(request.getMaxAttempts())
                .createdBy(currentUser.getId())
                .isActive(request.getIsActive())
                .build();
        
        quiz = quizRepository.save(quiz);
        return convertToResponseDto(quiz);
    }
    
    public QuizResponseDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return convertToResponseDto(quiz);
    }
    
    public QuizResponseDto updateQuiz(Long id, QuizRequestDto request) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setMaxAttempts(request.getMaxAttempts());
        quiz.setIsActive(request.getIsActive());
        
        quiz = quizRepository.save(quiz);
        return convertToResponseDto(quiz);
    }
    
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
    
    // Public methods
    public List<QuizResponseDto> getActiveQuizzes() {
        return quizRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponseDtoPublic)
                .collect(Collectors.toList());
    }
    
    public QuizResponseDto getPublicQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        if (!quiz.getIsActive()) {
            throw new RuntimeException("Quiz is not active");
        }
        return convertToResponseDtoPublic(quiz);
    }
    
    // Question management
    public List<QuestionResponseDto> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId).stream()
                .map(this::convertToQuestionResponseDto)
                .collect(Collectors.toList());
    }
    
    public QuestionResponseDto addQuestion(Long quizId, QuestionRequestDto request) {
        Question question = Question.builder()
                .quizId(quizId)
                .text(request.getText())
                .type(request.getType())
                .build();
        
        question = questionRepository.save(question);
        return convertToQuestionResponseDto(question);
    }
    
    public QuestionResponseDto updateQuestion(Long questionId, QuestionRequestDto request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        question.setText(request.getText());
        question.setType(request.getType());
        
        question = questionRepository.save(question);
        return convertToQuestionResponseDto(question);
    }
    
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
    
    public QuestionResponseDto getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return convertToQuestionResponseDto(question);
    }
    
    // Option management
    public List<OptionResponseDto> addOptions(Long questionId, List<OptionRequestDto> optionRequests) {
        return optionRequests.stream()
                .map(request -> {
                    Option option = Option.builder()
                            .questionId(questionId)
                            .text(request.getText())
                            .isCorrect(request.getIsCorrect())
                            .build();
                    option = optionRepository.save(option);
                    return convertToOptionResponseDto(option, true);
                })
                .collect(Collectors.toList());
    }
    
    public OptionResponseDto updateOption(Long optionId, OptionRequestDto request) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        
        option.setText(request.getText());
        option.setIsCorrect(request.getIsCorrect());
        
        option = optionRepository.save(option);
        return convertToOptionResponseDto(option, true);
    }
    
    // Quiz taking
    public Long getUserAttempts(Long quizId) {
        User currentUser = getCurrentUser();
        return takeRepository.countByUserIdAndQuizId(currentUser.getId(), quizId);
    }
    
    public TakeResponseDto startQuiz(Long quizId) {
        User currentUser = getCurrentUser();
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        
        Long attempts = takeRepository.countByUserIdAndQuizId(currentUser.getId(), quizId);
        if (attempts >= quiz.getMaxAttempts()) {
            throw new RuntimeException("Maximum attempts exceeded");
        }
        
        Integer nextAttemptNumber = takeRepository.getMaxAttemptNumber(currentUser.getId(), quizId);
        nextAttemptNumber = (nextAttemptNumber == null) ? 1 : nextAttemptNumber + 1;
        
        Take take = Take.builder()
                .userId(currentUser.getId())
                .quizId(quizId)
                .attemptNumber(nextAttemptNumber)
                .startedAt(LocalDateTime.now())
                .build();
        
        take = takeRepository.save(take);
        return convertToTakeResponseDto(take);
    }
    
    public TakeResponseDto submitQuiz(Long quizId, SubmitAnswersRequestDto request) {
        User currentUser = getCurrentUser();
        
        // Find the take
        Take take = takeRepository.findByUserIdAndQuizId(currentUser.getId(), quizId)
                .stream()
                .filter(t -> t.getAttemptNumber().equals(request.getAttemptNumber()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found"));
        
        // Save responses and calculate score
        List<Response> responses = request.getAnswers().stream()
                .map(answer -> Response.builder()
                        .takeId(take.getId())
                        .questionId(answer.getQuestionId())
                        .optionId(answer.getOptionId())
                        .text(answer.getText())
                        .build())
                .collect(Collectors.toList());
        
        responses = responseRepository.saveAll(responses);
        
        // Calculate score
        BigDecimal score = calculateScore(responses);
        take.setScore(score);
        take.setCompletedAt(LocalDateTime.now());
        
        Take savedTake = takeRepository.save(take);
        return convertToTakeResponseDto(savedTake);
    }
    
    public List<TakeResponseDto> getUserResults(Long userId) {
        return takeRepository.findByUserId(userId).stream()
                .map(this::convertToTakeResponseDto)
                .collect(Collectors.toList());
    }
    
    public TakeResponseDto getTakeById(Long takeId) {
        Take take = takeRepository.findById(takeId)
                .orElseThrow(() -> new RuntimeException("Take not found"));
        return convertToTakeResponseDto(take);
    }
    
    // Utility methods
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    
    private BigDecimal calculateScore(List<Response> responses) {
        if (responses.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        long correctAnswers = responses.stream()
                .mapToLong(response -> {
                    if (response.getOptionId() != null) {
                        Option option = optionRepository.findById(response.getOptionId()).orElse(null);
                        return (option != null && option.getIsCorrect()) ? 1 : 0;
                    }
                    return 0;
                })
                .sum();
        
        return BigDecimal.valueOf((double) correctAnswers / responses.size() * 100);
    }
    
    private QuizResponseDto convertToResponseDto(Quiz quiz) {
        return QuizResponseDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .maxAttempts(quiz.getMaxAttempts())
                .createdBy(quiz.getCreatedBy())
                .isActive(quiz.getIsActive())
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .questions(quiz.getQuestions() != null ? 
                    quiz.getQuestions().stream()
                        .map(this::convertToQuestionResponseDto)
                        .collect(Collectors.toList()) : null)
                .build();
    }
    
    private QuizResponseDto convertToResponseDtoPublic(Quiz quiz) {
        return QuizResponseDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .maxAttempts(quiz.getMaxAttempts())
                .createdBy(quiz.getCreatedBy())
                .isActive(quiz.getIsActive())
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .questions(quiz.getQuestions() != null ? 
                    quiz.getQuestions().stream()
                        .map(this::convertToQuestionResponseDtoPublic)
                        .collect(Collectors.toList()) : null)
                .build();
    }
    
    private QuestionResponseDto convertToQuestionResponseDto(Question question) {
        return QuestionResponseDto.builder()
                .id(question.getId())
                .quizId(question.getQuizId())
                .text(question.getText())
                .type(question.getType())
                .createdAt(question.getCreatedAt())
                .options(question.getOptions() != null ?
                    question.getOptions().stream()
                        .map(option -> convertToOptionResponseDto(option, true))
                        .collect(Collectors.toList()) : null)
                .build();
    }
    
    private QuestionResponseDto convertToQuestionResponseDtoPublic(Question question) {
        return QuestionResponseDto.builder()
                .id(question.getId())
                .quizId(question.getQuizId())
                .text(question.getText())
                .type(question.getType())
                .createdAt(question.getCreatedAt())
                .options(question.getOptions() != null ?
                    question.getOptions().stream()
                        .map(option -> convertToOptionResponseDto(option, false))
                        .collect(Collectors.toList()) : null)
                .build();
    }
    
    private OptionResponseDto convertToOptionResponseDto(Option option, boolean includeCorrect) {
        return OptionResponseDto.builder()
                .id(option.getId())
                .questionId(option.getQuestionId())
                .text(option.getText())
                .isCorrect(includeCorrect ? option.getIsCorrect() : null)
                .build();
    }
    
    private TakeResponseDto convertToTakeResponseDto(Take take) {
        return TakeResponseDto.builder()
                .id(take.getId())
                .userId(take.getUserId())
                .quizId(take.getQuizId())
                .attemptNumber(take.getAttemptNumber())
                .score(take.getScore())
                .startedAt(take.getStartedAt())
                .completedAt(take.getCompletedAt())
                .responses(take.getResponses() != null ?
                    take.getResponses().stream()
                        .map(this::convertToResponseDto)
                        .collect(Collectors.toList()) : null)
                .build();
    }
    
    private TakeResponseDto.ResponseDto convertToResponseDto(Response response) {
        Option option = null;
        if (response.getOptionId() != null) {
            option = optionRepository.findById(response.getOptionId()).orElse(null);
        }
        
        return TakeResponseDto.ResponseDto.builder()
                .id(response.getId())
                .questionId(response.getQuestionId())
                .optionId(response.getOptionId())
                .text(response.getText())
                .isCorrect(option != null ? option.getIsCorrect() : null)
                .build();
    }
}