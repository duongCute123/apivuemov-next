package com.vuemov.dto;

import com.vuemov.model.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String username;
    private String avatar;
    private String role;
    private LocalDateTime createdAt;
    private int favoriteCount;
    private int historyCount;
    
    public static UserResponse fromUser(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole() != null ? user.getRole() : "user");
        response.setCreatedAt(user.getCreatedAt());
        response.setFavoriteCount(user.getFavoriteSlugs() != null ? user.getFavoriteSlugs().size() : 0);
        response.setHistoryCount(user.getWatchHistory() != null ? user.getWatchHistory().size() : 0);
        return response;
    }
}
