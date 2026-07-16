package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.UserProgressResponse;
import com.vuemov.model.UserProgress;
import com.vuemov.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {
    
    @Autowired
    private UserProgressService userProgressService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<UserProgressResponse>> getUserProgress(@PathVariable String userId) {
        try {
            UserProgressResponse progress = userProgressService.getUserProgressResponse(userId);
            if (progress != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "User progress found", progress));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User progress not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/vocabulary/{userId}/{vocabularyId}")
    public ResponseEntity<ApiResponse<Void>> markVocabularyCompleted(
            @PathVariable String userId,
            @PathVariable String vocabularyId) {
        try {
            userProgressService.markVocabularyCompleted(userId, vocabularyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabulary marked as completed", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/grammar/{userId}/{grammarId}")
    public ResponseEntity<ApiResponse<Void>> markGrammarCompleted(
            @PathVariable String userId,
            @PathVariable String grammarId) {
        try {
            userProgressService.markGrammarCompleted(userId, grammarId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammar marked as completed", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PutMapping("/lesson/{userId}/{lessonId}/{completionPercentage}")
    public ResponseEntity<ApiResponse<Void>> updateLessonProgress(
            @PathVariable String userId,
            @PathVariable String lessonId,
            @PathVariable double completionPercentage) {
        try {
            userProgressService.updateLessonProgress(userId, lessonId, completionPercentage);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lesson progress updated", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/hsk-level-up/{userId}")
    public ResponseEntity<ApiResponse<Void>> increaseHskLevel(@PathVariable String userId) {
        try {
            userProgressService.increaseHskLevel(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "HSK level increased", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/streak/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateStreak(@PathVariable String userId) {
        try {
            userProgressService.updateStreak(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Streak updated", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
