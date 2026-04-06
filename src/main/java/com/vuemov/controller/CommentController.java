package com.vuemov.controller;

import com.vuemov.dto.ApiResponse;
import com.vuemov.dto.CommentRequest;
import com.vuemov.model.Comment;
import com.vuemov.model.User;
import com.vuemov.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<List<Comment>>> getComments(@PathVariable String slug) {
        List<Comment> comments = commentService.getCommentsBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> addComment(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(
                request.getSlug(),
                user.getId(),
                user.getUsername(),
                request.getContent(),
                request.getRating()
        );
        return ResponseEntity.ok(ApiResponse.success("Comment added", comment));
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal User user,
            @PathVariable String commentId) {
        try {
            commentService.deleteComment(commentId, user.getId());
            return ResponseEntity.ok(ApiResponse.success("Comment deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
