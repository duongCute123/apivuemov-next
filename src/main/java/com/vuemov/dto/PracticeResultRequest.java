package com.vuemov.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticeResultRequest {
    private String lessonId;
    private String practiceType; // quiz, flashcard, listening, writing
    private int totalQuestions;
    private int correctAnswers;
    private int timeTakenSeconds;
    private List<PracticeResultRequest.QuestionResultRequest> questionResults;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResultRequest {
        private String questionId;
        private String userAnswer;
        private String correctAnswer;
        private int timeTakenSeconds;
    }
}
