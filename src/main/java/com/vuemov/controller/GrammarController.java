package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.GrammarRequest;
import com.vuemov.model.Grammar;
import com.vuemov.service.GrammarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grammar")
@CrossOrigin(origins = "*")
public class GrammarController {
    
    @Autowired
    private GrammarService grammarService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Grammar>> createGrammar(@RequestBody GrammarRequest request) {
        try {
            Grammar grammar = grammarService.createGrammar(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammar created successfully", grammar));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Grammar>> getGrammar(@PathVariable String id) {
        try {
            Optional<Grammar> grammar = grammarService.getGrammarById(id);
            if (grammar.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Grammar found", grammar.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Grammar not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Grammar>> updateGrammar(
            @PathVariable String id,
            @RequestBody GrammarRequest request) {
        try {
            Grammar grammar = grammarService.updateGrammar(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammar updated successfully", grammar));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGrammar(@PathVariable String id) {
        try {
            grammarService.deleteGrammar(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammar deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/level/{hskLevel}")
    public ResponseEntity<ApiResponse<List<Grammar>>> getGrammarByLevel(@PathVariable int hskLevel) {
        try {
            List<Grammar> grammars = grammarService.getGrammarByHskLevel(hskLevel);
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammars found", grammars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Grammar>>> getAllGrammar() {
        try {
            List<Grammar> grammars = grammarService.getAllGrammar();
            return ResponseEntity.ok(new ApiResponse<>(true, "Grammars found", grammars));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
