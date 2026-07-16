package com.vuemov.repository;

import com.vuemov.model.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {
    
    Optional<Lesson> findByTitle(String title);
    
    List<Lesson> findByHskLevel(int hskLevel);
    
    List<Lesson> findByHskLevelOrderByLessonNumber(int hskLevel);
    
    Optional<Lesson> findByHskLevelAndLessonNumber(int hskLevel, int lessonNumber);
}
