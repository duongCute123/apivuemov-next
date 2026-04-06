package com.vuemov.repository;

import com.vuemov.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findBySlugOrderByCreatedAtDesc(String slug);
    List<Comment> findByUserId(String userId);
}
