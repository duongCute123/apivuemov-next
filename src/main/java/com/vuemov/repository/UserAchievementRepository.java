package com.vuemov.repository;

import com.vuemov.model.UserAchievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends MongoRepository<UserAchievement, String> {
    
    List<UserAchievement> findByUserId(String userId);
    
    boolean existsByUserIdAndAchievementCode(String userId, String achievementCode);
}
