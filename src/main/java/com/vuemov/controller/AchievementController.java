package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.model.Achievement;
import com.vuemov.model.UserAchievement;
import com.vuemov.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {
    
    @Autowired
    private AchievementService achievementService;
    
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<Void>> initializeAchievements() {
        try {
            achievementService.initializeDefaultAchievements();
            return ResponseEntity.ok(new ApiResponse<>(true, "Achievements initialized", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Achievement>>> getAchievementsByCategory(@PathVariable String category) {
        try {
            List<Achievement> achievements = achievementService.getAchievementsByCategory(category);
            return ResponseEntity.ok(new ApiResponse<>(true, "Achievements found", achievements));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/unlock/{userId}/{achievementCode}")
    public ResponseEntity<ApiResponse<Void>> unlockAchievement(
            @PathVariable String userId,
            @PathVariable String achievementCode) {
        try {
            achievementService.unlockAchievement(userId, achievementCode);
            return ResponseEntity.ok(new ApiResponse<>(true, "Achievement unlocked", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Achievement>>> getUserAchievements(@PathVariable String userId) {
        try {
            List<Achievement> achievements = achievementService.getUserUnlockedAchievements(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User achievements found", achievements));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/user/{userId}/all")
    public ResponseEntity<ApiResponse<List<UserAchievement>>> getUserAchievementRecords(@PathVariable String userId) {
        try {
            List<UserAchievement> achievements = achievementService.getUserAchievements(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "User achievement records found", achievements));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
