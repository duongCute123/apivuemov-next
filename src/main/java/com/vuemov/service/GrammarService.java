package com.vuemov.service;

import com.vuemov.dto.GrammarRequest;
import com.vuemov.model.Grammar;
import com.vuemov.repository.GrammarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GrammarService {
    
    @Autowired
    private GrammarRepository grammarRepository;
    
    public Grammar createGrammar(GrammarRequest request) {
        Grammar grammar = new Grammar(
            request.getTitle(),
            request.getExplanation(),
            request.getHskLevel(),
            request.getGrammarPattern()
        );
        grammar.setExampleSentences(request.getExampleSentences());
        grammar.setExampleTranslations(request.getExampleTranslations());
        grammar.setUsageNote(request.getUsageNote());
        grammar.setVideoUrl(request.getVideoUrl());
        grammar.setSimilarGrammar(request.getSimilarGrammar());
        
        return grammarRepository.save(grammar);
    }
    
    public Optional<Grammar> getGrammarById(String id) {
        return grammarRepository.findById(id);
    }
    
    public Grammar updateGrammar(String id, GrammarRequest request) {
        Optional<Grammar> existingGrammar = grammarRepository.findById(id);
        if (existingGrammar.isPresent()) {
            Grammar grammar = existingGrammar.get();
            grammar.setTitle(request.getTitle());
            grammar.setExplanation(request.getExplanation());
            grammar.setHskLevel(request.getHskLevel());
            grammar.setGrammarPattern(request.getGrammarPattern());
            grammar.setExampleSentences(request.getExampleSentences());
            grammar.setExampleTranslations(request.getExampleTranslations());
            grammar.setUsageNote(request.getUsageNote());
            grammar.setVideoUrl(request.getVideoUrl());
            grammar.setSimilarGrammar(request.getSimilarGrammar());
            grammar.setUpdatedAt(LocalDateTime.now());
            
            return grammarRepository.save(grammar);
        }
        throw new RuntimeException("Grammar not found");
    }
    
    public void deleteGrammar(String id) {
        grammarRepository.deleteById(id);
    }
    
    public List<Grammar> getGrammarByHskLevel(int hskLevel) {
        return grammarRepository.findByHskLevel(hskLevel);
    }
    
    public List<Grammar> getAllGrammar() {
        return grammarRepository.findAll();
    }
}
