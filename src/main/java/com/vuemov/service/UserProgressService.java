package com.vuemov.service;

import com.vuemov.dto.UserProgressResponse;
import com.vuemov.model.UserProgress;
import com.vuemov.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserProgressService {
    
    @Autowired
    private UserProgressRepository userProgressRepository;
    
    public UserProgress createUserProgress(String userId) {
        UserProgress userProgress = new UserProgress(userId);
        return userProgressRepository.save(userProgress);
    }
    
    public Optional<UserProgress> getUserProgress(String userId) {
        return userProgressRepository.findByUserId(userId);
    }
    
    public UserProgressResponse getUserProgressResponse(String userId) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            UserProgressResponse response = new UserProgressResponse();
            response.setId(progress.getId());
            response.setUserId(progress.getUserId());
            response.setCurrentHskLevel(progress.getCurrentHskLevel());
            response.setTotalXP(progress.getTotalXP());
            response.setTotalPoints(progress.getTotalPoints());
            response.setStreakDays(progress.getStreakDays());
            response.setLessonProgress(progress.getLessonProgress());
            response.setCompletedVocabularyIds(progress.getCompletedVocabularyIds());
            response.setCompletedGrammarIds(progress.getCompletedGrammarIds());
            response.setTotalLessonCount(progress.getTotalLessonCount());
            response.setCompletedLessonCount(progress.getCompletedLessonCount());
            return response;
        }
        return null;
    }
    
    public void markVocabularyCompleted(String userId, String vocabularyId) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            if (!progress.getCompletedVocabularyIds().contains(vocabularyId)) {
                progress.getCompletedVocabularyIds().add(vocabularyId);
                progress.setUpdatedAt(LocalDateTime.now());
                userProgressRepository.save(progress);
            }
        }
    }
    
    public void markGrammarCompleted(String userId, String grammarId) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            if (!progress.getCompletedGrammarIds().contains(grammarId)) {
                progress.getCompletedGrammarIds().add(grammarId);
                progress.setUpdatedAt(LocalDateTime.now());
                userProgressRepository.save(progress);
            }
        }
    }
    
    public void updateLessonProgress(String userId, String lessonId, double completionPercentage) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            progress.getLessonProgress().put(lessonId, completionPercentage);
            if (completionPercentage >= 100) {
                progress.setCompletedLessonCount(progress.getCompletedLessonCount() + 1);
            }
            progress.setUpdatedAt(LocalDateTime.now());
            userProgressRepository.save(progress);
        }
    }
    
    public void increaseHskLevel(String userId) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            if (progress.getCurrentHskLevel() < 6) {
                progress.setCurrentHskLevel(progress.getCurrentHskLevel() + 1);
                progress.setUpdatedAt(LocalDateTime.now());
                userProgressRepository.save(progress);
            }
        }
    }
    
    public void updateStreak(String userId) {
        Optional<UserProgress> userProgress = userProgressRepository.findByUserId(userId);
        if (userProgress.isPresent()) {
            UserProgress progress = userProgress.get();
            LocalDateTime lastActive = progress.getLastActiveDate();
            LocalDateTime now = LocalDateTime.now();
            
            // If last active was today, don't increase streak
            if (lastActive != null && 
                lastActive.toLocalDate().equals(now.toLocalDate())) {
                return;
            }
            
            // If last active was yesterday, increase streak
            if (lastActive != null && 
                lastActive.toLocalDate().equals(now.minusDays(1).toLocalDate())) {
                progress.setStreakDays(progress.getStreakDays() + 1);
            } else {
                // Otherwise reset streak to 1
                progress.setStreakDays(1);
            }
            
            progress.setLastActiveDate(now);
            progress.setUpdatedAt(now);
            userProgressRepository.save(progress);
        }
    }
}
