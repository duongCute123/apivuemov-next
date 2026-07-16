package com.vuemov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyRequest {
    private String simplifiedChinese;
    private String traditionalChinese;
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
}
