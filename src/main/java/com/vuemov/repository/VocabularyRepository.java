package com.vuemov.repository;

import com.vuemov.model.Vocabulary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyRepository extends MongoRepository<Vocabulary, String> {
    
    Optional<Vocabulary> findBySimplifiedChinese(String simplifiedChinese);
    
    List<Vocabulary> findByHskLevel(int hskLevel);
    
    List<Vocabulary> findByPinyin(String pinyin);
    
    @Query("{ 'hskLevel': { $gte: ?0, $lte: ?1 } }")
    List<Vocabulary> findByHskLevelRange(int minLevel, int maxLevel);
    
    List<Vocabulary> findByPartOfSpeech(String partOfSpeech);
}
