package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.LessonRequest;
import com.vuemov.model.Lesson;
import com.vuemov.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {
    
    @Autowired
    private LessonService lessonService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Lesson>> createLesson(@RequestBody LessonRequest request) {
        try {
            Lesson lesson = lessonService.createLesson(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lesson created successfully", lesson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> getLesson(@PathVariable String id) {
        try {
            Optional<Lesson> lesson = lessonService.getLessonById(id);
            if (lesson.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Lesson found", lesson.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Lesson not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Lesson>> updateLesson(
            @PathVariable String id,
            @RequestBody LessonRequest request) {
        try {
            Lesson lesson = lessonService.updateLesson(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lesson updated successfully", lesson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(@PathVariable String id) {
        try {
            lessonService.deleteLesson(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lesson deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{hskLevel}")
    public ResponseEntity<ApiResponse<List<Lesson>>> getLessonsByLevel(@PathVariable int hskLevel) {
        try {
            List<Lesson> lessons = lessonService.getLessonsByHskLevel(hskLevel);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lessons found", lessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{hskLevel}/{lessonNumber}")
    public ResponseEntity<ApiResponse<Lesson>> getLessonByLevelAndNumber(
            @PathVariable int hskLevel,
            @PathVariable int lessonNumber) {
        try {
            Optional<Lesson> lesson = lessonService.getLessonByHskLevelAndNumber(hskLevel, lessonNumber);
            if (lesson.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Lesson found", lesson.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Lesson not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Lesson>>> getAllLessons() {
        try {
            List<Lesson> lessons = lessonService.getAllLessons();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lessons found", lessons));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
