package com.vuemov.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exercises")
public class Exercise {
    @Id
    private String id;
    
    @Indexed
    private String title;
    
    private String description;
    
    private String exerciseType; // listening, grammar, writing, speaking
    
    @Indexed
    private int hskLevel;
    
    @Indexed
    private String lessonId;
    
    private String question;
    
    private String audioUrl; // For listening exercises
    
    private List<String> options; // Multiple choice options
    
    private String correctAnswer;
    
    private String explanation;
    
    private List<String> tips;
    
    private int difficulty; // 1-5 scale
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Exercise(String title, String exerciseType, int hskLevel, String question) {
        this.title = title;
        this.exerciseType = exerciseType;
        this.hskLevel = hskLevel;
        this.question = question;
        this.options = new ArrayList<>();
        this.tips = new ArrayList<>();
        this.difficulty = 2;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
