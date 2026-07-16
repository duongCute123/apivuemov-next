package com.vuemov.repository;

import com.vuemov.model.Grammar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrammarRepository extends MongoRepository<Grammar, String> {
    
    Optional<Grammar> findByTitle(String title);
    
    List<Grammar> findByHskLevel(int hskLevel);
}
