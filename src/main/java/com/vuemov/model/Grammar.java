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
@Document(collection = "grammar")
public class Grammar {
    @Id
    private String id;
    
    @Indexed
    private String title;
    
    private String explanation;
    
    @Indexed
    private int hskLevel;
    
    private String grammarPattern;
    
    private List<String> exampleSentences;
    
    private List<String> exampleTranslations;
    
    private String usageNote;
    
    private String videoUrl;
    
    private List<String> similarGrammar;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Grammar(String title, String explanation, int hskLevel, String grammarPattern) {
        this.title = title;
        this.explanation = explanation;
        this.hskLevel = hskLevel;
        this.grammarPattern = grammarPattern;
        this.exampleSentences = new ArrayList<>();
        this.exampleTranslations = new ArrayList<>();
        this.similarGrammar = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
