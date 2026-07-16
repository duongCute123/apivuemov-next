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
@Document(collection = "achievements")
public class Achievement {
    @Id
    private String id;
    
    @Indexed
    private String achievementCode; // e.g., "first_word", "100_xp", "week_streak_7"
    
    private String title;
    
    private String description;
    
    private String badgeImageUrl;
    
    private String category; // milestone, streak, practice, social
    
    private int pointsReward;
    
    private int xpReward;
    
    private String condition; // Description of how to unlock
    
    @Indexed
    private LocalDateTime createdAt;
    
    public Achievement(String achievementCode, String title, String description, String category) {
        this.achievementCode = achievementCode;
        this.title = title;
        this.description = description;
        this.category = category;
        this.pointsReward = 10;
        this.xpReward = 5;
        this.createdAt = LocalDateTime.now();
    }
}
