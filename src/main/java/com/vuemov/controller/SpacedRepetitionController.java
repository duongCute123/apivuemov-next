package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.model.SpacedRepetition;
import com.vuemov.service.SpacedRepetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/srs")
@CrossOrigin(origins = "*")
public class SpacedRepetitionController {
    
    @Autowired
    private SpacedRepetitionService spacedRepetitionService;
    
    @PostMapping("/init/{userId}/{vocabularyId}")
    public ResponseEntity<ApiResponse<SpacedRepetition>> initializeWord(
            @PathVariable String userId,
            @PathVariable String vocabularyId) {
        try {
            SpacedRepetition sr = spacedRepetitionService.initializeWord(userId, vocabularyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Word initialized for SRS", sr));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/correct/{userId}/{vocabularyId}")
    public ResponseEntity<ApiResponse<Void>> recordCorrectAnswer(
            @PathVariable String userId,
            @PathVariable String vocabularyId) {
        try {
            spacedRepetitionService.recordCorrectAnswer(userId, vocabularyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Correct answer recorded", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/incorrect/{userId}/{vocabularyId}")
    public ResponseEntity<ApiResponse<Void>> recordIncorrectAnswer(
            @PathVariable String userId,
            @PathVariable String vocabularyId) {
        try {
            spacedRepetitionService.recordIncorrectAnswer(userId, vocabularyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Incorrect answer recorded", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/due/{userId}")
    public ResponseEntity<ApiResponse<List<SpacedRepetition>>> getDueForReview(@PathVariable String userId) {
        try {
            List<SpacedRepetition> dueItems = spacedRepetitionService.getDueForReview(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Due items retrieved", dueItems));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/progress/{userId}")
    public ResponseEntity<ApiResponse<List<SpacedRepetition>>> getUserVocabularyProgress(@PathVariable String userId) {
        try {
            List<SpacedRepetition> progress = spacedRepetitionService.getUserVocabularyProgress(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Progress retrieved", progress));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/due-count/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getDueCount(@PathVariable String userId) {
        try {
            int dueCount = spacedRepetitionService.getDueCount(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Due count retrieved", dueCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
