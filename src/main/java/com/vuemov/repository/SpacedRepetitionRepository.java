package com.vuemov.repository;

import com.vuemov.model.SpacedRepetition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpacedRepetitionRepository extends MongoRepository<SpacedRepetition, String> {
    
    Optional<SpacedRepetition> findByUserIdAndVocabularyId(String userId, String vocabularyId);
    
    List<SpacedRepetition> findByUserId(String userId);
    
    @Query("{ 'userId': ?0, 'nextReviewDate': { $lte: ?1 } }")
    List<SpacedRepetition> findDueForReview(String userId, LocalDateTime now);
    
    List<SpacedRepetition> findByUserIdOrderByNextReviewDate(String userId);
}
