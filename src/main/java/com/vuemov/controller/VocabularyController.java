package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.VocabularyRequest;
import com.vuemov.model.Vocabulary;
import com.vuemov.service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vocabulary")
@CrossOrigin(origins = "*")
public class VocabularyController {
    
    @Autowired
    private VocabularyService vocabularyService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Vocabulary>> createVocabulary(@RequestBody VocabularyRequest request) {
        try {
            Vocabulary vocabulary = vocabularyService.createVocabulary(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabulary created successfully", vocabulary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Vocabulary>> getVocabulary(@PathVariable String id) {
        try {
            Optional<Vocabulary> vocabulary = vocabularyService.getVocabularyById(id);
            if (vocabulary.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Vocabulary found", vocabulary.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Vocabulary not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Vocabulary>> updateVocabulary(
            @PathVariable String id,
            @RequestBody VocabularyRequest request) {
        try {
            Vocabulary vocabulary = vocabularyService.updateVocabulary(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabulary updated successfully", vocabulary));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVocabulary(@PathVariable String id) {
        try {
            vocabularyService.deleteVocabulary(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabulary deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{hskLevel}")
    public ResponseEntity<ApiResponse<List<Vocabulary>>> getVocabularyByLevel(@PathVariable int hskLevel) {
        try {
            List<Vocabulary> vocabularies = vocabularyService.getVocabularyByHskLevel(hskLevel);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabularies found", vocabularies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{minLevel}/{maxLevel}")
    public ResponseEntity<ApiResponse<List<Vocabulary>>> getVocabularyByLevelRange(
            @PathVariable int minLevel,
            @PathVariable int maxLevel) {
        try {
            List<Vocabulary> vocabularies = vocabularyService.getVocabularyByHskLevelRange(minLevel, maxLevel);
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabularies found", vocabularies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Vocabulary>>> getAllVocabularies() {
        try {
            List<Vocabulary> vocabularies = vocabularyService.getAllVocabulary();
            return ResponseEntity.ok(new ApiResponse<>(true, "Vocabularies found", vocabularies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
