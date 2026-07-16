package com.vuemov.repository;

import com.vuemov.model.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    
    List<Exercise> findByExerciseType(String exerciseType);
    
    List<Exercise> findByHskLevel(int hskLevel);
    
    List<Exercise> findByLessonId(String lessonId);
    
    List<Exercise> findByLessonIdAndExerciseType(String lessonId, String exerciseType);
}
