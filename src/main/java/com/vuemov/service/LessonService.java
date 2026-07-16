package com.vuemov.service;

import com.vuemov.dto.LessonRequest;
import com.vuemov.model.Lesson;
import com.vuemov.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    
    @Autowired
    private LessonRepository lessonRepository;
    
    public Lesson createLesson(LessonRequest request) {
        Lesson lesson = new Lesson(
            request.getTitle(),
            request.getDescription(),
            request.getHskLevel(),
            request.getLessonNumber()
        );
        lesson.setContent(request.getContent());
        lesson.setAudioUrl(request.getAudioUrl());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setVocabularyIds(request.getVocabularyIds());
        lesson.setGrammarIds(request.getGrammarIds());
        
        return lessonRepository.save(lesson);
    }
    
    public Optional<Lesson> getLessonById(String id) {
        return lessonRepository.findById(id);
    }
    
    public Lesson updateLesson(String id, LessonRequest request) {
        Optional<Lesson> existingLesson = lessonRepository.findById(id);
        if (existingLesson.isPresent()) {
            Lesson lesson = existingLesson.get();
            lesson.setTitle(request.getTitle());
            lesson.setDescription(request.getDescription());
            lesson.setHskLevel(request.getHskLevel());
            lesson.setLessonNumber(request.getLessonNumber());
            lesson.setContent(request.getContent());
            lesson.setAudioUrl(request.getAudioUrl());
            lesson.setVideoUrl(request.getVideoUrl());
            lesson.setVocabularyIds(request.getVocabularyIds());
            lesson.setGrammarIds(request.getGrammarIds());
            lesson.setUpdatedAt(LocalDateTime.now());
            
            return lessonRepository.save(lesson);
        }
        throw new RuntimeException("Lesson not found");
    }
    
    public void deleteLesson(String id) {
        lessonRepository.deleteById(id);
    }
    
    public List<Lesson> getLessonsByHskLevel(int hskLevel) {
        return lessonRepository.findByHskLevelOrderByLessonNumber(hskLevel);
    }
    
    public Optional<Lesson> getLessonByHskLevelAndNumber(int hskLevel, int lessonNumber) {
        return lessonRepository.findByHskLevelAndLessonNumber(hskLevel, lessonNumber);
    }
    
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }
}
