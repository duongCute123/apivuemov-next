package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.service.DataSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seed")
@CrossOrigin(origins = "*")
public class SeedController {

    @Autowired
    private DataSeederService dataSeederService;

    @PostMapping("/vocabulary/{level}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> seedVocabulary(@PathVariable int level) {
        try {
            String basePath = "data/hsk" + level + "_vocabulary.csv";
            String extraPath = "data/hsk" + level + "_extra.csv";
            int count = dataSeederService.importVocabularyFromCsv(basePath, extraPath);
            Map<String, Object> result = new HashMap<>();
            result.put("level", level);
            result.put("imported", count);
            return ResponseEntity.ok(new ApiResponse<>(true, "Imported " + count + " vocabulary items", result));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", e.getMessage());
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), result));
        }
    }

    @PostMapping("/grammar/{level}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> seedGrammar(@PathVariable int level) {
        try {
            String filePath = "data/hsk" + level + "_grammar.csv";
            int count = dataSeederService.importGrammarFromCsv(filePath);
            Map<String, Object> result = new HashMap<>();
            result.put("level", level);
            result.put("imported", count);
            return ResponseEntity.ok(new ApiResponse<>(true, "Imported " + count + " grammar items", result));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", e.getMessage());
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), result));
        }
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<Map<String, Object>>> seedAll() {
        try {
            Map<String, Object> result = new HashMap<>();
            int totalVocab = 0;
            int totalGrammar = 0;
            
            for (int level = 1; level <= 9; level++) {
                String basePath = "data/hsk" + level + "_vocabulary.csv";
                String extraPath = "data/hsk" + level + "_extra.csv";
                int vCount = dataSeederService.importVocabularyFromCsv(basePath, extraPath);
                totalVocab += vCount;
                
                int gCount = dataSeederService.importGrammarFromCsv("data/hsk" + level + "_grammar.csv");
                totalGrammar += gCount;
            }
            
            int lessons = dataSeederService.createSeedLessons();
            int practiceVideos = dataSeederService.importPracticeVideosFromCsv("data/practice_videos_zhongwen.csv");
            int exercises = dataSeederService.createSeedExercises();
            int achievements = dataSeederService.createSeedAchievements();
            
            result.put("vocabularyImported", totalVocab);
            result.put("grammarImported", totalGrammar);
            result.put("lessonsCreated", lessons);
            result.put("practiceVideosImported", practiceVideos);
            result.put("exercisesCreated", exercises);
            result.put("achievementsCreated", achievements);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Seed data imported successfully", result));
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("error", e.getMessage());
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), result));
        }
    }

    @PostMapping("/lessons")
    public ResponseEntity<ApiResponse<Integer>> seedLessons() {
        int count = dataSeederService.createSeedLessons();
        return ResponseEntity.ok(new ApiResponse<>(true, "Created " + count + " lessons", count));
    }

    @PostMapping("/exercises")
    public ResponseEntity<ApiResponse<Integer>> seedExercises() {
        int count = dataSeederService.createSeedExercises();
        return ResponseEntity.ok(new ApiResponse<>(true, "Created " + count + " exercises", count));
    }

    @PostMapping("/achievements")
    public ResponseEntity<ApiResponse<Integer>> seedAchievements() {
        int count = dataSeederService.createSeedAchievements();
        return ResponseEntity.ok(new ApiResponse<>(true, "Created " + count + " achievements", count));
    }
}
