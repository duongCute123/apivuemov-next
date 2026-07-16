package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_progress")
public class UserProgress {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private int currentHskLevel;
    
    private int totalXP;
    
    private int totalPoints;
    
    private int streakDays;
    
    private LocalDateTime lastActiveDate;
    
    // Map of lesson ID -> completion percentage
    private Map<String, Double> lessonProgress;
    
    // List of completed vocabulary IDs
    @Indexed
    private List<String> completedVocabularyIds;
    
    // List of completed grammar IDs
    private List<String> completedGrammarIds;
    
    // Map of vocabulary ID -> number of times reviewed
    private Map<String, Integer> vocabularyReviewCount;
    
    private int totalLessonCount;
    
    private int completedLessonCount;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public UserProgress(String userId) {
        this.userId = userId;
        this.currentHskLevel = 1;
        this.totalXP = 0;
        this.totalPoints = 0;
        this.streakDays = 0;
        this.lessonProgress = new HashMap<>();
        this.completedVocabularyIds = new ArrayList<>();
        this.completedGrammarIds = new ArrayList<>();
        this.vocabularyReviewCount = new HashMap<>();
        this.totalLessonCount = 0;
        this.completedLessonCount = 0;
        this.lastActiveDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
