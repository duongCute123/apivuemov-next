package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.model.Exercise;
import com.vuemov.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {
    
    @Autowired
    private ExerciseService exerciseService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Exercise>> createExercise(@RequestBody Exercise exercise) {
        try {
            Exercise created = exerciseService.createExercise(exercise);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercise created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Exercise>> getExercise(@PathVariable String id) {
        try {
            Optional<Exercise> exercise = exerciseService.getExerciseById(id);
            if (exercise.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Exercise found", exercise.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Exercise not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Exercise>> updateExercise(
            @PathVariable String id,
            @RequestBody Exercise exercise) {
        try {
            Exercise updated = exerciseService.updateExercise(id, exercise);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercise updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExercise(@PathVariable String id) {
        try {
            exerciseService.deleteExercise(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercise deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/type/{exerciseType}")
    public ResponseEntity<ApiResponse<List<Exercise>>> getExercisesByType(@PathVariable String exerciseType) {
        try {
            List<Exercise> exercises = exerciseService.getExercisesByType(exerciseType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercises found", exercises));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{hskLevel}")
    public ResponseEntity<ApiResponse<List<Exercise>>> getExercisesByLevel(@PathVariable int hskLevel) {
        try {
            List<Exercise> exercises = exerciseService.getExercisesByHskLevel(hskLevel);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercises found", exercises));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<Exercise>>> getExercisesByLesson(@PathVariable String lessonId) {
        try {
            List<Exercise> exercises = exerciseService.getExercisesByLesson(lessonId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercises found", exercises));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/lesson/{lessonId}/type/{exerciseType}")
    public ResponseEntity<ApiResponse<List<Exercise>>> getExercisesByLessonAndType(
            @PathVariable String lessonId,
            @PathVariable String exerciseType) {
        try {
            List<Exercise> exercises = exerciseService.getExercisesByLessonAndType(lessonId, exerciseType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Exercises found", exercises));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
