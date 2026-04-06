package com.vuemov.service;

import com.vuemov.model.Comment;
import com.vuemov.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    
    public List<Comment> getCommentsBySlug(String slug) {
        return commentRepository.findBySlugOrderByCreatedAtDesc(slug);
    }
    
    public List<Comment> getCommentsByUser(String userId) {
        return commentRepository.findByUserId(userId);
    }
    
    public Comment addComment(String slug, String userId, String username, String content, int rating) {
        Comment comment = new Comment();
        comment.setSlug(slug);
        comment.setUserId(userId);
        comment.setUsername(username);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setCreatedAt(LocalDateTime.now());
        
        return commentRepository.save(comment);
    }
    
    public void deleteComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own comments");
        }
        
        commentRepository.delete(comment);
    }
}
