package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchHistoryItem {
    private String slug;
    private LocalDateTime watchedAt;
}
