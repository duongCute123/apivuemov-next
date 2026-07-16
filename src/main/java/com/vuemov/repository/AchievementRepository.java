package com.vuemov.repository;

import com.vuemov.model.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {
    
    Optional<Achievement> findByAchievementCode(String code);
    
    List<Achievement> findByCategory(String category);
}
