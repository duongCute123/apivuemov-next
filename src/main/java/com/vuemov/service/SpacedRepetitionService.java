package com.vuemov.service;

import com.vuemov.model.SpacedRepetition;
import com.vuemov.repository.SpacedRepetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpacedRepetitionService {
    
    @Autowired
    private SpacedRepetitionRepository spacedRepetitionRepository;
    
    public SpacedRepetition initializeWord(String userId, String vocabularyId) {
        Optional<SpacedRepetition> existing = spacedRepetitionRepository
            .findByUserIdAndVocabularyId(userId, vocabularyId);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        SpacedRepetition sr = new SpacedRepetition(userId, vocabularyId);
        return spacedRepetitionRepository.save(sr);
    }
    
    public void recordCorrectAnswer(String userId, String vocabularyId) {
        Optional<SpacedRepetition> sr = spacedRepetitionRepository
            .findByUserIdAndVocabularyId(userId, vocabularyId);
        
        if (sr.isPresent()) {
            SpacedRepetition review = sr.get();
            review.recordCorrectReview();
            spacedRepetitionRepository.save(review);
        }
    }
    
    public void recordIncorrectAnswer(String userId, String vocabularyId) {
        Optional<SpacedRepetition> sr = spacedRepetitionRepository
            .findByUserIdAndVocabularyId(userId, vocabularyId);
        
        if (sr.isPresent()) {
            SpacedRepetition review = sr.get();
            review.recordIncorrectReview();
            spacedRepetitionRepository.save(review);
        }
    }
    
    public List<SpacedRepetition> getDueForReview(String userId) {
        return spacedRepetitionRepository.findDueForReview(userId, LocalDateTime.now());
    }
    
    public List<SpacedRepetition> getUserVocabularyProgress(String userId) {
        return spacedRepetitionRepository.findByUserIdOrderByNextReviewDate(userId);
    }
    
    public int getDueCount(String userId) {
        return getDueForReview(userId).size();
    }
}
