package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.model.AnalyticsEvent;
import com.vuemov.model.User;
import com.vuemov.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track")
    public ResponseEntity<ApiResponse<Void>> trackEvent(@RequestBody AnalyticsEvent event) {
        if (event.getUserAgent() != null && event.getUserAgent().length() > 500) {
            event.setUserAgent(event.getUserAgent().substring(0, 500));
        }
        analyticsService.logEvent(event);
        return ResponseEntity.ok(ApiResponse.success("Event tracked", null));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(@AuthenticationPrincipal User user) {
        Map<String, Object> stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/devices")
    public ResponseEntity<ApiResponse<?>> getDeviceStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getDeviceStats()));
    }

    @GetMapping("/browsers")
    public ResponseEntity<ApiResponse<?>> getBrowserStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getBrowserStats()));
    }

    @GetMapping("/os")
    public ResponseEntity<ApiResponse<?>> getOsStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getOsStats()));
    }

    @GetMapping("/top-movies")
    public ResponseEntity<ApiResponse<?>> getTopMovies(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getTopMovies(limit)));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<?>> getRecentActivity(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getRecentActivity(limit)));
    }

    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<?>> getVisitTrend(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getVisitTrend(days)));
    }
}
