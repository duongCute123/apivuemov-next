package com.vuemov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgressResponse {
    private String id;
    private String userId;
    private int currentHskLevel;
    private int totalXP;
    private int totalPoints;
    private int streakDays;
    private Map<String, Double> lessonProgress;
    private List<String> completedVocabularyIds;
    private List<String> completedGrammarIds;
    private int totalLessonCount;
    private int completedLessonCount;
}
