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
@Document(collection = "user_achievements")
public class UserAchievement {
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String achievementId;
    
    private String achievementCode;
    
    @Indexed
    private LocalDateTime unlockedAt;
    
    public UserAchievement(String userId, String achievementId, String achievementCode) {
        this.userId = userId;
        this.achievementId = achievementId;
        this.achievementCode = achievementCode;
        this.unlockedAt = LocalDateTime.now();
    }
}
