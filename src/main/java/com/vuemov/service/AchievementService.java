package com.vuemov.service;

import com.vuemov.model.Achievement;
import com.vuemov.model.UserAchievement;
import com.vuemov.repository.AchievementRepository;
import com.vuemov.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AchievementService {
    
    @Autowired
    private AchievementRepository achievementRepository;
    
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    
    public Achievement createAchievement(String code, String title, String description, String category) {
        Achievement achievement = new Achievement(code, title, description, category);
        return achievementRepository.save(achievement);
    }
    
    public Optional<Achievement> getAchievementByCode(String code) {
        return achievementRepository.findByAchievementCode(code);
    }
    
    public List<Achievement> getAchievementsByCategory(String category) {
        return achievementRepository.findByCategory(category);
    }
    
    public void unlockAchievement(String userId, String achievementCode) {
        Optional<Achievement> achievement = achievementRepository.findByAchievementCode(achievementCode);
        
        if (achievement.isPresent()) {
            // Check if already unlocked
            if (!userAchievementRepository.existsByUserIdAndAchievementCode(userId, achievementCode)) {
                UserAchievement userAchievement = new UserAchievement(
                    userId, 
                    achievement.get().getId(), 
                    achievementCode
                );
                userAchievementRepository.save(userAchievement);
            }
        }
    }
    
    public List<UserAchievement> getUserAchievements(String userId) {
        return userAchievementRepository.findByUserId(userId);
    }
    
    public List<Achievement> getUserUnlockedAchievements(String userId) {
        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(userId);
        return userAchievements.stream()
            .map(ua -> achievementRepository.findById(ua.getAchievementId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    public void initializeDefaultAchievements() {
        // Milestone achievements
        createAchievementIfNotExists("first_word", "First Word", "Learn your first word", "milestone");
        createAchievementIfNotExists("ten_words", "Word Collector", "Learn 10 words", "milestone");
        createAchievementIfNotExists("fifty_words", "Word Master", "Learn 50 words", "milestone");
        createAchievementIfNotExists("hundred_words", "Vocabulary Pro", "Learn 100 words", "milestone");
        
        // Streak achievements
        createAchievementIfNotExists("three_day_streak", "Getting Started", "3 day streak", "streak");
        createAchievementIfNotExists("week_streak", "Week Warrior", "7 day streak", "streak");
        createAchievementIfNotExists("month_streak", "Month Master", "30 day streak", "streak");
        
        // Practice achievements
        createAchievementIfNotExists("first_practice", "First Test", "Complete your first practice", "practice");
        createAchievementIfNotExists("perfect_score", "Perfect!", "Score 100% on a practice", "practice");
        createAchievementIfNotExists("challenge_master", "Daily Champion", "Complete daily challenge", "practice");
    }
    
    private void createAchievementIfNotExists(String code, String title, String description, String category) {
        if (achievementRepository.findByAchievementCode(code).isEmpty()) {
            createAchievement(code, title, description, category);
        }
    }
}
