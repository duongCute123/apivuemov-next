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
@Document(collection = "lessons")
public class Lesson {
    @Id
    private String id;
    
    @Indexed
    private String title;
    
    private String description;
    
    @Indexed
    private int hskLevel;
    
    @Indexed
    private int lessonNumber;
    
    private String content;
    
    private String audioUrl;
    
    private String videoUrl;
    
    private List<String> vocabularyIds;
    
    private List<String> grammarIds;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Lesson(String title, String description, int hskLevel, int lessonNumber) {
        this.title = title;
        this.description = description;
        this.hskLevel = hskLevel;
        this.lessonNumber = lessonNumber;
        this.vocabularyIds = new ArrayList<>();
        this.grammarIds = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
