package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "practice_results")
public class PracticeResult {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String lessonId;
    
    private String practiceType; // quiz, flashcard, listening, writing
    
    private int totalQuestions;
    
    private int correctAnswers;
    
    private double scorePercentage;
    
    private int xpEarned;
    
    private List<QuestionResult> questionResults;
    
    private int timeTakenSeconds;
    
    @Indexed
    private LocalDateTime completedAt;
    
    public PracticeResult(String userId, String lessonId, String practiceType) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.practiceType = practiceType;
        this.questionResults = new ArrayList<>();
        this.completedAt = LocalDateTime.now();
    }
    
    public void calculateScore() {
        if (this.totalQuestions > 0) {
            this.scorePercentage = (double) this.correctAnswers / this.totalQuestions * 100;
            this.xpEarned = Math.max(10, (int) (this.scorePercentage / 10));
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResult {
        private String questionId;
        private String userAnswer;
        private String correctAnswer;
        private boolean isCorrect;
        private int timeTakenSeconds;
    }
}
