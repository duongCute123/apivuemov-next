package com.vuemov.service;

import com.vuemov.dto.VocabularyRequest;
import com.vuemov.model.Vocabulary;
import com.vuemov.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VocabularyService {
    
    @Autowired
    private VocabularyRepository vocabularyRepository;
    
    public Vocabulary createVocabulary(VocabularyRequest request) {
        Vocabulary vocabulary = new Vocabulary(
            request.getSimplifiedChinese(),
            request.getPinyin(),
            request.getEnglishMeaning(),
            request.getVietnameseMeaning(),
            request.getHskLevel()
        );
        vocabulary.setTraditionalChinese(request.getTraditionalChinese());
        vocabulary.setPartOfSpeech(request.getPartOfSpeech());
        vocabulary.setExampleSentence(request.getExampleSentence());
        vocabulary.setExampleTranslation(request.getExampleTranslation());
        vocabulary.setAudioUrl(request.getAudioUrl());
        vocabulary.setImageUrl(request.getImageUrl());
        vocabulary.setMnemonicTips(request.getMnemonicTips());
        
        return vocabularyRepository.save(vocabulary);
    }
    
    public Optional<Vocabulary> getVocabularyById(String id) {
        return vocabularyRepository.findById(id);
    }
    
    public Vocabulary updateVocabulary(String id, VocabularyRequest request) {
        Optional<Vocabulary> existingVocab = vocabularyRepository.findById(id);
        if (existingVocab.isPresent()) {
            Vocabulary vocab = existingVocab.get();
            vocab.setSimplifiedChinese(request.getSimplifiedChinese());
            vocab.setTraditionalChinese(request.getTraditionalChinese());
            vocab.setPinyin(request.getPinyin());
            vocab.setEnglishMeaning(request.getEnglishMeaning());
            vocab.setVietnameseMeaning(request.getVietnameseMeaning());
            vocab.setPartOfSpeech(request.getPartOfSpeech());
            vocab.setHskLevel(request.getHskLevel());
            vocab.setExampleSentence(request.getExampleSentence());
            vocab.setExampleTranslation(request.getExampleTranslation());
            vocab.setAudioUrl(request.getAudioUrl());
            vocab.setImageUrl(request.getImageUrl());
            vocab.setMnemonicTips(request.getMnemonicTips());
            vocab.setUpdatedAt(LocalDateTime.now());
            
            return vocabularyRepository.save(vocab);
        }
        throw new RuntimeException("Vocabulary not found");
    }
    
    public void deleteVocabulary(String id) {
        vocabularyRepository.deleteById(id);
    }
    
    public List<Vocabulary> getVocabularyByHskLevel(int hskLevel) {
        return vocabularyRepository.findByHskLevel(hskLevel);
    }
    
    public List<Vocabulary> getVocabularyByHskLevelRange(int minLevel, int maxLevel) {
        return vocabularyRepository.findByHskLevelRange(minLevel, maxLevel);
    }
    
    public List<Vocabulary> getAllVocabulary() {
        return vocabularyRepository.findAll();
    }
}
