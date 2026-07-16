package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_challenges")
public class UserChallenge {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String dailyChallengeId;
    
    @Indexed
    private LocalDate date;
    
    private boolean completed;
    
    private int correctAnswers;
    
    private int totalQuestions;
    
    private double scorePercentage;
    
    private int pointsEarned;
    
    private int xpEarned;
    
    private int timeTakenSeconds;
    
    @Indexed
    private LocalDateTime completedAt;
    
    private LocalDateTime createdAt;
    
    public UserChallenge(String userId, String dailyChallengeId, LocalDate date) {
        this.userId = userId;
        this.dailyChallengeId = dailyChallengeId;
        this.date = date;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public void markCompleted(int correct, int total, int timeSeconds) {
        this.completed = true;
        this.correctAnswers = correct;
        this.totalQuestions = total;
        this.scorePercentage = (double) correct / total * 100;
        this.pointsEarned = (int) (this.scorePercentage / 10 * 5);
        this.xpEarned = (int) (this.scorePercentage / 10 * 2);
        this.timeTakenSeconds = timeSeconds;
        this.completedAt = LocalDateTime.now();
    }
}
