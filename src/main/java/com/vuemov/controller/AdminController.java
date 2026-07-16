package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Value("${admin.secret-key:changeme}")
    private String adminSecretKey;

    @PostMapping("/promote")
    public ResponseEntity<ApiResponse<Void>> promoteUser(
            @RequestBody Map<String, String> body,
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {

        String email = body.get("email");
        String key = body.get("secretKey");

        if (key == null || !key.equals(adminSecretKey)) {
            if (adminKey == null || !adminKey.equals(adminSecretKey)) {
                return ResponseEntity.status(403).body(ApiResponse.error("Invalid admin key"));
            }
        }

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email is required"));
        }

        try {
            userService.promoteToAdmin(email);
            return ResponseEntity.ok(ApiResponse.success("User promoted to admin", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
