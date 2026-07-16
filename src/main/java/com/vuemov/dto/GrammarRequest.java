package com.vuemov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarRequest {
    private String title;
    private String explanation;
    private int hskLevel;
    private String grammarPattern;
    private List<String> exampleSentences;
    private List<String> exampleTranslations;
    private String usageNote;
    private String videoUrl;
    private List<String> similarGrammar;
}
