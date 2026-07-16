package com.vuemov.service;

import com.vuemov.dto.PracticeResultRequest;
import com.vuemov.model.PracticeResult;
import com.vuemov.model.UserProgress;
import com.vuemov.repository.PracticeResultRepository;
import com.vuemov.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PracticeService {
    
    @Autowired
    private PracticeResultRepository practiceResultRepository;
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    public PracticeResult recordPracticeResult(String userId, PracticeResultRequest request) {
        PracticeResult practiceResult = new PracticeResult(
            userId,
            request.getLessonId(),
            request.getPracticeType()
        );
        
        practiceResult.setTotalQuestions(request.getTotalQuestions());
        practiceResult.setCorrectAnswers(request.getCorrectAnswers());
        practiceResult.setTimeTakenSeconds(request.getTimeTakenSeconds());
        
        // Convert question results
        List<PracticeResult.QuestionResult> questionResults = new ArrayList<>();
        if (request.getQuestionResults() != null) {
            for (PracticeResultRequest.QuestionResultRequest qr : request.getQuestionResults()) {
                PracticeResult.QuestionResult questionResult = new PracticeResult.QuestionResult(
                    qr.getQuestionId(),
                    qr.getUserAnswer(),
                    qr.getCorrectAnswer(),
                    qr.getUserAnswer().equals(qr.getCorrectAnswer()),
                    qr.getTimeTakenSeconds()
                );
                questionResults.add(questionResult);
            }
        }
        practiceResult.setQuestionResults(questionResults);
        
        // Calculate score and XP
        practiceResult.calculateScore();
        
        // Save practice result
        PracticeResult savedResult = practiceResultRepository.save(practiceResult);
        
        // Update user progress
        updateUserProgressAfterPractice(userId, practiceResult);
        
        return savedResult;
    }
    
    private void updateUserProgressAfterPractice(String userId, PracticeResult practiceResult) {
        var userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            progress.setTotalXP(progress.getTotalXP() + practiceResult.getXpEarned());
            progress.setTotalPoints(progress.getTotalPoints() + (int) practiceResult.getScorePercentage());
            progress.setUpdatedAt(LocalDateTime.now());
            userProgressRepository.save(progress);
        }
    }
    
    public List<PracticeResult> getUserPracticeResults(String userId) {
        return practiceResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
    }
    
    public List<PracticeResult> getLessonPracticeResults(String userId, String lessonId) {
        return practiceResultRepository.findByUserIdAndLessonId(userId, lessonId);
    }
    
    public List<PracticeResult> getPracticeByType(String userId, String practiceType) {
        return practiceResultRepository.findByUserIdAndPracticeType(userId, practiceType);
    }
    
    public PracticeResult getPracticeResultById(String id) {
        return practiceResultRepository.findById(id).orElse(null);
    }
}
