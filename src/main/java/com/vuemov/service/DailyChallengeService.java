package com.vuemov.service;

import com.vuemov.model.DailyChallenge;
import com.vuemov.model.UserChallenge;
import com.vuemov.repository.DailyChallengeRepository;
import com.vuemov.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DailyChallengeService {
    
    @Autowired
    private DailyChallengeRepository dailyChallengeRepository;
    
    @Autowired
    private UserChallengeRepository userChallengeRepository;
    
    public Optional<DailyChallenge> getTodaysChallenge() {
        return dailyChallengeRepository.findByDate(LocalDate.now());
    }
    
    public Optional<DailyChallenge> getChallengeByDate(LocalDate date) {
        return dailyChallengeRepository.findByDate(date);
    }
    
    public UserChallenge getUserChallengeStatus(String userId, String challengeId, LocalDate date) {
        Optional<UserChallenge> existing = userChallengeRepository
            .findByUserIdAndDailyChallengeIdAndDate(userId, challengeId, date);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new user challenge entry if it doesn't exist
        UserChallenge userChallenge = new UserChallenge(userId, challengeId, date);
        return userChallengeRepository.save(userChallenge);
    }
    
    public UserChallenge completeChallenge(String userId, String challengeId, LocalDate date, 
                                           int correctAnswers, int totalQuestions, int timeSeconds) {
        UserChallenge userChallenge = getUserChallengeStatus(userId, challengeId, date);
        userChallenge.markCompleted(correctAnswers, totalQuestions, timeSeconds);
        return userChallengeRepository.save(userChallenge);
    }
    
    public List<UserChallenge> getUserChallenges(String userId) {
        return userChallengeRepository.findByUserId(userId);
    }
    
    public int getConsecutiveDaysCompleted(String userId) {
        LocalDate today = LocalDate.now();
        int streak = 0;
        
        for (int i = 0; i < 365; i++) {
            LocalDate checkDate = today.minusDays(i);
            int count = userChallengeRepository.countByUserIdAndCompletedAndDateBetween(
                userId, true, checkDate, checkDate);
            if (count > 0) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
    }
}
