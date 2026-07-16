package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "spaced_repetition")
public class SpacedRepetition {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String vocabularyId;
    
    // SRS intervals (in days): 1, 3, 7, 14, 30, 90
    private int interval;
    
    private double easeFactor; // 1.3 - 2.5 (default 2.0)
    
    private int repetitions;
    
    @Indexed
    private LocalDateTime nextReviewDate;
    
    private LocalDateTime lastReviewDate;
    
    private int correctCount;
    
    private int incorrectCount;
    
    private LocalDateTime createdAt;
    
    public SpacedRepetition(String userId, String vocabularyId) {
        this.userId = userId;
        this.vocabularyId = vocabularyId;
        this.interval = 1;
        this.easeFactor = 2.0;
        this.repetitions = 0;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.nextReviewDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
    
    public void recordCorrectReview() {
        this.correctCount++;
        this.repetitions++;
        this.easeFactor = Math.max(1.3, this.easeFactor + (0.1 - (5 - 5) * (0.08 + (5 - 5) * 0.02)));
        updateInterval();
    }
    
    public void recordIncorrectReview() {
        this.incorrectCount++;
        this.repetitions = 0;
        this.easeFactor = Math.max(1.3, this.easeFactor - 0.2);
        this.interval = 1;
        this.nextReviewDate = LocalDateTime.now().plusDays(1);
    }
    
    private void updateInterval() {
        if (this.repetitions == 1) {
            this.interval = 1;
        } else if (this.repetitions == 2) {
            this.interval = 3;
        } else {
            this.interval = (int) Math.round(this.interval * this.easeFactor);
        }
        this.nextReviewDate = LocalDateTime.now().plusDays(this.interval);
        this.lastReviewDate = LocalDateTime.now();
    }
}
