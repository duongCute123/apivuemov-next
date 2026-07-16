package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "daily_challenges")
public class DailyChallenge {
    @Id
    private String id;
    
    @Indexed
    private LocalDate date;
    
    private String title;
    
    private String description;
    
    private String challengeType; // vocabulary, listening, grammar, writing
    
    private int hskLevel;
    
    private List<String> vocabularyIds;
    
    private String audioUrl;
    
    private int pointsReward;
    
    private int xpReward;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public DailyChallenge(LocalDate date, String title, String challengeType, int hskLevel) {
        this.date = date;
        this.title = title;
        this.challengeType = challengeType;
        this.hskLevel = hskLevel;
        this.vocabularyIds = new ArrayList<>();
        this.pointsReward = 50;
        this.xpReward = 25;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
