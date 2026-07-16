package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.model.DailyChallenge;
import com.vuemov.model.UserChallenge;
import com.vuemov.service.DailyChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/daily-challenge")
@CrossOrigin(origins = "*")
public class DailyChallengeController {
    
    @Autowired
    private DailyChallengeService dailyChallengeService;
    
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<DailyChallenge>> getTodaysChallenge() {
        try {
            Optional<DailyChallenge> challenge = dailyChallengeService.getTodaysChallenge();
            if (challenge.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Today's challenge found", challenge.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No challenge for today", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<DailyChallenge>> getChallengeByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Optional<DailyChallenge> challenge = dailyChallengeService.getChallengeByDate(localDate);
            if (challenge.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Challenge found", challenge.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Challenge not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/status/{userId}")
    public ResponseEntity<ApiResponse<UserChallenge>> getUserChallengeStatus(
            @PathVariable String userId,
            @RequestParam String challengeId,
            @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            UserChallenge userChallenge = dailyChallengeService.getUserChallengeStatus(userId, challengeId, localDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Status retrieved", userChallenge));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/complete/{userId}")
    public ResponseEntity<ApiResponse<UserChallenge>> completeChallenge(
            @PathVariable String userId,
            @RequestParam String challengeId,
            @RequestParam String date,
            @RequestParam int correctAnswers,
            @RequestParam int totalQuestions,
            @RequestParam int timeSeconds) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            UserChallenge completed = dailyChallengeService.completeChallenge(
                userId, challengeId, localDate, correctAnswers, totalQuestions, timeSeconds
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Challenge completed", completed));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
    
    @GetMapping("/streak/{userId}")
    public ResponseEntity<ApiResponse<Integer>> getStreak(@PathVariable String userId) {
        try {
            int streak = dailyChallengeService.getConsecutiveDaysCompleted(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Streak retrieved", streak));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
