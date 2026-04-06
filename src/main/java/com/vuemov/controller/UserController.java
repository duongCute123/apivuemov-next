package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.UserResponse;
import com.vuemov.model.User;
import com.vuemov.model.WatchHistoryItem;
import com.vuemov.service.AuthService;
import com.vuemov.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final AuthService authService;
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@AuthenticationPrincipal User user) {
        UserResponse profile = authService.getProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<String>>> getFavorites(@AuthenticationPrincipal User user) {
        List<String> favorites = userService.getFavorites(user.getId());
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
    
    @PostMapping("/favorites/{slug}")
    public ResponseEntity<ApiResponse<Void>> addFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable String slug) {
        userService.addFavorite(user.getId(), slug);
        return ResponseEntity.ok(ApiResponse.success("Added to favorites", null));
    }
    
    @DeleteMapping("/favorites/{slug}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable String slug) {
        userService.removeFavorite(user.getId(), slug);
        return ResponseEntity.ok(ApiResponse.success("Removed from favorites", null));
    }
    
    @GetMapping("/favorites/{slug}/check")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable String slug) {
        boolean isFavorite = userService.isFavorite(user.getId(), slug);
        return ResponseEntity.ok(ApiResponse.success(Map.of("isFavorite", isFavorite)));
    }
    
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<WatchHistoryItem>>> getHistory(@AuthenticationPrincipal User user) {
        List<WatchHistoryItem> history = userService.getHistory(user.getId());
        return ResponseEntity.ok(ApiResponse.success(history));
    }
    
    @PostMapping("/history/{slug}")
    public ResponseEntity<ApiResponse<Void>> addHistory(
            @AuthenticationPrincipal User user,
            @PathVariable String slug) {
        userService.addHistory(user.getId(), slug);
        return ResponseEntity.ok(ApiResponse.success("Added to history", null));
    }
    
    @DeleteMapping("/history/{slug}")
    public ResponseEntity<ApiResponse<Void>> removeHistory(
            @AuthenticationPrincipal User user,
            @PathVariable String slug) {
        userService.removeHistory(user.getId(), slug);
        return ResponseEntity.ok(ApiResponse.success("Removed from history", null));
    }
    
    @DeleteMapping("/history")
    public ResponseEntity<ApiResponse<Void>> clearHistory(@AuthenticationPrincipal User user) {
        userService.clearHistory(user.getId());
        return ResponseEntity.ok(ApiResponse.success("History cleared", null));
    }
}
