package com.vuemov.service;

import com.vuemov.model.Exercise;
import com.vuemov.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {
    
    @Autowired
    private ExerciseRepository exerciseRepository;
    
    public Exercise createExercise(Exercise exercise) {
        exercise.setCreatedAt(LocalDateTime.now());
        exercise.setUpdatedAt(LocalDateTime.now());
        return exerciseRepository.save(exercise);
    }
    
    public Optional<Exercise> getExerciseById(String id) {
        return exerciseRepository.findById(id);
    }
    
    public Exercise updateExercise(String id, Exercise exerciseData) {
        Optional<Exercise> existing = exerciseRepository.findById(id);
        if (existing.isPresent()) {
            Exercise exercise = existing.get();
            exercise.setTitle(exerciseData.getTitle());
            exercise.setDescription(exerciseData.getDescription());
            exercise.setExerciseType(exerciseData.getExerciseType());
            exercise.setQuestion(exerciseData.getQuestion());
            exercise.setOptions(exerciseData.getOptions());
            exercise.setCorrectAnswer(exerciseData.getCorrectAnswer());
            exercise.setExplanation(exerciseData.getExplanation());
            exercise.setTips(exerciseData.getTips());
            exercise.setDifficulty(exerciseData.getDifficulty());
            exercise.setUpdatedAt(LocalDateTime.now());
            
            return exerciseRepository.save(exercise);
        }
        throw new RuntimeException("Exercise not found");
    }
    
    public void deleteExercise(String id) {
        exerciseRepository.deleteById(id);
    }
    
    public List<Exercise> getExercisesByType(String exerciseType) {
        return exerciseRepository.findByExerciseType(exerciseType);
    }
    
    public List<Exercise> getExercisesByHskLevel(int hskLevel) {
        return exerciseRepository.findByHskLevel(hskLevel);
    }
    
    public List<Exercise> getExercisesByLesson(String lessonId) {
        return exerciseRepository.findByLessonId(lessonId);
    }
    
    public List<Exercise> getExercisesByLessonAndType(String lessonId, String exerciseType) {
        return exerciseRepository.findByLessonIdAndExerciseType(lessonId, exerciseType);
    }
}
