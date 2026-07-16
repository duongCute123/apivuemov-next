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
@Document(collection = "vocabulary")
public class Vocabulary {
    @Id
    private String id;
    
    @Indexed
    private String simplifiedChinese;
    
    private String traditionalChinese;
    
    @Indexed
    private String pinyin;
    
    private String englishMeaning;
    
    private String vietnameseMeaning;
    
    private String partOfSpeech;
    
    private int hskLevel;
    
    private String exampleSentence;
    
    private String exampleTranslation;
    
    private String audioUrl;
    
    private String imageUrl;
    
    private List<String> mnemonicTips;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Vocabulary(String simplifiedChinese, String pinyin, String englishMeaning, 
                      String vietnameseMeaning, int hskLevel) {
        this.simplifiedChinese = simplifiedChinese;
        this.pinyin = pinyin;
        this.englishMeaning = englishMeaning;
        this.vietnameseMeaning = vietnameseMeaning;
        this.hskLevel = hskLevel;
        this.mnemonicTips = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
