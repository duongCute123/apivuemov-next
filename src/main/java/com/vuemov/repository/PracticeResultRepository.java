package com.vuemov.repository;

import com.vuemov.model.PracticeResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PracticeResultRepository extends MongoRepository<PracticeResult, String> {
    
    List<PracticeResult> findByUserId(String userId);
    
    List<PracticeResult> findByUserIdAndLessonId(String userId, String lessonId);
    
    List<PracticeResult> findByUserIdOrderByCompletedAtDesc(String userId);
    
    List<PracticeResult> findByUserIdAndPracticeType(String userId, String practiceType);
}
