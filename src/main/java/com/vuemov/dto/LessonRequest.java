package com.vuemov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
    private String title;
    private String description;
    private int hskLevel;
    private int lessonNumber;
    private String content;
    private String audioUrl;
    private String videoUrl;
    private List<String> vocabularyIds;
    private List<String> grammarIds;
}
