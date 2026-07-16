package com.vuemov.service;

import com.vuemov.dto.AuthResponse;
import com.vuemov.dto.LoginRequest;
import com.vuemov.dto.RegisterRequest;
import com.vuemov.dto.UserResponse;
import com.vuemov.model.User;
import com.vuemov.repository.UserRepository;
import com.vuemov.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private UserProgressService userProgressService;
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setFavoriteSlugs(new ArrayList<>());
        user.setWatchHistory(new ArrayList<>());
        
        user = userRepository.save(user);
        
        // Create user progress record
        userProgressService.createUserProgress(user.getId());
        
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getAvatar(), user.getRole() != null ? user.getRole() : "user");
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getUsername(), user.getAvatar(), user.getRole() != null ? user.getRole() : "user");
    }
    
    public UserResponse getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.fromUser(user);
    }
    
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
