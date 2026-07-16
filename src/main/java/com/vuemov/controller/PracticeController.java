package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.PracticeResultRequest;
import com.vuemov.model.PracticeResult;
import com.vuemov.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
@CrossOrigin(origins = "*")
public class PracticeController {
    
    @Autowired
    private PracticeService practiceService;
    
    @PostMapping("/record/{userId}")
    public ResponseEntity<ApiResponse<PracticeResult>> recordPractice(
            @PathVariable String userId,
            @RequestBody PracticeResultRequest request) {
        try {
            PracticeResult result = practiceService.recordPracticeResult(userId, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Practice result recorded successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PracticeResult>> getPracticeResult(@PathVariable String id) {
        try {
            PracticeResult result = practiceService.getPracticeResultById(id);
            if (result != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Practice result found", result));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Practice result not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PracticeResult>>> getUserPracticeResults(@PathVariable String userId) {
        try {
            List<PracticeResult> results = practiceService.getUserPracticeResults(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Practice results found", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/user/{userId}/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<List<PracticeResult>>> getLessonPracticeResults(
            @PathVariable String userId,
            @PathVariable String lessonId) {
        try {
            List<PracticeResult> results = practiceService.getLessonPracticeResults(userId, lessonId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Practice results found", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/user/{userId}/type/{practiceType}")
    public ResponseEntity<ApiResponse<List<PracticeResult>>> getPracticeByType(
            @PathVariable String userId,
            @PathVariable String practiceType) {
        try {
            List<PracticeResult> results = practiceService.getPracticeByType(userId, practiceType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Practice results found", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
