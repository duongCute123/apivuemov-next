package com.vuemov.service;

import com.vuemov.model.User;
import com.vuemov.model.WatchHistoryItem;
import com.vuemov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public List<String> getFavorites(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavoriteSlugs() != null ? user.getFavoriteSlugs() : new ArrayList<>();
    }
    
    public void addFavorite(String userId, String slug) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getFavoriteSlugs() == null) {
            user.setFavoriteSlugs(new ArrayList<>());
        }
        
        if (!user.getFavoriteSlugs().contains(slug)) {
            user.getFavoriteSlugs().add(slug);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    public void removeFavorite(String userId, String slug) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getFavoriteSlugs() != null) {
            user.getFavoriteSlugs().remove(slug);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    public boolean isFavorite(String userId, String slug) {
        User user = userRepository.findById(userId)
                .orElse(null);
        return user != null && user.getFavoriteSlugs() != null && user.getFavoriteSlugs().contains(slug);
    }
    
    public List<WatchHistoryItem> getHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWatchHistory() != null ? user.getWatchHistory() : new ArrayList<>();
    }
    
    public void addHistory(String userId, String slug) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getWatchHistory() == null) {
            user.setWatchHistory(new ArrayList<>());
        }
        
        user.getWatchHistory().removeIf(item -> item.getSlug().equals(slug));
        
        WatchHistoryItem newItem = new WatchHistoryItem(slug, LocalDateTime.now());
        user.getWatchHistory().add(0, newItem);
        
        if (user.getWatchHistory().size() > 100) {
            user.setWatchHistory(new ArrayList<>(user.getWatchHistory().subList(0, 100)));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    public void removeHistory(String userId, String slug) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getWatchHistory() != null) {
            user.getWatchHistory().removeIf(item -> item.getSlug().equals(slug));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    public void clearHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setWatchHistory(new ArrayList<>());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
