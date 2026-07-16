package com.vuemov.repository;

import com.vuemov.model.UserChallenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserChallengeRepository extends MongoRepository<UserChallenge, String> {
    
    Optional<UserChallenge> findByUserIdAndDailyChallengeIdAndDate(String userId, String challengeId, LocalDate date);
    
    List<UserChallenge> findByUserId(String userId);
    
    List<UserChallenge> findByUserIdAndDate(String userId, LocalDate date);
    
    List<UserChallenge> findByUserIdAndCompleted(String userId, boolean completed);
    
    int countByUserIdAndCompletedAndDateBetween(String userId, boolean completed, LocalDate startDate, LocalDate endDate);
}
