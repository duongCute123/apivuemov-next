package com.vuemov.repository;

import com.vuemov.model.DailyChallenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface DailyChallengeRepository extends MongoRepository<DailyChallenge, String> {
    
    Optional<DailyChallenge> findByDate(LocalDate date);
    
    List<DailyChallenge> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<DailyChallenge> findByHskLevel(int hskLevel);
}
